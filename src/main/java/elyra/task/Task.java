package elyra.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a generic task.
 * A Task object corresponds to a task with a description and completion status.
 */
public abstract class Task {
    private String description;
    private boolean isDone;

    /**
     * Creates a new Task with the specified description.
     * The task is initially marked as not done.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this(description, false);
    }

    /**
     * Creates a new Task with the specified description and completion status.
     *
     * @param description Description of the task.
     * @param isDone Whether the task is completed.
     */
    public Task(String description, boolean isDone) {
        assert description != null : "Description cannot be null";
        assert !description.isEmpty() : "Description cannot be empty";
        this.description = description;
        this.isDone = isDone;
    }

    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the completion status of the task.
     *
     * @param isDone Whether the task is completed.
     */
    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Returns task information as an array of strings for storage serialization.
     *
     * @param timeFormatter Formatter for date and time fields.
     * @return Array of strings containing task information.
     */
    public String[] getInfos(DateTimeFormatter timeFormatter) {
        return new String[] {(isDone ? "1" : "0"), this.description};
    }

    /**
     * Returns a formatted string representation of the task for UI display.
     *
     * @param timeFormatter Formatter for date and time fields.
     * @return Formatted string for UI display.
     */
    public String toUiString(DateTimeFormatter timeFormatter) {
        return (isDone ? "[X] " : "[ ] ") + description;
    }

    public void updateField(String fieldName,
                            String newTextContent,
                            LocalDateTime newDateTimeContent) throws UnsupportedOperationException {
        if (newTextContent != null) {
            updateField(fieldName, newTextContent);
        } else if (newDateTimeContent != null) {
            updateField(fieldName, newDateTimeContent);
        } else {
            String message = "Non-reachable state: at least one of newTextContent and " +
                    "newDateTimeContent should be non-null.";
            throw new AssertionError(message);
        }
    }

    protected void updateField(String fieldName,
                            String newTextContent) throws UnsupportedOperationException {
        if (!fieldName.equalsIgnoreCase("description")) {
            throw new UnsupportedOperationException("Only the description field can be updated with text content.");
        }
        this.description = newTextContent;
    };

    protected void updateField(String fieldName,
                            LocalDateTime newDateTimeContent) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This task does not have updatable datetime fields.");
    }
}
