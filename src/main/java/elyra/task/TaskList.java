package elyra.task;

import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        assert task != null : "Attempted to add null task";
        this.tasks.add(task);
    }

    public Task getTask(int index) {
        if (index > 0 && index <= this.tasks.size()) {
            return this.tasks.get(index - 1);
        }
        String message = "We do not have a task numbered " + index + " in the list."
                + " The list currently has " + this.tasks.size() + " tasks.";
        throw new IndexOutOfBoundsException(message);
    }

    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Marks the task at the specified index as done.
     *
     * @param index Index of the task to mark (1-based).
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public void markTask(int index) {
        Task currentTask = getTask(index); // To check validity of index
        currentTask.setDone(true);
    }

    /**
     * Marks the task at the specified index as not done.
     *
     * @param index Index of the task to unmark (1-based).
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public void unmarkTask(int index) {
        Task currentTask = getTask(index); // To check validity of index
        currentTask.setDone(false);
    }

    /**
     * Deletes the task at the specified index and returns it.
     *
     * @param index Index of the task to delete (1-based).
     * @return The deleted task.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public Task deleteTask(int index) {
        Task removedTask = getTask(index); // To check validity of index
        this.tasks.remove(index - 1);
        return removedTask;
    }
    public ArrayList<Task> findTasksByKeyword(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : this.tasks) {
            if (task.getDescription().contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}
