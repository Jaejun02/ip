package elyra.command;

import java.util.ArrayList;

import elyra.task.Task;

public class FindCommand implements Command {
    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    public ExecutionResult execute(Context context) {
        ArrayList<Task> matchingTasks = context.tasks()
                .findTasksByKeyword(this.keyword);
        context.ui().showMatchingTaskList(matchingTasks, this.keyword);
        return ExecutionResult.CONTINUE;
    }
}
