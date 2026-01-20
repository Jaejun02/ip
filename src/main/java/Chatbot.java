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
            if (userInput.equalsIgnoreCase("bye")) {
                ui.bidFarewell();
                break;
            } else if (userInput.equalsIgnoreCase("list")) {
                String[] replies = IntStream.range(0, this.userInputs.size())
                                           .mapToObj(i -> (i + 1) + ". " + this.userInputs.get(i))
                                           .toArray(String[]::new);
                ui.replyUser(replies);
            } else {
                this.userInputs.add(userInput);
                ui.replyUser("added: " + userInput);
            }
        }
        scanner.close();
    }
}
