package elyra.command;

import elyra.task.Task;

/**
 * Represents a command to delete a task.
 */
public class DeleteCommand implements Command {
    private final int index;

    /**
     * Creates a new DeleteCommand with the specified task index.
     *
     * @param index Index of the task to delete (1-based).
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public ExecutionResult execute(Context context) {
        Task removedTask = context.tasks().deleteTask(this.index);
        String response = context.ui().confirmDeletion(removedTask, context.tasks());
        return new ExecutionResult(false, true, response);
    }
}
