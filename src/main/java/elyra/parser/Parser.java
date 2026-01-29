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
            default -> {
                String message = "I'm sorry, but I don't recognize the command provided!";
                throw new IllegalArgumentException(message);
            }
        };

    }

    private Command parseByeCommand(String[] inputTokens) {
        if (inputTokens.length > 1) {
            String message = "The 'bye' command does not take any arguments!";
            throw new IllegalArgumentException(message);
        } else {
            return new ByeCommand();
        }
    }

    private Command parseListCommand(String[] inputTokens) {
        if (inputTokens.length > 1) {
            String message = "The 'list' command does not take any arguments!";
            throw new IllegalArgumentException(message);
        } else {
            return new ListCommand();
        }
    }

    private Command parseMarkCommand(String[] inputTokens) {
        if (inputTokens.length != 2) {
            String message = "The 'mark' command requires exactly one argument!";
            throw new IllegalArgumentException(message);
        } else {
            try {
                int index = Integer.parseInt(inputTokens[1]);
                return new MarkCommand(index);
            } catch (NumberFormatException e) {
                String message = "The 'mark' command requires a numeric argument!";
                throw new IllegalArgumentException(message);
            }
        }
    }

    private Command parseUnmarkCommand(String[] inputTokens) {
        if (inputTokens.length != 2) {
            String message = "The 'unmark' command requires exactly one argument!";
            throw new IllegalArgumentException(message);
        } else {
            try {
                int index = Integer.parseInt(inputTokens[1]);
                return new UnmarkCommand(index);
            } catch (NumberFormatException e) {
                String message = "The 'unmark' command requires a numeric argument!";
                throw new IllegalArgumentException(message);
            }
        }
    }

    private Command parseTodoCommand(String[] inputTokens) {
        if (inputTokens.length < 2) {
            String message = "The 'todo' command requires a description!";
            throw new IllegalArgumentException(message);
        } else {
            String description = String.join(" ", Arrays.copyOfRange(inputTokens, 1, inputTokens.length));
            return new AddTodoCommand(description);
        }
    }

    private Command parseDeadlineCommand(String[] inputTokens) {
        String argument = String.join(" ", Arrays.copyOfRange(inputTokens, 1, inputTokens.length));
        String[] parts = argument.split("/by");
        if (parts.length != 2) {
            String message = "The 'deadline' command requires a description and a due date!";
            throw new IllegalArgumentException(message);
        } else {
            String description = parts[0].trim();
            LocalDateTime by = parseDateTime(parts[1].trim());
            if (description.isEmpty()) {
                String message = "The 'deadline' command requires a non-empty description!";
                throw new IllegalArgumentException(message);
            }
            return new AddDeadlineCommand(description, by);
        }
    }

    private Command parseEventCommand(String[] inputTokens) {
        String argument = String.join(" ", Arrays.copyOfRange(inputTokens, 1, inputTokens.length));
        String[] parts = argument.split("\\s*/from\\s*|\\s*/to\\s*");
        if (parts.length != 3) {
            String message = "The 'event' command requires a description, start time, and end time!";
            throw new IllegalArgumentException(message);
        } else {
            String description = parts[0].trim();
            LocalDateTime from = parseDateTime(parts[1].trim());
            LocalDateTime to = parseDateTime(parts[2].trim());
            if (description.isEmpty()) {
                String message = "The 'event' command requires a non-empty description!";
                throw new IllegalArgumentException(message);
            }
            return new AddEventCommand(description, from, to);
        }
    }

    private Command parseDeleteCommand(String[] inputTokens) {
        if (inputTokens.length != 2) {
            String message = "The 'delete' command requires exactly one argument!";
            throw new IllegalArgumentException(message);
        } else {
            try {
                int index = Integer.parseInt(inputTokens[1]);
                return new DeleteCommand(index);
            } catch (NumberFormatException e) {
                String message = "The 'delete' command requires a numeric argument!";
                throw new IllegalArgumentException(message);
            }
        }
    }

    private Command parseFindCommand(String[] inputTokens) {
        if (inputTokens.length < 2) {
            String message = "The 'find' command requires a keyword (or phrase) to search for!";
            throw new IllegalArgumentException(message);
        } else {
            String keyword = String.join(" ", Arrays.copyOfRange(inputTokens, 1, inputTokens.length));
            return new FindCommand(keyword);
        }
    }

    private LocalDateTime parseDateTime(String dateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);
        try {
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (DateTimeParseException e) {
            String message = "Invalid date/time format! Please use 'yyyy-MM-dd HH:mm'.";
            throw new IllegalArgumentException(message);
        }
    }

    private void invalidateDelimiter(String userInput) {
        if (userInput.contains(Storage.DELIM.strip())) {
            String message = "'" + Storage.DELIM.strip()
                + "' is reserved and cannot be used in task descriptions!";
            throw new IllegalArgumentException(message);
        }
    }
}
