package elyra.task;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Represents a list of tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();

    /**
     * Adds a new task to the task list.
     *
     * @param task The task to add.
     */
    public void addTask(Task task) {
        assert task != null : "Attempted to add null task";
        this.tasks.add(task);
    }

    /**
     * Retrieves the task at the specified index.
     *
     * @param index Index of the task to retrieve (1-based).
     * @return The task at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public Task getTask(int index) {
        int size = this.tasks.size();
        if (index > 0 && index <= size) {
            return this.tasks.get(index - 1);
        }
        if (size == 0) {
            throw new IndexOutOfBoundsException("Your task list is empty. Add a task first.");
        }
        String message = "Task index " + index + " is out of range. Please enter a number between 1 and "
                + size + ".";
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

    /**
     * Finds and returns a list of tasks that contain the specified keyword in their description.
     *
     * @param keyword The keyword to search for.
     * @return An ArrayList of tasks that contain the keyword.
     */
    public ArrayList<Task> findTasksByKeyword(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : this.tasks) {
            if (task.getDescription().contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /**
     * Updates a specific field of the task at the specified index.
     *
     * @param index Index of the task to update (1-based).
     * @param fieldName The name of the field to update.
     * @param nextTextContent The new text content for the field (if applicable).
     * @param newDateTimeContent The new date-time content for the field (if applicable).
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public void updateTask(int index, String fieldName, String nextTextContent, LocalDateTime newDateTimeContent) {
        Task currentTask = getTask(index); // To check validity of index
        currentTask.updateField(fieldName, nextTextContent, newDateTimeContent);
    }
}
