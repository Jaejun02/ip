package elyra.command;

public class ListCommand implements Command {
    @Override
    public ExecutionResult execute(Context context) {
        context.ui().showUserInputList(context.tasks().getTasks());
        return ExecutionResult.CONTINUE;
    }
}
