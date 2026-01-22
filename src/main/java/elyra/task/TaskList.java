package elyra.task;

import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
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

    public void markTask(int index) {
        Task currentTask = getTask(index); // To check validity of index
        currentTask.setDone(true);
    }

    public void unmarkTask(int index) {
        Task currentTask = getTask(index); // To check validity of index
        currentTask.setDone(false);
    }
}
