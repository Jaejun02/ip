package elyra.task;

import java.time.format.DateTimeFormatter;

public class Task {
    private final String description;
    private boolean isDone;

    public Task(String description) {
        this(description, false);
    }

    public Task(String description, boolean isDone) {
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
}
