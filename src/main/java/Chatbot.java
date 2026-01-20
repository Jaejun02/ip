import java.util.Scanner;
import java.util.ArrayList;
import java.util.stream.IntStream;

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
        String[] replies = IntStream.range(0, this.userInputs.size())
                                   .mapToObj(i -> (i + 1) + ". " + this.userInputs.get(i))
                                   .toArray(String[]::new);
        ui.replyUser(replies);
        return ExecutionResult.CONTINUE;
    }

    private ExecutionResult executeAdd(String userInput) {
        this.userInputs.add(userInput);
        ui.replyUser("added: " + userInput);
        return ExecutionResult.CONTINUE;
    }
}
