package elyra.task;

import java.time.format.DateTimeFormatter;

/**
 * Represents a todo task.
 * A ToDo object corresponds to a task without any date or time constraints.
 */
public class ToDo extends Task {
    /**
     * Creates a new ToDo task with the specified description.
     *
     * @param description Description of the todo task.
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Creates a new ToDo task with the specified description and completion status.
     *
     * @param description Description of the todo task.
     * @param isDone Whether the task is completed.
     */
    public ToDo(String description, boolean isDone) {
        super(description, isDone);
    }

    @Override
    public String[] getInfos(DateTimeFormatter timeFormatter) {
        String[] baseInfo = super.getInfos(timeFormatter);
        return new String[] {"T", baseInfo[0], baseInfo[1]};
    }

    @Override
    public String toUiString(DateTimeFormatter timeFormatter) {
        return "[T]" + super.toUiString(timeFormatter);
    }
}
