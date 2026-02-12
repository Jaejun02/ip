package elyra;

import java.io.IOException;

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
    private boolean haveLoadError = false;
    private String loadDataErrorMessage = "";

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
            this.haveLoadError = true;
            this.loadDataErrorMessage = ui.showLoadDataErrorMessage(err.getMessage());
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
     * Returns a greeting message from Elyra.
     */
    public String getGreeting() {
        return ui.greetUser(this.name);
    }

    public boolean haveLoadError() {
        return this.haveLoadError;
    }

    public String getLoadDataErrorMessage() {
        return this.loadDataErrorMessage;
    }

    /**
     * Processes the user input and returns the corresponding response.
     *
     * @param userInput The input string from the user.
     * @return An ExecutionResult containing the response and execution status.
     */
    public ExecutionResult getResponse(String userInput) {
        ExecutionResult result;
        try {
            Command currentCommand = parser.parseCommand(userInput);
            assert currentCommand != null : "Parser returned null Command";

            Context currentContext = new Context(this.ui, this.tasks);
            result = currentCommand.execute(currentContext);
            assert result != null : "Command.execute returned null ExecutionResult";

            if (result.isSave()) {
                storage.saveTasks(this.tasks);
            }
        } catch (IllegalArgumentException | IndexOutOfBoundsException err) {
            String response = ui.showUserInputErrorMessage(err.getMessage(), userInput);
            result = new ExecutionResult(false, false, response);
        } catch (IOException err) {
            String response = ui.showSaveDataErrorMessage(err.getMessage());
            result = new ExecutionResult(false, false, response);
        }
        return result;
    }
}
