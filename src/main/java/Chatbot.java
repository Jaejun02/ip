import java.util.Scanner;

public class Chatbot {
    private final String name;
    private final Ui ui = new Ui();
    private final Parser parser = new Parser();
    private TaskList tasks = new TaskList();

    public Chatbot(String name) {
        this.name = name;
    }

    public void run() {
        ui.greetUser(this.name);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String userInput = scanner.nextLine().trim();
            Command currentCommand = parser.parseCommand(userInput);
            ExecutionResult result = execute(currentCommand);
            if (result == ExecutionResult.EXIT) {
                break;
            }
        }
        scanner.close();
    }

    public enum ExecutionResult {
        EXIT,
        CONTINUE
    }

    private ExecutionResult execute(Command command) {

        return switch (command.commandWord().toLowerCase()) {
            case "bye" -> executeBye();
            case "list" -> executeList();
            case "mark" -> executeMark(command.argument());
            case "unmark" -> executeUnmark(command.argument());
            default -> executeAdd(command.argument());
        };
    }

    private ExecutionResult executeBye() {
        ui.bidFarewell();
        return ExecutionResult.EXIT;
    }

    private ExecutionResult executeList() {
        ui.showUserInputList(this.tasks.getTasks());
        return ExecutionResult.CONTINUE;
    }

    private ExecutionResult executeAdd(String userInput) {
        Task newTask = new Task(userInput);
        this.tasks.addTask(newTask);
        ui.confirmAddition(newTask);
        return ExecutionResult.CONTINUE;
    }

    private ExecutionResult executeMark(String argument) {
        int index = Integer.parseInt(argument);
        this.tasks.markTask(index);
        Task markedTask = this.tasks.getTask(index);
        ui.confirmMark(markedTask);
        return ExecutionResult.CONTINUE;
    }

    private ExecutionResult executeUnmark(String argument) {
        int index = Integer.parseInt(argument);
        this.tasks.unmarkTask(index);
        Task unmarkedTask = this.tasks.getTask(index);
        ui.confirmUnmark(unmarkedTask);
        return ExecutionResult.CONTINUE;
    }
}
