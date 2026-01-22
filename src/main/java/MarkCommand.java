public class MarkCommand implements Command {
    private final int index;

    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    public ExecutionResult execute(Context context) {
        context.tasks().markTask(index);
        Task markedTask = context.tasks().getTask(index);
        context.ui().confirmMark(markedTask);
        return ExecutionResult.CONTINUE;
    }

}
