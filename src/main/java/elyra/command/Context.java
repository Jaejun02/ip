package elyra.command;

import elyra.task.TaskList;
import elyra.ui.Ui;

/**
 * Represents the execution context for commands.
 * A Context object contains the UI and task list needed for command execution.
 */
public record Context(Ui ui, TaskList tasks) {};
