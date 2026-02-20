package elyra.command;

import java.time.LocalDateTime;

import elyra.task.Event;

/**
 * Represents a command to add an event task.
 */
public class AddEventCommand implements Command {
    private final String description;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    /**
     * Creates a new AddEventCommand with the specified description, start time, and end time.
     *
     * @param description Description of the event task.
     * @param startAt Start date and time of the event.
     * @param endAt End date and time of the event.
     */
    public AddEventCommand(String description, LocalDateTime startAt, LocalDateTime endAt) {
        this.description = description;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    /**
     * @inheritDoc
     */
    @Override
    public ExecutionResult execute(Context context) {
        if (!this.startAt.isBefore(this.endAt)) {
            String message = "The 'event' command requires the start time to be before the end time. "
                    + "Usage: event <description> /from yyyy-MM-dd HH:mm /to yyyy-MM-dd HH:mm";
            throw new IllegalArgumentException(message);
        }
        Event newTask = new Event(this.description, this.startAt, this.endAt);
        context.tasks().addTask(newTask);
        String response = context.ui().confirmAddition(newTask, context.tasks());
        return new ExecutionResult(false, true, response);
    }
}
