package elyra.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task.
 * An Event object corresponds to a task with a start date and time and an end date and time.
 */
public class Event extends Task {
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    /**
     * Creates a new Event task with the specified description, start time, and end time.
     *
     * @param description Description of the event task.
     * @param startAt Start date and time of the event.
     * @param endAt End date and time of the event.
     */
    public Event(String description, LocalDateTime startAt, LocalDateTime endAt) {
        super(description);
        this.startAt = startAt;
        this.endAt = endAt;
    }

    /**
     * Creates a new Event task with the specified description, completion status, start time, and end time.
     *
     * @param description Description of the event task.
     * @param isDone Whether the task is completed.
     * @param startAt Start date and time of the event.
     * @param endAt End date and time of the event.
     */
    public Event(String description, boolean isDone, LocalDateTime startAt, LocalDateTime endAt) {
        super(description, isDone);
        this.startAt = startAt;
        this.endAt = endAt;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String[] getInfos(DateTimeFormatter timeFormatter) {
        String[] baseInfo = super.getInfos(timeFormatter);
        return new String[] {TaskType.EVENT.getStorageCode(), baseInfo[0], baseInfo[1],
                this.startAt.format(timeFormatter), this.endAt.format(timeFormatter)};
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toUiString(DateTimeFormatter timeFormatter) {
        return TaskType.EVENT.getUiTag() + super.toUiString(timeFormatter)
                + " (from: " + this.startAt.format(timeFormatter) + " to: "
                + this.endAt.format(timeFormatter) + ")";
    }
}
