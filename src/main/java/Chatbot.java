import java.util.Scanner;

public class Chatbot {
    private final String name;
    private final Ui ui = new Ui();

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
            } else {
                ui.replyUser(userInput);
            }
        }
        scanner.close();
    }
}
