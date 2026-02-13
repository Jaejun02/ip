package elyra.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task.
 * A Deadline object corresponds to a task with a due date and time.
 */
public class Deadline extends Task {
    private LocalDateTime by;

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
        assert by != null : "Deadline.by cannot be null.";
        this.by = by;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String[] getInfos(DateTimeFormatter timeFormatter) {
        String[] baseInfo = super.getInfos(timeFormatter);
        return new String[] {TaskType.DEADLINE.getStorageCode(), baseInfo[0],
                baseInfo[1], this.by.format(timeFormatter)};
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toUiString(DateTimeFormatter timeFormatter) {
        return TaskType.DEADLINE.getUiTag() + super.toUiString(timeFormatter)
                + " (by: " + this.by.format(timeFormatter) + ")";
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void updateDateTimeField(String fieldName,
                            LocalDateTime newDateTimeContent) throws UnsupportedOperationException {
        if (!fieldName.equalsIgnoreCase("by")) {
            throw new UnsupportedOperationException(
                    "Cannot update field '" + fieldName + "' for Deadline task. "
                            + "Only 'by' field can be updated with date and time content.");
        }
        by = newDateTimeContent;
    }
}
