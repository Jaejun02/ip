import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    public void markTaskAsDone(int index) {
        if (index > 0 && index <= this.tasks.size()) {
            this.tasks.get(index - 1).setDone(true);
        }
    }

    public void markTaskAsUndone(int index) {
        if (index > 0 && index <= this.tasks.size()) {
            this.tasks.get(index - 1).setDone(false);
        }
    }
}
