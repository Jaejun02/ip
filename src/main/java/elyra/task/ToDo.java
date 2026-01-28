package elyra.task;

import java.time.format.DateTimeFormatter;

public class ToDo extends Task {
    public ToDo(String description) {
        super(description);
    }

    public ToDo(String description, boolean isDone) {
        super(description, isDone);
    }

    @Override
    public String[] getInfos(DateTimeFormatter timeFormatter) {
        String[] baseInfo = super.getInfos(timeFormatter);
        return new String[] {"T", baseInfo[0], baseInfo[1]};
    }

    @Override
    public String toUiString(DateTimeFormatter timeFormatter) {
        return "[T]" + super.toUiString(timeFormatter);
    }
}
