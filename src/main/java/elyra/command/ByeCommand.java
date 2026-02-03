package elyra.command;

/**
 * Represents a command to exit the application.
 */
public class ByeCommand implements Command {
    @Override
    public ExecutionResult execute(Context context) {
        String response = context.ui().bidFarewell();
        return new ExecutionResult(true, false, response);
    }

}
