package elyra.command;

import elyra.Ui;
import elyra.task.TaskList;

public record Context(Ui ui, TaskList tasks) {};