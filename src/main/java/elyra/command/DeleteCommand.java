package elyra.command;

import elyra.task.Task;

public class DeleteCommand implements Command {
    private final int index;

    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public ExecutionResult execute(Context context) {
        Task removedTask = context.tasks().deleteTask(this.index);
        context.ui().confirmDeletion(removedTask, context.tasks());
        return ExecutionResult.CONTINUE;
    }
}
