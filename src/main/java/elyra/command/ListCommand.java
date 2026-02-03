package elyra.command;

/**
 * Represents a command to list all tasks.
 */
public class ListCommand implements Command {
    @Override
    public ExecutionResult execute(Context context) {
        String response = context.ui().showUserInputList(context.tasks().getTasks());
        return new ExecutionResult(false, false, response);
    }
}
