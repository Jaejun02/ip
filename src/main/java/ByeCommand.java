public class ByeCommand implements Command {
    @Override
    public ExecutionResult execute(Context context) {
        context.ui().bidFarewell();
        return ExecutionResult.EXIT;
    }

}
