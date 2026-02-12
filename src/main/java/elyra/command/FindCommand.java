package elyra.command;

import java.util.ArrayList;

import elyra.task.Task;

/**
 * Represents a command to find a task with a keyword.
 */
public class FindCommand implements Command {
    private final String keyword;

    /**
     * Creates a new FindCommand with the specified keyword.
     *
     * @param keyword Keyword the description of all returned tasks should contain.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * @inheritDoc
     */
    @Override
    public ExecutionResult execute(Context context) {
        ArrayList<Task> matchingTasks = context.tasks()
                .findTasksByKeyword(this.keyword);
        String response = context.ui().showMatchingTaskList(matchingTasks, this.keyword);
        return new ExecutionResult(false, false, response);
    }
}
