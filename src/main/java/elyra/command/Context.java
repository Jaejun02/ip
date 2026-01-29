package elyra.command;

import elyra.task.TaskList;
import elyra.ui.Ui;

public record Context(Ui ui, TaskList tasks) {};
