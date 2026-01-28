package elyra.task;

import java.util.Locale;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    public Event(String description, LocalDateTime startAt, LocalDateTime endAt) {
        super(description);
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public Event(String description, boolean isDone, LocalDateTime startAt, LocalDateTime endAt) {
        super(description, isDone);
        this.startAt = startAt;
        this.endAt = endAt;
    }

    @Override
    public String[] getInfos(DateTimeFormatter timeFormatter) {
        String[] baseInfo = super.getInfos(timeFormatter);
        return new String[] {"E", baseInfo[0], baseInfo[1],
                this.startAt.format(timeFormatter), this.endAt.format(timeFormatter)};
    }

    @Override
    public String toUiString(DateTimeFormatter timeFormatter) {
        return "[E]" + super.toUiString(timeFormatter)
                + " (from: " + this.startAt.format(timeFormatter) + " to: "
                + this.endAt.format(timeFormatter) + ")";
    }
}
