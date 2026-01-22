package elyra.command;

import elyra.task.Task;

public class UnmarkCommand implements Command {
    private final int index;

    public UnmarkCommand(int index) {
        this.index = index;
    }

    @Override
    public ExecutionResult execute(Context context) {
        context.tasks().unmarkTask(index);
        Task unmarkedTask = context.tasks().getTask(index);
        context.ui().confirmUnmark(unmarkedTask);
        return ExecutionResult.CONTINUE;
    }

}
