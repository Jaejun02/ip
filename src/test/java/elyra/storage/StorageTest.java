package elyra.storage;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import elyra.task.Deadline;
import elyra.task.Event;
import elyra.task.Task;
import elyra.task.TaskList;
import elyra.task.ToDo;

public class StorageTest {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            .withResolverStyle(ResolverStyle.STRICT);

    @TempDir
    Path tempDir;

    @Test
    void loadTasksHappyPathParsesAllTaskTypes() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        String delim = Storage.DELIM;
        String content = String.join(System.lineSeparator(),
                "T" + delim + "1" + delim + "read book",
                "D" + delim + "0" + delim + "submit report" + delim + "2024-02-01T12:30",
                "E" + delim + "1" + delim + "meeting" + delim + "2024-03-10T09:00" + delim + "2024-03-10T10:30");
        Files.writeString(file, content);

        Storage storage = new Storage(file.toString());
        TaskList tasks = storage.loadTasks();

        assertEquals(3, tasks.getTasks().size());
        assertInstanceOf(ToDo.class, tasks.getTask(1));
        assertTaskInfos(tasks.getTask(1), new String[] {"T", "1", "read book"});

        assertInstanceOf(Deadline.class, tasks.getTask(2));
        assertTaskInfos(tasks.getTask(2), new String[] {"D", "0", "submit report", "2024-02-01T12:30:00"});

        assertInstanceOf(Event.class, tasks.getTask(3));
        assertTaskInfos(tasks.getTask(3), new String[] {"E", "1", "meeting",
                "2024-03-10T09:00:00", "2024-03-10T10:30:00"});
    }

    @Test
    void loadTasksMissingFileReturnsEmptyTaskList() throws IOException {
        Path file = tempDir.resolve("missing.txt");
        Storage storage = new Storage(file.toString());

        TaskList tasks = storage.loadTasks();

        assertEquals(0, tasks.getTasks().size());
    }

    @Test
    void loadTasksSkipsEmptyLines() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        String delim = Storage.DELIM;
        String content = String.join(System.lineSeparator(),
                "",
                "   ",
                "T" + delim + "0" + delim + "read book");
        Files.writeString(file, content);

        Storage storage = new Storage(file.toString());
        TaskList tasks = storage.loadTasks();

        assertEquals(1, tasks.getTasks().size());
        assertTaskInfos(tasks.getTask(1), new String[] {"T", "0", "read book"});
    }

    @Test
    void loadTasksTooFewFieldsThrowsIOException() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        Files.writeString(file, "T" + Storage.DELIM + "1");

        Storage storage = new Storage(file.toString());
        IOException exception = assertThrows(IOException.class, storage::loadTasks);
        assertEquals("Found corrupted data at line 1 (too few fields for Tasks).", exception.getMessage());
    }

    @Test
    void loadTasksInvalidDoneStatusThrowsIOException() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        Files.writeString(file, "T" + Storage.DELIM + "X" + Storage.DELIM + "read book");

        Storage storage = new Storage(file.toString());
        IOException exception = assertThrows(IOException.class, storage::loadTasks);
        assertEquals("Found corrupted data at line 1 (invalid done status): 'X'", exception.getMessage());
    }

    @Test
    void loadTasksUnknownTaskTypeThrowsIOException() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        Files.writeString(file, "Z" + Storage.DELIM + "0" + Storage.DELIM + "read book");

        Storage storage = new Storage(file.toString());
        IOException exception = assertThrows(IOException.class, storage::loadTasks);
        assertEquals("Found corrupted data at line 1 (unknown task type): 'Z'", exception.getMessage());
    }

    @Test
    void loadTasksDeadlineTooFewFieldsThrowsIOException() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        Files.writeString(file, "D" + Storage.DELIM + "0" + Storage.DELIM + "submit report");

        Storage storage = new Storage(file.toString());
        IOException exception = assertThrows(IOException.class, storage::loadTasks);
        assertEquals("Found corrupted data at line 1 (too few fields for Deadline).", exception.getMessage());
    }

    @Test
    void loadTasksEventTooFewFieldsThrowsIOException() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        Files.writeString(file, "E" + Storage.DELIM + "0" + Storage.DELIM + "meeting"
                + Storage.DELIM + "2024-03-10T09:00");

        Storage storage = new Storage(file.toString());
        IOException exception = assertThrows(IOException.class, storage::loadTasks);
        assertEquals("Found corrupted data at line 1 (too few fields for Event).", exception.getMessage());
    }

    @Test
    void loadTasksInvalidDateTimeThrowsIOException() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        Files.writeString(file, "D" + Storage.DELIM + "0" + Storage.DELIM + "submit report"
                + Storage.DELIM + "2024-13-01T12:30");

        Storage storage = new Storage(file.toString());
        IOException exception = assertThrows(IOException.class, storage::loadTasks);
        assertEquals("Found corrupted data at line 1 (invalid date & time format).", exception.getMessage());
    }

    private void assertTaskInfos(Task task, String[] expected) {
        assertArrayEquals(expected, task.getInfos(timeFormatter));
    }
}
