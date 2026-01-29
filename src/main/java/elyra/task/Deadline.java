package elyra.task;

import java.util.Locale;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task.
 * A Deadline object corresponds to a task with a due date and time.
 */
public class Deadline extends Task {
    private final LocalDateTime by;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(
            "MMM dd uuuu, h:mm a", Locale.ENGLISH);

    /**
     * Creates a new Deadline task with the specified description and due date.
     *
     * @param description Description of the deadline task.
     * @param by Due date and time of the deadline.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Creates a new Deadline task with the specified description, completion status, and due date.
     *
     * @param description Description of the deadline task.
     * @param isDone Whether the task is completed.
     * @param by Due date and time of the deadline.
     */
    public Deadline(String description, boolean isDone, LocalDateTime by) {
        super(description, isDone);
        this.by = by;
    }

    @Override
    public String[] getInfos(DateTimeFormatter timeFormatter) {
        String[] baseInfo = super.getInfos(timeFormatter);
        return new String[] {"D", baseInfo[0], baseInfo[1], this.by.format(timeFormatter)};
    }

    @Override
    public String toUiString(DateTimeFormatter timeFormatter) {
        return "[D]" + super.toUiString(timeFormatter)
                + " (by: " + this.by.format(timeFormatter) + ")";
    }
}
