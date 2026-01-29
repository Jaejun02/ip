package elyra.task;

import java.time.format.DateTimeFormatter;

public class Task {
    private final String description;
    private boolean isDone;

    public Task(String description) {
        this(description, false);
    }

    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    public String[] getInfos(DateTimeFormatter timeFormatter) {
        return new String[] {(isDone ? "1" : "0"), this.description};
    }

    public String toUiString(DateTimeFormatter timeFormatter) {
        return (isDone ? "[X] " : "[ ] ") + description;
    }
}
