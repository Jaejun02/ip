public class AddTodoCommand implements Command{
    private final String description;

    public AddTodoCommand(String description) {
        this.description = description;
    }

    @Override
    public ExecutionResult execute(Context context) {
        Task newTask = new ToDo(this.description);
        context.tasks().addTask(newTask);
        context.ui().confirmAddition(newTask);
        return ExecutionResult.CONTINUE;
    }
}
