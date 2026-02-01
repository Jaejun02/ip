package elyra.command;

import java.time.LocalDateTime;

import elyra.task.Deadline;

/**
 * Represents a command to add a deadline task.
 */
public class AddDeadlineCommand implements Command {
    private final String description;
    private final LocalDateTime by;

    /**
     * Creates a new AddDeadlineCommand with the specified description and deadline.
     *
     * @param description Description of the deadline task.
     * @param by Due date and time of the deadline.
     */
    public AddDeadlineCommand(String description, LocalDateTime by) {
        this.description = description;
        this.by = by;
    }

    @Override
    public ExecutionResult execute(Context context) {
        Deadline newTask = new Deadline(this.description, this.by);
        context.tasks().addTask(newTask);
        context.ui().confirmAddition(newTask, context.tasks());
        return new ExecutionResult(false, true);
    }
}
