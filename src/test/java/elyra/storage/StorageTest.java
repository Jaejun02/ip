package elyra.storage;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import elyra.task.Deadline;
import elyra.task.Event;
import elyra.task.Task;
import elyra.task.TaskList;
import elyra.task.ToDo;

// Solution below was adapted from AI generated draft of StorageTest, with modifications to match the actual
// Storage implementation and task formats. Major help was on generating repetitive test cases for various error
// scenarios, and on generating the expected content format for saveTasks test.
public class StorageTest {
    public static final String DONE_FLAG = "1";
    public static final String NOT_DONE_FLAG = "0";
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            .withResolverStyle(ResolverStyle.STRICT);

    @TempDir
    Path tempDir;

    @Test
    void loadTasks_happyPath_parsesAllTaskTypes() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        String delim = Storage.DELIM;
        String content = String.join(System.lineSeparator(),
                "T" + delim + DONE_FLAG + delim + "read book",
                "D" + delim + NOT_DONE_FLAG + delim + "submit report" + delim + "2024-02-01T12:30",
                "E" + delim + DONE_FLAG + delim + "meeting" + delim + "2024-03-10T09:00" + delim + "2024-03-10T10:30");
        Files.writeString(file, content);

        Storage storage = new Storage(file.toString());
        TaskList tasks = storage.loadTasks();

        assertEquals(3, tasks.getTasks().size());
        assertInstanceOf(ToDo.class, tasks.getTask(1));
        assertTaskInfos(tasks.getTask(1), new String[] {"T", DONE_FLAG, "read book"});

        assertInstanceOf(Deadline.class, tasks.getTask(2));
        assertTaskInfos(tasks.getTask(2),
                new String[] {"D", NOT_DONE_FLAG, "submit report", "2024-02-01T12:30:00"});

        assertInstanceOf(Event.class, tasks.getTask(3));
        assertTaskInfos(tasks.getTask(3), new String[] {"E", DONE_FLAG, "meeting",
                "2024-03-10T09:00:00", "2024-03-10T10:30:00"});
    }

    @Test
    void loadTasks_missingFile_returnsEmptyTaskList() throws IOException {
        Path file = tempDir.resolve("missing.txt");
        Storage storage = new Storage(file.toString());

        TaskList tasks = storage.loadTasks();

        assertEquals(0, tasks.getTasks().size());
    }

    @Test
    void loadTasks_emptyLines_skipsEmptyLines() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        String delim = Storage.DELIM;
        String content = String.join(System.lineSeparator(),
                "",
                "   ",
                "T" + delim + NOT_DONE_FLAG + delim + "read book");
        Files.writeString(file, content);

        Storage storage = new Storage(file.toString());
        TaskList tasks = storage.loadTasks();

        assertEquals(1, tasks.getTasks().size());
        assertTaskInfos(tasks.getTask(1), new String[] {"T", NOT_DONE_FLAG, "read book"});
    }

    @Test
    void loadTasks_tooFewFields_throwsIoException() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        Files.writeString(file, "T" + Storage.DELIM + DONE_FLAG);

        Storage storage = new Storage(file.toString());
        IOException exception = assertThrows(IOException.class, storage::loadTasks);
        assertEquals("Data file is corrupted at line 1: not enough fields for a task.", exception.getMessage());
    }

    @Test
    void loadTasks_invalidDoneStatus_throwsIoException() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        Files.writeString(file, "T" + Storage.DELIM + "X" + Storage.DELIM + "read book");

        Storage storage = new Storage(file.toString());
        IOException exception = assertThrows(IOException.class, storage::loadTasks);
        assertEquals("Data file is corrupted at line 1: invalid done flag 'X' (expected 0 or 1).",
                exception.getMessage());
    }

    @Test
    void loadTasks_unknownTaskType_throwsIoException() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        Files.writeString(file, "Z" + Storage.DELIM + NOT_DONE_FLAG + Storage.DELIM + "read book");

        Storage storage = new Storage(file.toString());
        IOException exception = assertThrows(IOException.class, storage::loadTasks);
        assertEquals("Data file is corrupted at line 1: unknown task type 'Z'.", exception.getMessage());
    }

    @Test
    void loadTasks_deadlineTooFewFields_throwsIoException() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        Files.writeString(file, "D" + Storage.DELIM + NOT_DONE_FLAG + Storage.DELIM + "submit report");

        Storage storage = new Storage(file.toString());
        IOException exception = assertThrows(IOException.class, storage::loadTasks);
        assertEquals("Data file is corrupted at line 1: deadline is missing field(s).", exception.getMessage());
    }

    @Test
    void loadTasks_eventTooFewFields_throwsIoException() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        Files.writeString(file, "E" + Storage.DELIM + NOT_DONE_FLAG + Storage.DELIM + "meeting"
                + Storage.DELIM + "2024-03-10T09:00");

        Storage storage = new Storage(file.toString());
        IOException exception = assertThrows(IOException.class, storage::loadTasks);
        assertEquals("Data file is corrupted at line 1: event is missing field(s).", exception.getMessage());
    }

    @Test
    void loadTasks_invalidDateTime_throwsIoException() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        Files.writeString(file, "D" + Storage.DELIM + NOT_DONE_FLAG + Storage.DELIM + "submit report"
                + Storage.DELIM + "2024-13-01T12:30");

        Storage storage = new Storage(file.toString());
        IOException exception = assertThrows(IOException.class, storage::loadTasks);
        assertEquals("Data file is corrupted at line 1: invalid date/time format "
                + "(expected yyyy-MM-ddTHH:mm[:ss]).", exception.getMessage());
    }

    @Test
    void saveTasks_missingDirectories_createsDirectoriesAndWritesContent() throws IOException {
        Path file = tempDir.resolve("nested").resolve("tasks.txt");
        Storage storage = new Storage(file.toString());

        TaskList tasks = new TaskList();
        tasks.addTask(new ToDo("read book", true));
        tasks.addTask(new Deadline("submit report", false,
                LocalDateTime.of(2024, 2, 1, 12, 30)));
        tasks.addTask(new Event("meeting", true,
                LocalDateTime.of(2024, 3, 10, 9, 0),
                LocalDateTime.of(2024, 3, 10, 10, 30)));

        storage.saveTasks(tasks);

        String delim = Storage.DELIM;
        String expectedContent = String.join(System.lineSeparator(),
                "T" + delim + DONE_FLAG + delim + "read book",
                "D" + delim + NOT_DONE_FLAG + delim + "submit report" + delim + "2024-02-01T12:30:00",
                "E" + delim + DONE_FLAG + delim + "meeting" + delim + "2024-03-10T09:00:00"
                        + delim + "2024-03-10T10:30:00") + System.lineSeparator();
        assertEquals(expectedContent, Files.readString(file));
    }

    private void assertTaskInfos(Task task, String[] expected) {
        assertArrayEquals(expected, task.getInfos(timeFormatter));
    }
}
