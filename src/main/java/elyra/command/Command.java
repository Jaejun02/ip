package elyra.command;

/**
 * Represents a command that can be executed.
 */
public interface Command {
    /**
     * Executes the command with the given context.
     *
     * @param context Context containing UI and task list for command execution.
     * @return ExecutionResult containing the outcome of the command execution.
     */
    public ExecutionResult execute(Context context);
}
