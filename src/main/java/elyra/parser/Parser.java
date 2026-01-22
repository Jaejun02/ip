package elyra.parser;

import java.util.Arrays;

import elyra.command.Command;
import elyra.command.ByeCommand;
import elyra.command.ListCommand;
import elyra.command.MarkCommand;
import elyra.command.UnmarkCommand;
import elyra.command.AddTodoCommand;
import elyra.command.AddDeadlineCommand;
import elyra.command.AddEventCommand;

import elyra.parser.exception.UnknownCommandException;

public class Parser {
    public Command parseCommand(String userInput) {
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
            default -> {
                throw new UnknownCommandException(userInput);
            }
        };

    }

    private Command parseByeCommand(String[] inputTokens) {
        if (inputTokens.length > 1) {
            String message = "The 'bye' command does not take any arguments!\nReceived: "
                    + String.join(" ", inputTokens);
            throw new IllegalArgumentException(message);
        } else {
            return new ByeCommand();
        }
    }

    private Command parseListCommand(String[] inputTokens) {
        if (inputTokens.length > 1) {
            String message = "The 'list' command does not take any arguments!\nReceived: "
                    + String.join(" ", inputTokens);
            throw new IllegalArgumentException(message);
        } else {
            return new ListCommand();
        }
    }

    private Command parseMarkCommand(String[] inputTokens) {
        if (inputTokens.length != 2) {
            String message = "The 'mark' command requires exactly one argument!\nReceived: "
                    + String.join(" ", inputTokens);
            throw new IllegalArgumentException(message);
        } else {
            try {
                int index = Integer.parseInt(inputTokens[1]);
                return new MarkCommand(index);
            } catch (NumberFormatException e) {
                String message = "The 'mark' command requires a numeric argument!\nReceived: "
                        + inputTokens[1];
                throw new IllegalArgumentException(message);
            }
        }
    }

    private Command parseUnmarkCommand(String[] inputTokens) {
        if (inputTokens.length != 2) {
            String message = "The 'unmark' command requires exactly one argument!\nReceived: "
                    + String.join(" ", inputTokens);
            throw new IllegalArgumentException(message);
        } else {
            try {
                int index = Integer.parseInt(inputTokens[1]);
                return new UnmarkCommand(index);
            } catch (NumberFormatException e) {
                String message = "The 'unmark' command requires a numeric argument!\nReceived: "
                        + inputTokens[1];
                throw new IllegalArgumentException(message);
            }
        }
    }

    private Command parseTodoCommand(String[] inputTokens) {
        if (inputTokens.length < 2) {
            String message = "The 'todo' command requires a description!\nReceived: "
                    + String.join(" ", inputTokens);
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
            String message = "The 'deadline' command requires a description and a due date!\nReceived: "
                    + argument;
            throw new IllegalArgumentException(message);
        } else {
            String description = parts[0].trim();
            String by = parts[1].trim();
            if (description.isEmpty() || by.isEmpty()) {
                String message = "The 'deadline' command requires a non-empty description and due date!\nReceived: "
                        + argument;
                throw new IllegalArgumentException(message);
            }
            return new AddDeadlineCommand(description, by);
        }
    }

    private Command parseEventCommand(String[] inputTokens) {
        String argument = String.join(" ", Arrays.copyOfRange(inputTokens, 1, inputTokens.length));
        String[] parts = argument.split("\\s*/from\\s*|\\s*/to\\s*");
        if (parts.length != 3) {
            String message = "The 'event' command requires a description, start time, and end time!\nReceived: "
                    + argument;
            throw new IllegalArgumentException(message);
        } else {
            String description = parts[0].trim();
            String from = parts[1].trim();
            String to = parts[2].trim();
            if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
                String message = "The 'event' command requires a non-empty description, start time, and end time!\nReceived: "
                        + argument;
                throw new IllegalArgumentException(message);
            }
            return new AddEventCommand(description, from, to);
        }
    }
}
