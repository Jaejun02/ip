package elyra.command;

import elyra.task.Task;

/**
 * Represents a command to mark a task as not done.
 */
public class UnmarkCommand implements Command {
    private final int index;

    /**
     * Creates a new UnmarkCommand with the specified task index.
     *
     * @param index Index of the task to mark as not done (1-based).
     */
    public UnmarkCommand(int index) {
        this.index = index;
    }

    @Override
    public ExecutionResult execute(Context context) {
        context.tasks().unmarkTask(index);
        Task unmarkedTask = context.tasks().getTask(index);
        context.ui().confirmUnmark(unmarkedTask);
        return new ExecutionResult(false, true);
    }

}
