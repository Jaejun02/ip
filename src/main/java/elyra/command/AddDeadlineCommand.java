package elyra.command;

import java.time.LocalDateTime;

import elyra.task.Deadline;

public class AddDeadlineCommand implements Command{
    private final String description;
    private final LocalDateTime by;

    public AddDeadlineCommand(String description, LocalDateTime by) {
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
