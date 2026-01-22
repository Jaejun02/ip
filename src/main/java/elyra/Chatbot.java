package elyra;

import java.util.Scanner;

import elyra.parser.Parser;
import elyra.task.TaskList;
import elyra.command.Command;
import elyra.command.Context;
import elyra.command.ExecutionResult;
import elyra.ui.Ui;

public class Chatbot {
    private final String name;
    private final Ui ui = new Ui();
    private final Parser parser = new Parser();
    private final TaskList tasks = new TaskList();

    public Chatbot(String name) {
        this.name = name;
    }

    public void run() {
        ui.greetUser(this.name);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String userInput = scanner.nextLine().trim();
            try {
                Command currentCommand = parser.parseCommand(userInput);
                Context currentContext = new Context(this.ui, this.tasks);
                ExecutionResult result = currentCommand.execute(currentContext);
                if (result == ExecutionResult.EXIT) {
                    break;
                }
            } catch (IllegalArgumentException | IndexOutOfBoundsException err) {
                ui.showErrorMessage(err.getMessage(), userInput);
            }

        }
        scanner.close();
    }
}