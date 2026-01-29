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
import elyra.task.ToDo;

public class Storage {
    public static final String DEFAULT_PATH = "./data/elyra.txt";
    public static final String DELIM = " ||| ";
    private static final String DELIM_REGEX = Pattern.quote(DELIM);
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        .withResolverStyle(ResolverStyle.STRICT);

    private final Path filePath;

    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

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
        if (parts.length < 3) {
            String errorMessage = String.format("Found corrupted data at line %d (too few fields for Tasks).",
                    lineNumber);
            throw new IOException(errorMessage);
        }

        String taskType = parts[0];
        boolean isDone = parseDoneStatus(parts[1], lineNumber);
        String description = parts[2];

        switch (taskType) {
        case "T":
            return new ToDo(description, isDone);
        case "D":
            if (parts.length < 4) {
                String errorMessage = String.format("Found corrupted data at line %d (too few fields for Deadline).",
                        lineNumber);
                throw new IOException(errorMessage);
            }
            LocalDateTime by = parseDateTime(parts[3].trim(), lineNumber);
            return new Deadline(description, isDone, by);
        case "E":
            if (parts.length < 5) {
                String errorMessage = String.format("Found corrupted data at line %d (too few fields for Event).",
                        lineNumber);
                throw new IOException(errorMessage);
            }
            LocalDateTime from = parseDateTime(parts[3].trim(), lineNumber);
            LocalDateTime to = parseDateTime(parts[4].trim(), lineNumber);
            return new Event(description, isDone, from, to);
        default:
            String errorMessage = String.format("Found corrupted data at line %d (unknown task type): '%s'",
                    lineNumber, taskType);
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
