import java.util.Scanner;

public class Chatbot {
    private final String name;
    private final Ui ui = new Ui();
    private TaskList tasks = new TaskList();

    public Chatbot(String name) {
        this.name = name;
    }

    public void run() {
        ui.greetUser(this.name);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String userInput = scanner.nextLine().trim();
            ExecutionResult result = execute(userInput);
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

    private ExecutionResult execute(String userInput) {
        return switch (userInput.toLowerCase()) {
            case "bye" -> executeBye();
            case "list" -> executeList();
            default -> executeAdd(userInput);
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
}
