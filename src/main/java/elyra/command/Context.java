package elyra.command;

import elyra.ui.Ui;
import elyra.task.TaskList;

/**
 * Represents the execution context for commands.
 * A Context object contains the UI and task list needed for command execution.
 */
public record Context(Ui ui, TaskList tasks) {};