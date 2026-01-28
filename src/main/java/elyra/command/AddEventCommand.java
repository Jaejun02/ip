package elyra.command;

import elyra.task.Event;

import java.time.LocalDateTime;

public class AddEventCommand implements Command {
    private final String description;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    public AddEventCommand(String description, LocalDateTime startAt, LocalDateTime endAt) {
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
