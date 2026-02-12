package elyra.storage;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;
import java.util.regex.Pattern;

import elyra.task.Deadline;
import elyra.task.Event;
import elyra.task.Task;
import elyra.task.TaskList;
import elyra.task.TaskType;
import elyra.task.ToDo;

/**
 * Represents persistent storage for tasks.
 * A Storage object handles loading tasks from and saving tasks to a file.
 */
public class Storage {
    public static final String DEFAULT_PATH = "./data/elyra.txt";
    public static final String DELIM = " ||| ";
    private static final String DELIM_REGEX = Pattern.quote(DELIM);
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            .withResolverStyle(ResolverStyle.STRICT);
    private static final int MIN_TASK_FIELDS = 3;
    private static final int MIN_DEADLINE_FIELDS = 4;
    private static final int MIN_EVENT_FIELDS = 5;

    private final Path filePath;

    /**
     * Creates a new Storage instance with the specified file path.
     *
     * @param filePath Path to the file where tasks will be stored.
     */
    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Loads tasks from the storage file.
     *
     * @return TaskList containing all tasks loaded from the file.
     * @throws IOException If an error occurs while reading the file or data is corrupted.
     */
    public TaskList loadTasks() throws IOException {
        if (Files.notExists(this.filePath)) {
            return new TaskList();
        }

        try (Scanner scanner = new Scanner(this.filePath)) {
            TaskList tasks = new TaskList();
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                lineNumber += 1;
                if (line.isEmpty()) {
                    continue;
                }
                Task task = parseTaskFromLine(line, lineNumber);
                tasks.addTask(task);
            }
            return tasks;
        }
    }

    /**
     * Saves tasks to the storage file.
     *
     * @param tasks TaskList containing all tasks to be saved.
     * @throws IOException If an error occurs while writing to the file.
     */
    public void saveTasks(TaskList tasks) throws IOException {
        Path parent = filePath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (FileWriter writer = new FileWriter(filePath.toFile(), false)) {
            for (Task task : tasks.getTasks()) {
                writer.write(serializeTask(task) + System.lineSeparator());
            }
        }
    }

    private Task parseTaskFromLine(String line, int lineNumber) throws IOException {
        String[] parts = line.split(DELIM_REGEX);
        validateMinFields(parts, lineNumber);

        String taskType = parts[0];
        boolean isDone = parseDoneStatus(parts[1], lineNumber);
        String description = parts[2];

        try {
            TaskType currentTaskType = TaskType.fromStorageCode(taskType);
            return switch (currentTaskType) {
                case TODO -> parseToDo(description, isDone);
                case DEADLINE -> parseDeadline(parts, description, isDone, lineNumber);
                case EVENT -> parseEvent(parts, description, isDone, lineNumber);
                default -> throw new AssertionError("Unreachable state: Unknown command is parsed.");
            };
        } catch (IllegalArgumentException e) {
            String errorMessage = String.format("Found corrupted data at line %d (unknown task type): '%s'",
                    lineNumber, taskType);
            throw new IOException(errorMessage);
        }
    }

    private void validateMinFields(String[] parts, int lineNumber) throws IOException {
        if (parts.length < MIN_TASK_FIELDS) {
            String errorMessage = String.format("Found corrupted data at line %d (too few fields for Tasks).",
                    lineNumber);
            throw new IOException(errorMessage);
        }
    }

    private boolean parseDoneStatus(String isDone, int lineNumber) throws IOException {
        if (isDone.equals("1")) {
            return true;
        } else if (isDone.equals("0")) {
            return false;
        } else {
            String errorMessage = String.format("Found corrupted data at line %d (invalid done status): '%s'",
                    lineNumber, isDone);
            throw new IOException(errorMessage);
        }
    }

    private Task parseToDo(String description, boolean isDone) {
        return new ToDo(description, isDone);
    }

    private Task parseDeadline(String[] parts, String description,
                               boolean isDone, int lineNumber) throws IOException {
        if (parts.length < MIN_DEADLINE_FIELDS) {
            String errorMessage = String.format("Found corrupted data at line %d (too few fields for Deadline).",
                    lineNumber);
            throw new IOException(errorMessage);
        }
        LocalDateTime by = parseDateTime(parts[3].trim(), lineNumber);
        return new Deadline(description, isDone, by);
    }

    private Task parseEvent(String[] parts, String description,
                            boolean isDone, int lineNumber) throws IOException {
        if (parts.length < MIN_EVENT_FIELDS) {
            String errorMessage = String.format("Found corrupted data at line %d (too few fields for Event).",
                    lineNumber);
            throw new IOException(errorMessage);
        }
        LocalDateTime from = parseDateTime(parts[3].trim(), lineNumber);
        LocalDateTime to = parseDateTime(parts[4].trim(), lineNumber);
        return new Event(description, isDone, from, to);
    }

    private LocalDateTime parseDateTime(String dateTimeStr, int lineNumber) throws IOException {
        try {
            return LocalDateTime.parse(dateTimeStr, this.timeFormatter);
        } catch (DateTimeParseException e) {
            String errorMessage = String.format("Found corrupted data at line %d (invalid date & time format).",
                    lineNumber);
            throw new IOException(errorMessage);
        }
    }

    private String serializeTask(Task task) {
        String[] taskInfos = task.getInfos(this.timeFormatter);
        return String.join(DELIM, taskInfos);
    }
}
