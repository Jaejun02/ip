package elyra.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import elyra.command.AddDeadlineCommand;
import elyra.command.AddEventCommand;
import elyra.command.AddTodoCommand;
import elyra.command.ByeCommand;
import elyra.command.Command;
import elyra.command.DeleteCommand;
import elyra.command.ListCommand;
import elyra.command.MarkCommand;
import elyra.command.UnmarkCommand;
import elyra.storage.Storage;

public class ParserTest {
    private final Parser parser = new Parser();

    @Test
    void parseCommandByeReturnsByeCommand() {
        Command command = parser.parseCommand("bye");
        assertInstanceOf(ByeCommand.class, command);
    }

    @Test
    void parseCommandListReturnsListCommand() {
        Command command = parser.parseCommand("list");
        assertInstanceOf(ListCommand.class, command);
    }

    @Test
    void parseCommandMarkReturnsMarkCommandWithIndex() {
        Command command = parser.parseCommand("mark 2");
        assertInstanceOf(MarkCommand.class, command);
        assertPrivateFieldEquals(command, "index", 2);
    }

    @Test
    void parseCommandUnmarkReturnsUnmarkCommandWithIndex() {
        Command command = parser.parseCommand("unmark 3");
        assertInstanceOf(UnmarkCommand.class, command);
        assertPrivateFieldEquals(command, "index", 3);
    }

    @Test
    void parseCommandTodoReturnsAddTodoCommandWithDescription() {
        Command command = parser.parseCommand("todo read book");
        assertInstanceOf(AddTodoCommand.class, command);
        assertPrivateFieldEquals(command, "description", "read book");
    }

    @Test
    void parseCommandDeadlineReturnsAddDeadlineCommandWithFields() {
        Command command = parser.parseCommand("deadline submit report /by 2024-02-01 12:30");
        assertInstanceOf(AddDeadlineCommand.class, command);
        assertPrivateFieldEquals(command, "description", "submit report");
        assertPrivateFieldEquals(command, "by", LocalDateTime.of(2024, 2, 1,
                12, 30));
    }

    @Test
    void parseCommandEventReturnsAddEventCommandWithFields() {
        Command command = parser.parseCommand("event meeting /from 2024-03-10 09:00 /to 2024-03-10 10:30");
        assertInstanceOf(AddEventCommand.class, command);
        assertPrivateFieldEquals(command, "description", "meeting");
        assertPrivateFieldEquals(command, "startAt", LocalDateTime.of(2024, 3, 10,
                9, 0));
        assertPrivateFieldEquals(command, "endAt", LocalDateTime.of(2024, 3, 10,
                10, 30));
    }

    @Test
    void parseCommandDeleteReturnsDeleteCommandWithIndex() {
        Command command = parser.parseCommand("delete 4");
        assertInstanceOf(DeleteCommand.class, command);
        assertPrivateFieldEquals(command, "index", 4);
    }

    @Test
    void parseCommandUnknownCommandThrowsWithMessage() {
        assertThrowsWithMessage("unknown",
                "I'm sorry, but I don't recognize the command provided!");
    }

    @Test
    void parseCommandReservedDelimiterThrowsWithMessage() {
        String reserved = Storage.DELIM.strip();
        assertThrowsWithMessage("todo use " + reserved + " here",
                "'" + reserved + "' is reserved and cannot be used in task descriptions!");
    }

    @Test
    void parseCommandByeWithArgumentsThrowsWithMessage() {
        assertThrowsWithMessage("bye now", "The 'bye' command does not take any arguments!");
    }

    @Test
    void parseCommandListWithArgumentsThrowsWithMessage() {
        assertThrowsWithMessage("list all", "The 'list' command does not take any arguments!");
    }

    @Test
    void parseCommandMarkWithoutIndexThrowsWithMessage() {
        assertThrowsWithMessage("mark", "The 'mark' command requires exactly one argument!");
    }

    @Test
    void parseCommandMarkWithNonNumericIndexThrowsWithMessage() {
        assertThrowsWithMessage("mark two", "The 'mark' command requires a numeric argument!");
    }

    @Test
    void parseCommandUnmarkWithTooManyArgsThrowsWithMessage() {
        assertThrowsWithMessage("unmark 1 2",
                "The 'unmark' command requires exactly one argument!");
    }

    @Test
    void parseCommandUnmarkWithNonNumericIndexThrowsWithMessage() {
        assertThrowsWithMessage("unmark two", "The 'unmark' command requires a numeric argument!");
    }

    @Test
    void parseCommandTodoWithoutDescriptionThrowsWithMessage() {
        assertThrowsWithMessage("todo", "The 'todo' command requires a description!");
    }

    @Test
    void parseCommandDeadlineMissingByThrowsWithMessage() {
        assertThrowsWithMessage("deadline read book",
                "The 'deadline' command requires a description and a due date!");
    }

    @Test
    void parseCommandDeadlineEmptyDescriptionThrowsWithMessage() {
        assertThrowsWithMessage("deadline /by 2024-02-01 12:30",
                "The 'deadline' command requires a non-empty description!");
    }

    @Test
    void parseCommandDeadlineInvalidDateThrowsWithMessage() {
        assertThrowsWithMessage("deadline read book /by 2024/02/18 10:00",
                "Invalid date/time format! Please use 'yyyy-MM-dd HH:mm'.");
    }

    @Test
    void parseCommandEventMissingEndThrowsWithMessage() {
        assertThrowsWithMessage("event meeting /from 2024-03-10 09:00",
                "The 'event' command requires a description, start time, and end time!");
    }

    @Test
    void parseCommandEventEmptyDescriptionThrowsWithMessage() {
        assertThrowsWithMessage("event /from 2024-03-10 09:00 /to 2024-03-10 10:30",
                "The 'event' command requires a non-empty description!");
    }

    @Test
    void parseCommandEventInvalidDateThrowsWithMessage() {
        assertThrowsWithMessage("event meeting /from 2024/03/10 09:00 /to 2024/12/10 10:30",
                "Invalid date/time format! Please use 'yyyy-MM-dd HH:mm'.");
    }

    @Test
    void parseCommandDeleteWithoutIndexThrowsWithMessage() {
        assertThrowsWithMessage("delete", "The 'delete' command requires exactly one argument!");
    }

    @Test
    void parseCommandDeleteWithNonNumericIndexThrowsWithMessage() {
        assertThrowsWithMessage("delete one", "The 'delete' command requires a numeric argument!");
    }

    @Test
    void parseCommandExtraWhitespaceReturnsMarkCommand() {
        Command command = parser.parseCommand("   mark    5   ");
        assertInstanceOf(MarkCommand.class, command);
        assertPrivateFieldEquals(command, "index", 5);
    }

    @Test
    void parseCommandMixedCaseReturnsCorrectCommand() {
        Command command1 = parser.parseCommand("LiSt");
        assertInstanceOf(ListCommand.class, command1);

        Command command2 = parser.parseCommand("ToDo read book");
        assertInstanceOf(AddTodoCommand.class, command2);
        assertPrivateFieldEquals(command2, "description", "read book");

        Command command3 = parser.parseCommand("EvenT meeting /from 2024-03-10 09:00 /to 2024-03-10 10:30");
        assertInstanceOf(AddEventCommand.class, command3);
        assertPrivateFieldEquals(command3, "description", "meeting");
        assertPrivateFieldEquals(command3, "startAt", LocalDateTime.of(2024, 3, 10,
                9, 0));
        assertPrivateFieldEquals(command3, "endAt", LocalDateTime.of(2024, 3, 10,
                10, 30));
    }

    private void assertThrowsWithMessage(String input, String expectedMessage) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> parser.parseCommand(input));
        assertEquals(expectedMessage, exception.getMessage());
    }

    private void assertPrivateFieldEquals(Object target, String fieldName, Object expected) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            assertEquals(expected, field.get(target));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access field '" + fieldName + "': " + e.getMessage());
        }
    }
}
