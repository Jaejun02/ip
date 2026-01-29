package elyra;

import java.io.IOException;
import java.util.Scanner;

import elyra.command.Command;
import elyra.command.Context;
import elyra.command.ExecutionResult;
import elyra.parser.Parser;
import elyra.storage.Storage;
import elyra.task.TaskList;
import elyra.ui.Ui;

/**
 * Represents the main chatbot application.
 * An Elyra object handles user interactions and manages tasks through a command-based interface.
 */
public class Elyra {
    private final String name = "Elyra";
    private final Ui ui = new Ui();
    private final Parser parser = new Parser();
    private final TaskList tasks;
    private final Storage storage;

    /**
     * Creates a new Elyra chatbot instance with the specified file path for data storage.
     *
     * @param filePath Path to the file where tasks will be stored and loaded from.
     */
    public Elyra(String filePath) {
        this.storage = new Storage(filePath);
        TaskList loadedTasks;
        try {
            loadedTasks = storage.loadTasks();
        } catch (IOException err) {
            ui.showLoadDataErrorMessage(err.getMessage());
            loadedTasks = new TaskList();
        }
        this.tasks = loadedTasks;
    }

    /**
     * Creates a new Elyra chatbot instance with the default file path for data storage.
     */
    public Elyra() {
        this(Storage.DEFAULT_PATH);
    }

    /**
     * Runs the main chatbot loop, processing user input until exit command is received.
     */
    public void run() {
        ui.greetUser(this.name);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String userInput = scanner.nextLine().trim();
            try {
                Command currentCommand = parser.parseCommand(userInput);
                Context currentContext = new Context(this.ui, this.tasks);
                ExecutionResult result = currentCommand.execute(currentContext);
                storage.saveTasks(this.tasks);
                if (result == ExecutionResult.EXIT) {
                    break;
                }
            } catch (IllegalArgumentException | IndexOutOfBoundsException err) {
                ui.showUserInputErrorMessage(err.getMessage(), userInput);
            } catch (IOException err) {
                ui.showSaveDataErrorMessage(err.getMessage());
            }

        }
        scanner.close();
    }

    public static void main(String[] args) {
        new Elyra().run();
    }
}
