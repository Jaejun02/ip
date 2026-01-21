public class Parser {
    public Command parseCommand(String userInput) {
        String[] inputTokens = userInput.trim().split("\\s+");
        String commandWord = inputTokens[0].toLowerCase();

        return switch (commandWord) {
            case "bye" -> parseByeCommand(inputTokens);
            case "list" -> parseListCommand(inputTokens);
            case "mark" -> parseMarkCommand(inputTokens);
            case "unmark" -> parseUnmarkCommand(inputTokens);
            default -> parseAddCommand(userInput);
        };

    }

    private Command parseByeCommand(String[] inputTokens) {
        if (inputTokens.length > 1) {
            String message = "The 'bye' command does not take any arguments!\nReceived: "
                    + String.join(" ", inputTokens);
            throw new IllegalArgumentException(message);
        } else {
            return new Command(inputTokens[0], "");
        }
    }

    private Command parseListCommand(String[] inputTokens) {
        if (inputTokens.length > 1) {
            String message = "The 'list' command does not take any arguments!\nReceived: "
                    + String.join(" ", inputTokens);
            throw new IllegalArgumentException(message);
        } else {
            return new Command(inputTokens[0], "");
        }
    }

    private Command parseAddCommand(String userInput) {
        return new Command("", userInput);
    }

    private Command parseMarkCommand(String[] inputTokens) {
        if (inputTokens.length != 2) {
            String message = "The 'mark' command requires exactly one argument!\nReceived: "
                    + String.join(" ", inputTokens);
            throw new IllegalArgumentException(message);
        } else {
            try {
                Integer.parseInt(inputTokens[1]);
                return new Command(inputTokens[0], inputTokens[1]);
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
                Integer.parseInt(inputTokens[1]);
                return new Command(inputTokens[0], inputTokens[1]);
            } catch (NumberFormatException e) {
                String message = "The 'unmark' command requires a numeric argument!\nReceived: "
                        + inputTokens[1];
                throw new IllegalArgumentException(message);
            }
        }
    }
}
