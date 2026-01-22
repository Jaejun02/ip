public class AddCommand implements Command {
    private final String description;

    public AddCommand(String description) {
        this.description = description;
    }

    @Override
    public ExecutionResult execute(Context context) {
        Task newTask = new Task(this.description);
        context.tasks().addTask(newTask);
        context.ui().confirmAddition(newTask);
        return ExecutionResult.CONTINUE;
    }
}
