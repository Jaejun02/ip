package elyra.task;

public class Event extends Task {
    private final String startAt;
    private final String endAt;

    public Event(String description, String startAt, String endAt) {
        super(description);
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public Event(String description, boolean isDone, String startAt, String endAt) {
        super(description, isDone);
        this.startAt = startAt;
        this.endAt = endAt;
    }

    @Override
    public String[] getInfos() {
        String[] baseInfo = super.getInfos();
        return new String[] {"E", baseInfo[0], baseInfo[1], this.startAt, this.endAt};
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.startAt + " to: " + this.endAt + ")";
    }
}
