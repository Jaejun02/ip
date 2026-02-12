package elyra.command;

import java.time.LocalDateTime;

import elyra.task.Task;

public class UpdateCommand implements Command {
    private final int index;
    private final String fieldToUpdate;
    private final String newTextContent;
    private final LocalDateTime newDateTimeContent;

    public UpdateCommand(int index, String fieldToUpdate, String newTextContent) {
        this.index = index;
        this.fieldToUpdate = fieldToUpdate;
        this.newTextContent = newTextContent;
        this.newDateTimeContent = null;
    }

    public UpdateCommand(int index, String fieldToUpdate, LocalDateTime newDateTimeContent) {
        this.index = index;
        this.fieldToUpdate = fieldToUpdate;
        this.newDateTimeContent = newDateTimeContent;
        this.newTextContent = null;
    }

    public ExecutionResult execute(Context context) {
        assert !(newTextContent == null && newDateTimeContent == null)
            : "Either newTextContent or newDateTimeContent should be non-null.";
        context.tasks().updateTask(index, fieldToUpdate, newTextContent, newDateTimeContent);
        Task updatedTask = context.tasks().getTask(index);
        String response = context.ui().confirmUpdate(updatedTask);
        return new ExecutionResult(false, true, response);
    }

}
