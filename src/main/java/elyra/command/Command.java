package elyra.command;

public interface Command {
    public ExecutionResult execute(Context context);
}