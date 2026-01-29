package elyra.command;

/**
 * Represents a command to exit the application.
 */
public class ByeCommand implements Command {
    @Override
    public ExecutionResult execute(Context context) {
        context.ui().bidFarewell();
        return ExecutionResult.EXIT;
    }

}
