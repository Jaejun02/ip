package elyra.task;

public class ToDo extends Task {
    public ToDo(String description) {
        super(description);
    }

    public ToDo(String description, boolean isDone) {
        super(description, isDone);
    }

    @Override
    public String[] getInfos() {
        String[] baseInfo = super.getInfos();
        return new String[] {"T", baseInfo[0], baseInfo[1]};
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
