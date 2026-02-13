package elyra.command;

import elyra.task.TaskList;
import elyra.ui.Ui;

/**
 * Represents the execution context for commands.
 * A Context object contains the UI and task list needed for command execution.
 */
public record Context(Ui ui, TaskList tasks) {
    /**
     * Constructs a new Context with the specified UI and task list.
     *
     * @param ui    The UI to use for command execution.
     * @param tasks The task list to use for command execution.
     */
    public Context {
        assert ui != null : "Context.ui cannot be null";
        assert tasks != null : "Context.tasks cannot be null";
    }
};
