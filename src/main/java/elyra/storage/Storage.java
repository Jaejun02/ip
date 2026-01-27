package elyra.storage;

import elyra.task.Task;
import elyra.task.TaskList;
import elyra.task.ToDo;
import elyra.task.Deadline;
import elyra.task.Event;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Storage {
    public static final String DEFAULT_PATH = "./data/elyra.txt";
    private static final String DELIM = " \\|\\| ";

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

    private Task parseTaskFromLine(String line, int lineNumber) throws IOException {
        String[] parts = line.split(DELIM);
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
            String by = parts[3];
            return new Deadline(description, isDone, by);
        case "E":
            if (parts.length < 5) {
                String errorMessage = String.format("Found corrupted data at line %d (too few fields for Event).",
                        lineNumber);
                throw new IOException(errorMessage);
            }
            String from = parts[3];
            String to = parts[4];
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
}