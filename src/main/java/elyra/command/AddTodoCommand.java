package elyra.command;

import elyra.task.ToDo;

public class AddTodoCommand implements Command{
    private final String description;

    public AddTodoCommand(String description) {
        this.description = description;
    }

    @Override
    public ExecutionResult execute(Context context) {
        ToDo newTask = new ToDo(this.description);
        context.tasks().addTask(newTask);
        context.ui().confirmAddition(newTask, context.tasks());
        return ExecutionResult.CONTINUE;
    }
}
