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
    private static final int DEADLINE_FIELDS_NUM = 4;
    private static final int EVENT_FIELDS_NUM = 5;
    private static final int TASK_FIELDS_NUM = 3;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            .withResolverStyle(ResolverStyle.STRICT);

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
        assert tasks != null : "saveTasks called with null TaskList";
        assert tasks.getTasks() != null : "TaskList.getTasks() returned null";

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
            String errorMessage = String.format("Data file is corrupted at line %d: unknown task type '%s'.",
                    lineNumber, taskType);
            throw new IOException(errorMessage);
        }
    }

    private void validateMinFields(String[] parts, int lineNumber) throws IOException {
        if (parts.length < TASK_FIELDS_NUM) {
            String errorMessage = String.format("Data file is corrupted at line %d: not enough fields for a task.",
                    lineNumber);
            throw new IOException(errorMessage);
        }
    }

    private boolean parseDoneStatus(String isDone, int lineNumber) throws IOException {
        String DONE_FLAG = "1";
        String NOT_DONE_FLAG = "0";
        if (isDone.equals(DONE_FLAG)) {
            return true;
        } else if (isDone.equals(NOT_DONE_FLAG)) {
            return false;
        } else {
            String errorMessage = String.format(
                    "Data file is corrupted at line %d: invalid done flag '%s' (expected 0 or 1).",
                    lineNumber, isDone);
            throw new IOException(errorMessage);
        }
    }

    private Task parseToDo(String description, boolean isDone) {
        return new ToDo(description, isDone);
    }

    private Task parseDeadline(String[] parts, String description,
                               boolean isDone, int lineNumber) throws IOException {
        if (parts.length < DEADLINE_FIELDS_NUM) {
            String errorMessage = String.format(
                    "Data file is corrupted at line %d: deadline is missing field(s).",
                    lineNumber);
            throw new IOException(errorMessage);
        }
        if (parts.length > DEADLINE_FIELDS_NUM) {
            String errorMessage = String.format(
                    "Data file is corrupted at line %d: deadline has too many fields.",
                    lineNumber);
            throw new IOException(errorMessage);
        }
        LocalDateTime by = parseDateTime(parts[3].trim(), lineNumber);
        return new Deadline(description, isDone, by);
    }

    private Task parseEvent(String[] parts, String description,
                            boolean isDone, int lineNumber) throws IOException {
        if (parts.length < EVENT_FIELDS_NUM) {
            String errorMessage = String.format(
                    "Data file is corrupted at line %d: event is missing field(s).",
                    lineNumber);
            throw new IOException(errorMessage);
        }
        if (parts.length > EVENT_FIELDS_NUM) {
            String errorMessage = String.format(
                    "Data file is corrupted at line %d: event has too many fields.",
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
            String errorMessage = String.format(
                    "Data file is corrupted at line %d: invalid date/time format "
                            + "(expected yyyy-MM-ddTHH:mm[:ss]).",
                    lineNumber);
            throw new IOException(errorMessage);
        }
    }

    private String serializeTask(Task task) {
        assert task != null : "Task to serialize should not be null";

        String[] taskInfos = task.getInfos(this.timeFormatter);
        assert taskInfos != null : "task.getInfos returned null";
        assert taskInfos.length >= 3 : "Serialized task must have at least 3 fields (type, done, desc)";
        assert taskInfos[0].equals("T") || taskInfos[0].equals("D") || taskInfos[0].equals("E")
                : "Unknown task type (" + taskInfos[0] + ") in serialized task";
        for (String field : taskInfos) {
            assert field != null : "Serialized task's field should not be null";
            assert !field.contains(DELIM) : "Field contains reserved delimiter: " + field;
        }
        return String.join(DELIM, taskInfos);
    }
}
