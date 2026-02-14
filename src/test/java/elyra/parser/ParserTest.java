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
import elyra.command.FindCommand;
import elyra.command.ListCommand;
import elyra.command.MarkCommand;
import elyra.command.UnmarkCommand;
import elyra.command.UpdateCommand;
import elyra.storage.Storage;

// Solution below was adapted from AI generated draft of ParserTest, with modifications to match the actual
// Parser implementation and command formats. Major help was on generating repetitive test cases for various error
// scenarios, and on generating the expected content format for each command type.
public class ParserTest {
    private final Parser parser = new Parser();

    @Test
    void parseCommand_bye_returnsByeCommand() {
        Command command = parser.parseCommand("bye");
        assertInstanceOf(ByeCommand.class, command);
    }

    @Test
    void parseCommand_list_returnsListCommand() {
        Command command = parser.parseCommand("list");
        assertInstanceOf(ListCommand.class, command);
    }

    @Test
    void parseCommand_markWithIndex_returnsMarkCommand() {
        Command command = parser.parseCommand("mark 2");
        assertInstanceOf(MarkCommand.class, command);
        assertPrivateFieldEquals(command, "index", 2);
    }

    @Test
    void parseCommand_unmarkWithIndex_returnsUnmarkCommand() {
        Command command = parser.parseCommand("unmark 3");
        assertInstanceOf(UnmarkCommand.class, command);
        assertPrivateFieldEquals(command, "index", 3);
    }

    @Test
    void parseCommand_todoWithDescription_returnsAddTodoCommand() {
        Command command = parser.parseCommand("todo read book");
        assertInstanceOf(AddTodoCommand.class, command);
        assertPrivateFieldEquals(command, "description", "read book");
    }

    @Test
    void parseCommand_deadlineWithFields_returnsAddDeadlineCommand() {
        Command command = parser.parseCommand("deadline submit report /by 2024-02-01 12:30");
        assertInstanceOf(AddDeadlineCommand.class, command);
        assertPrivateFieldEquals(command, "description", "submit report");
        assertPrivateFieldEquals(command, "by", LocalDateTime.of(2024, 2, 1,
                12, 30));
    }

    @Test
    void parseCommand_eventWithFields_returnsAddEventCommand() {
        Command command = parser.parseCommand("event meeting /from 2024-03-10 09:00 /to 2024-03-10 10:30");
        assertInstanceOf(AddEventCommand.class, command);
        assertPrivateFieldEquals(command, "description", "meeting");
        assertPrivateFieldEquals(command, "startAt", LocalDateTime.of(2024, 3, 10,
                9, 0));
        assertPrivateFieldEquals(command, "endAt", LocalDateTime.of(2024, 3, 10,
                10, 30));
    }

    @Test
    void parseCommand_deleteWithIndex_returnsDeleteCommand() {
        Command command = parser.parseCommand("delete 4");
        assertInstanceOf(DeleteCommand.class, command);
        assertPrivateFieldEquals(command, "index", 4);
    }

    @Test
    void parseCommand_findWithKeyword_returnsFindCommand() {
        Command command = parser.parseCommand("find read book");
        assertInstanceOf(FindCommand.class, command);
        assertPrivateFieldEquals(command, "keyword", "read book");
    }

    @Test
    void parseCommand_updateTextField_returnsUpdateCommand() {
        Command command = parser.parseCommand("update 2 /field description /with new title");
        assertInstanceOf(UpdateCommand.class, command);
        assertPrivateFieldEquals(command, "index", 2);
        assertPrivateFieldEquals(command, "fieldToUpdate", "description");
        assertPrivateFieldEquals(command, "newTextContent", "new title");
        assertPrivateFieldEquals(command, "newDateTimeContent", null);
    }

    @Test
    void parseCommand_updateDateTimeField_returnsUpdateCommand() {
        Command command = parser.parseCommand("update 3 /field by /with 2024-02-01 12:30");
        assertInstanceOf(UpdateCommand.class, command);
        assertPrivateFieldEquals(command, "index", 3);
        assertPrivateFieldEquals(command, "fieldToUpdate", "by");
        assertPrivateFieldEquals(command, "newDateTimeContent",
                LocalDateTime.of(2024, 2, 1, 12, 30));
        assertPrivateFieldEquals(command, "newTextContent", null);
    }

    @Test
    void parseCommand_unknownCommand_throwsWithMessage() {
        assertThrowsWithMessage("unknown",
                "I'm sorry, but I don't recognize the command provided!");
    }

    @Test
    void parseCommand_reservedDelimiter_throwsWithMessage() {
        String reserved = Storage.DELIM.strip();
        assertThrowsWithMessage("todo use " + reserved + " here",
                "'" + reserved + "' is reserved and cannot be used in task descriptions!");
    }

    @Test
    void parseCommand_byeWithArguments_throwsWithMessage() {
        assertThrowsWithMessage("bye now", "The 'bye' command does not take any arguments!");
    }

    @Test
    void parseCommand_listWithArguments_throwsWithMessage() {
        assertThrowsWithMessage("list all", "The 'list' command does not take any arguments!");
    }

    @Test
    void parseCommand_markWithoutIndex_throwsWithMessage() {
        assertThrowsWithMessage("mark", "The 'mark' command requires exactly one argument!");
    }

    @Test
    void parseCommand_markWithNonNumericIndex_throwsWithMessage() {
        assertThrowsWithMessage("mark two", "The 'mark' command requires a numeric argument!");
    }

    @Test
    void parseCommand_unmarkWithTooManyArgs_throwsWithMessage() {
        assertThrowsWithMessage("unmark 1 2",
                "The 'unmark' command requires exactly one argument!");
    }

    @Test
    void parseCommand_unmarkWithNonNumericIndex_throwsWithMessage() {
        assertThrowsWithMessage("unmark two", "The 'unmark' command requires a numeric argument!");
    }

    @Test
    void parseCommand_todoWithoutDescription_throwsWithMessage() {
        assertThrowsWithMessage("todo", "The 'todo' command requires a description!");
    }

    @Test
    void parseCommand_deadlineMissingBy_throwsWithMessage() {
        assertThrowsWithMessage("deadline read book",
                "The 'deadline' command requires a description and a due date!");
    }

    @Test
    void parseCommand_deadlineEmptyDescription_throwsWithMessage() {
        assertThrowsWithMessage("deadline /by 2024-02-01 12:30",
                "The 'deadline' command requires a non-empty description!");
    }

    @Test
    void parseCommand_deadlineInvalidDate_throwsWithMessage() {
        assertThrowsWithMessage("deadline read book /by 2024/02/18 10:00",
                "Invalid date/time format! Please use 'yyyy-MM-dd HH:mm'.");
    }

    @Test
    void parseCommand_eventMissingEnd_throwsWithMessage() {
        assertThrowsWithMessage("event meeting /from 2024-03-10 09:00",
                "The 'event' command requires a description, start time, and end time!");
    }

    @Test
    void parseCommand_eventEmptyDescription_throwsWithMessage() {
        assertThrowsWithMessage("event /from 2024-03-10 09:00 /to 2024-03-10 10:30",
                "The 'event' command requires a non-empty description!");
    }

    @Test
    void parseCommand_eventInvalidDate_throwsWithMessage() {
        assertThrowsWithMessage("event meeting /from 2024/03/10 09:00 /to 2024/12/10 10:30",
                "Invalid date/time format! Please use 'yyyy-MM-dd HH:mm'.");
    }

    @Test
    void parseCommand_deleteWithoutIndex_throwsWithMessage() {
        assertThrowsWithMessage("delete", "The 'delete' command requires exactly one argument!");
    }

    @Test
    void parseCommand_deleteWithNonNumericIndex_throwsWithMessage() {
        assertThrowsWithMessage("delete one", "The 'delete' command requires a numeric argument!");
    }

    @Test
    void parseCommand_findWithoutKeyword_throwsWithMessage() {
        assertThrowsWithMessage("find", "The 'find' command requires a keyword (or phrase) to search for!");
    }

    @Test
    void parseCommand_updateMissingArguments_throwsWithMessage() {
        assertThrowsWithMessage("update 1 /field description",
                "The 'update' command requires a task index, field name, and new content!");
    }

    @Test
    void parseCommand_updateInvalidIndex_throwsWithMessage() {
        assertThrowsWithMessage("update one /field description /with new title",
                "The 'update' command's index argument should be an integer!");
    }

    @Test
    void parseCommand_updateInvalidDate_throwsWithMessage() {
        assertThrowsWithMessage("update 1 /field by /with 2024/02/18 10:00",
                "Invalid date/time format! Please use 'yyyy-MM-dd HH:mm'.");
    }

    @Test
    void parseCommand_extraWhitespace_returnsMarkCommand() {
        Command command = parser.parseCommand("   mark    5   ");
        assertInstanceOf(MarkCommand.class, command);
        assertPrivateFieldEquals(command, "index", 5);
    }

    @Test
    void parseCommand_mixedCase_returnsCorrectCommand() {
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
