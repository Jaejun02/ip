public class AddDeadlineCommand implements Command{
    private final String description;
    private final String by;

    public AddDeadlineCommand(String description, String by) {
        this.description = description;
        this.by = by;
    }

    @Override
    public ExecutionResult execute(Context context) {
        Deadline newTask = new Deadline(this.description, this.by);
        context.tasks().addTask(newTask);
        context.ui().confirmAddition(newTask, context.tasks());
        return ExecutionResult.CONTINUE;
    }
}
