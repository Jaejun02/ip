package elyra.command;

import elyra.task.ToDo;

/**
 * Represents a command to add a todo task.
 */
public class AddTodoCommand implements Command{
    private final String description;

    /**
     * Creates a new AddTodoCommand with the specified description.
     *
     * @param description Description of the todo task.
     */
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
