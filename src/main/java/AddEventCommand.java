public class AddEventCommand implements Command {
    private final String description;
    private final String startAt;
    private final String endAt;

    public AddEventCommand(String description, String startAt, String endAt) {
        this.description = description;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    @Override
    public ExecutionResult execute(Context context) {
        Event newTask = new Event(this.description, this.startAt, this.endAt);
        context.tasks().addTask(newTask);
        context.ui().confirmAddition(newTask, context.tasks());
        return ExecutionResult.CONTINUE;
    }
}
