package elyra.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Arrays;

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

/**
 * Represents a parser for user input commands.
 * A Parser object converts user input strings into executable Command objects.
 */
public class Parser {
    /**
     * Parses user input into a Command object.
     *
     * @param userInput User input string to parse.
     * @return Command object corresponding to the user input.
     * @throws IllegalArgumentException If the command is unknown, invalid, or has incorrect arguments.
     */
    public Command parseCommand(String userInput) {
        if (userInput == null || userInput.trim().isEmpty()) {
            String message = "Please enter a command. Try: list";
            throw new IllegalArgumentException(message);
        }
        invalidateDelimiter(userInput);

        String[] inputTokens = userInput.trim().split("\\s+");
        String commandWord = inputTokens[0].toLowerCase();

        return switch (commandWord) {
            case "bye" -> parseByeCommand(inputTokens);
            case "list" -> parseListCommand(inputTokens);
            case "mark" -> parseMarkCommand(inputTokens);
            case "unmark" -> parseUnmarkCommand(inputTokens);
            case "todo" -> parseTodoCommand(inputTokens);
            case "deadline" -> parseDeadlineCommand(inputTokens);
            case "event" -> parseEventCommand(inputTokens);
            case "delete" -> parseDeleteCommand(inputTokens);
            case "find" -> parseFindCommand(inputTokens);
            case "update" -> parseUpdateCommand(inputTokens);
            default -> {
                String message = "I don't recognize that command. Try: list, todo, deadline, event, "
                        + "mark, unmark, delete, find, update, bye.";
                throw new IllegalArgumentException(message);
            }
        };

    }

    private Command parseByeCommand(String[] inputTokens) {
        if (inputTokens.length > 1) {
            String message = "The 'bye' command does not take any arguments. Usage: bye";
            throw new IllegalArgumentException(message);
        }
        return new ByeCommand();
    }

    private Command parseListCommand(String[] inputTokens) {
        if (inputTokens.length > 1) {
            String message = "The 'list' command does not take any arguments. Usage: list";
            throw new IllegalArgumentException(message);
        }
        return new ListCommand();
    }

    private Command parseMarkCommand(String[] inputTokens) {
        if (inputTokens.length != 2) {
            String message = "The 'mark' command requires exactly one argument. Usage: mark <index>";
            throw new IllegalArgumentException(message);
        }
        try {
            int index = Integer.parseInt(inputTokens[1]);
            return new MarkCommand(index);
        } catch (NumberFormatException e) {
            String message = "Index must be a number. Example: mark 2";
            throw new IllegalArgumentException(message);
        }
    }

    private Command parseUnmarkCommand(String[] inputTokens) {
        if (inputTokens.length != 2) {
            String message = "The 'unmark' command requires exactly one argument. Usage: unmark <index>";
            throw new IllegalArgumentException(message);
        }
        try {
            int index = Integer.parseInt(inputTokens[1]);
            return new UnmarkCommand(index);
        } catch (NumberFormatException e) {
            String message = "Index must be a number. Example: unmark 2";
            throw new IllegalArgumentException(message);
        }
    }

    private Command parseTodoCommand(String[] inputTokens) {
        if (inputTokens.length < 2) {
            String message = "The 'todo' command needs a description. Usage: todo <description>";
            throw new IllegalArgumentException(message);
        }
        String description = String.join(" ", Arrays.copyOfRange(inputTokens, 1, inputTokens.length));
        return new AddTodoCommand(description);
    }

    private Command parseDeadlineCommand(String[] inputTokens) {
        String argument = String.join(" ", Arrays.copyOfRange(inputTokens, 1, inputTokens.length));
        String[] parts = argument.split("/by");
        if (parts.length != 2) {
            String message = "The 'deadline' command requires a description and a due date. "
                    + "Usage: deadline <description> /by yyyy-MM-dd HH:mm";
            throw new IllegalArgumentException(message);
        }
        String description = parts[0].trim();
        LocalDateTime by = parseDateTime(parts[1].trim());
        if (description.isEmpty()) {
            String message = "The 'deadline' description cannot be empty. Usage: "
                    + "deadline <description> /by yyyy-MM-dd HH:mm";
            throw new IllegalArgumentException(message);
        }
        return new AddDeadlineCommand(description, by);
    }

    private Command parseEventCommand(String[] inputTokens) {
        String argument = String.join(" ", Arrays.copyOfRange(inputTokens, 1, inputTokens.length));
        // Delimiter pattern for splitting an event command into 3 parts: description, /from datetime, /to datetime.
        String eventDelimiterPattern = "\\s*/from\\s*|\\s*/to\\s*";
        String[] parts = argument.split(eventDelimiterPattern);
        if (parts.length != 3) {
            String message = "The 'event' command requires a description, start time, and end time. "
                    + "Usage: event <description> /from yyyy-MM-dd HH:mm /to yyyy-MM-dd HH:mm";
            throw new IllegalArgumentException(message);
        }
        String description = parts[0].trim();
        LocalDateTime from = parseDateTime(parts[1].trim());
        LocalDateTime to = parseDateTime(parts[2].trim());
        if (description.isEmpty()) {
            String message = "The 'event' description cannot be empty. Usage: "
                    + "event <description> /from yyyy-MM-dd HH:mm /to yyyy-MM-dd HH:mm";
            throw new IllegalArgumentException(message);
        }
        return new AddEventCommand(description, from, to);
    }

    private Command parseDeleteCommand(String[] inputTokens) {
        if (inputTokens.length != 2) {
            String message = "The 'delete' command requires exactly one argument. Usage: delete <index>";
            throw new IllegalArgumentException(message);
        }
        try {
            int index = Integer.parseInt(inputTokens[1]);
            return new DeleteCommand(index);
        } catch (NumberFormatException e) {
            String message = "Index must be a number. Example: delete 3";
            throw new IllegalArgumentException(message);
        }
    }

    private Command parseFindCommand(String[] inputTokens) {
        if (inputTokens.length < 2) {
            String message = "The 'find' command needs a keyword. Usage: find <keyword>";
            throw new IllegalArgumentException(message);
        }
        String keyword = String.join(" ", Arrays.copyOfRange(inputTokens, 1, inputTokens.length));
        return new FindCommand(keyword);
    }

    private Command parseUpdateCommand(String[] inputTokens) {
        String argument = String.join(" ", Arrays.copyOfRange(inputTokens, 1, inputTokens.length));
        // Delimiter pattern for splitting an update command into 3 parts: index, /field fieldName, /with newContent.
        String updateDelimiterPattern = "\\s*/field\\s*|\\s*/with\\s*";
        String[] parts = argument.split(updateDelimiterPattern, -1);
        if (parts.length != 3) {
            String message = "The 'update' command requires a task index, field, and new value. "
                    + "Usage: update <index> /field <field> /with <value>";
            throw new IllegalArgumentException(message);
        }
        try {
            String indexText = parts[0].trim();
            if (indexText.isEmpty()) {
                String message = "The 'update' command requires a task index. "
                        + "Usage: update <index> /field <field> /with <value>";
                throw new IllegalArgumentException(message);
            }
            String fieldName = parts[1].trim();
            if (fieldName.isEmpty()) {
                String message = "The 'update' command requires a field name. "
                        + "Usage: update <index> /field <field> /with <value>";
                throw new IllegalArgumentException(message);
            }
            String newContent = parts[2].trim();
            if (newContent.isEmpty()) {
                String message = "The 'update' command requires a new value. "
                        + "Usage: update <index> /field <field> /with <value>";
                throw new IllegalArgumentException(message);
            }
            int index = Integer.parseInt(indexText);
            boolean isBy = fieldName.equalsIgnoreCase("by");
            boolean isFrom = fieldName.equalsIgnoreCase("from");
            boolean isTo = fieldName.equalsIgnoreCase("to");
            boolean isDateTimeField = isBy || isFrom || isTo;
            if (isDateTimeField) {
                LocalDateTime newDateTimeContent = parseDateTime(newContent);
                return new UpdateCommand(index, fieldName, newDateTimeContent);
            }
            return new UpdateCommand(index, fieldName, newContent);
        } catch (NumberFormatException e) {
            String message = "Index must be a number. Example: update 2 /field description /with new title";
            throw new IllegalArgumentException(message);
        }
    }

    private LocalDateTime parseDateTime(String dateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);
        try {
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (DateTimeParseException e) {
            String message = "Invalid date/time format. Use yyyy-MM-dd HH:mm (e.g. 2024-02-01 12:30).";
            throw new IllegalArgumentException(message);
        }
    }

    private void invalidateDelimiter(String userInput) {
        if (userInput.contains(Storage.DELIM.strip())) {
            String message = "The text '" + Storage.DELIM.strip()
                    + "' is reserved for saving tasks. Please remove it from your description.";
            throw new IllegalArgumentException(message);
        }
    }
}
