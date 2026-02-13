package elyra.command;

import java.time.LocalDateTime;

import elyra.task.Task;

/**
 * Represents a command to update a task.
 */
public class UpdateCommand implements Command {
    private final int index;
    private final String fieldToUpdate;
    private final String newTextContent;
    private final LocalDateTime newDateTimeContent;

    /**
     * Creates an update command for text fields.
     *
     * @param index          The index of the task to update.
     * @param fieldToUpdate  The field of the task to update (e.g., "description").
     * @param newTextContent The new text content for the specified field.
     */
    public UpdateCommand(int index, String fieldToUpdate, String newTextContent) {
        this.index = index;
        this.fieldToUpdate = fieldToUpdate;
        this.newTextContent = newTextContent;
        this.newDateTimeContent = null;
    }

    /**
     * Creates an update command for text fields.
     *
     * @param index              The index of the task to update.
     * @param fieldToUpdate      The field of the task to update (e.g., "dueDate").
     * @param newDateTimeContent The new date/time content for the specified field.
     */
    public UpdateCommand(int index, String fieldToUpdate, LocalDateTime newDateTimeContent) {
        this.index = index;
        this.fieldToUpdate = fieldToUpdate;
        this.newDateTimeContent = newDateTimeContent;
        this.newTextContent = null;
    }

    /**
     * Executes the update command.
     * @param context Context containing UI and task list for command execution.
     * @return ExecutionResult containing the outcome of the command.
     */
    public ExecutionResult execute(Context context) {
        assert !(newTextContent == null && newDateTimeContent == null)
            : "Either newTextContent or newDateTimeContent should be non-null.";
        context.tasks().updateTask(index, fieldToUpdate, newTextContent, newDateTimeContent);
        Task updatedTask = context.tasks().getTask(index);
        String response = context.ui().confirmUpdate(updatedTask);
        return new ExecutionResult(false, true, response);
    }

}
