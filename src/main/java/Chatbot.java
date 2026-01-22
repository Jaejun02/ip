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
            Context currentContext = new Context(this.ui, this.tasks);
            ExecutionResult result = currentCommand.execute(currentContext);
            if (result == ExecutionResult.EXIT) {
                break;
            }
        }
        scanner.close();
    }
}