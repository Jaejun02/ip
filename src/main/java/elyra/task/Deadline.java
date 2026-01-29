package elyra.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Deadline extends Task {
    private final LocalDateTime by;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(
            "MMM dd uuuu, h:mm a", Locale.ENGLISH);

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    public Deadline(String description, boolean isDone, LocalDateTime by) {
        super(description, isDone);
        this.by = by;
    }

    @Override
    public String[] getInfos(DateTimeFormatter timeFormatter) {
        String[] baseInfo = super.getInfos(timeFormatter);
        return new String[] {"D", baseInfo[0], baseInfo[1], this.by.format(timeFormatter)};
    }

    @Override
    public String toUiString(DateTimeFormatter timeFormatter) {
        return "[D]" + super.toUiString(timeFormatter)
                + " (by: " + this.by.format(timeFormatter) + ")";
    }
}
