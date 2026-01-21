import java.util.Scanner;
import java.util.ArrayList;

public class Chatbot {
    private final String name;
    private final Ui ui = new Ui();
    private ArrayList<String> userInputs = new ArrayList<>();

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
        ui.showUserInputList(this.userInputs);
        return ExecutionResult.CONTINUE;
    }

    private ExecutionResult executeAdd(String userInput) {
        this.userInputs.add(userInput);
        ui.confirmAddition(userInput);
        return ExecutionResult.CONTINUE;
    }
}
