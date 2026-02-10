package elyra.command;

import elyra.task.Task;

/**
 * Represents a command to mark a task as done.
 */
public class MarkCommand implements Command {
    private final int index;

    /**
     * Creates a new MarkCommand with the specified task index.
     *
     * @param index Index of the task to mark as done (1-based).
     */
    public MarkCommand(int index) {
        this.index = index;
    }

    /**
     * @inheritDoc
     */
    @Override
    public ExecutionResult execute(Context context) {
        context.tasks().markTask(index);
        Task markedTask = context.tasks().getTask(index);
        String response = context.ui().confirmMark(markedTask);
        return new ExecutionResult(false, true, response);
    }

}
