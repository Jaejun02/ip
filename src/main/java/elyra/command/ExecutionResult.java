package elyra.command;

/**
 * Represents the result of command execution.
 */
public record ExecutionResult(boolean isExit, boolean isSave, String response) {
    public ExecutionResult {
        assert response != null : "ExecutionResult.response cannot be null";
    }
};
