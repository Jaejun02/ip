import java.util.ArrayList;
import java.util.stream.IntStream;

public class Ui {
    private final String splitter = "-".repeat(60);
    private final String indentation = " ".repeat(4);

    private void formatPrint(String message) {
        System.out.println(this.indentation + this.splitter);
        System.out.println(this.indentation + message);
        System.out.println(this.indentation + this.splitter);
    }

    private void formatPrint(String[] messages) {
        System.out.println(this.indentation + this.splitter);
        for (String message : messages) {
            System.out.println(this.indentation + message);
        }
        System.out.println(this.indentation + this.splitter);
    }

    public void greetUser(String name) {
        String[] greeting = {"Hello! I am " + name + ", your personal chatbot.", "What can I do for you today?"};
        formatPrint(greeting);
    }

    public void bidFarewell() {
        String farewell = "Goodbye! Hope to see you again soon!";
        formatPrint(farewell);
    }

    public void showUserInputList(ArrayList<String> userInputs) {
        if (userInputs.isEmpty()) {
            formatPrint("Your input list is currently empty.");
            return;
        }

        String[] formattedUserInputs = IntStream.range(0, userInputs.size())
                .mapToObj(i -> (i + 1) + ". " + userInputs.get(i))
                .toArray(String[]::new);
        formatPrint(formattedUserInputs);

    }

    public void confirmAddition(String userInput) {
        String confirmation = "Added: " + userInput;
        formatPrint(confirmation);
    }
}
