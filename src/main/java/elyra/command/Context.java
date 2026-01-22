package elyra.command;

import elyra.ui.Ui;
import elyra.task.TaskList;

public record Context(Ui ui, TaskList tasks) {};