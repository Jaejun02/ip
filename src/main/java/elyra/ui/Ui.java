package elyra.ui;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.IntStream;

import elyra.task.Task;
import elyra.task.TaskList;

public class Ui {
    private final String splitter = "-".repeat(60);
    private final String indentation = " ".repeat(4);
    private final String smallIndentation = " ".repeat(2);
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(
            "MMM dd uuuu, h:mm a", Locale.ENGLISH);

    private String[] formatTaskList(ArrayList<Task> tasks) {
        assert tasks != null : "formatTaskList should not receive null tasks list";
        assert tasks.stream().allMatch(Objects::nonNull) : "Task list contains null entries";
        return IntStream.range(0, tasks.size())
            .mapToObj(i -> (i + 1) + ". " + tasks.get(i).toUiString(this.timeFormatter))
            .toArray(String[]::new);
    }

    public String greetUser(String name) {
        String[] greeting = {"Hello! I am " + name + ", your personal chatbot.", "What can I do for you today?"};
        return String.join(System.lineSeparator(), greeting);
    }

    public String bidFarewell() {
        return "Goodbye! Hope to see you again soon!";
    }

    public String showUserInputList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            return "You currently don't have any tasks in your list.";
        }

        String header = "Here are your tasks:";
        String[] formattedTaskList = formatTaskList(tasks);
        return String.join(System.lineSeparator(),
                header,
                String.join(System.lineSeparator(), formattedTaskList));
    }

    public String showMatchingTaskList(ArrayList<Task> tasks, String keyword) {
        if (tasks.isEmpty()) {
            return "There are no tasks matching the keyword: '" + keyword + "'";
        }

        String header = "Here are the matching tasks:";
        String[] formattedTaskList = formatTaskList(tasks);
        return String.join(System.lineSeparator(),
                header,
                String.join(System.lineSeparator(), formattedTaskList));
    }

    public String confirmAddition(Task newTask, TaskList tasks) {
        String[] confirmation = {"Got it! I have added this task:",
                smallIndentation + newTask.toUiString(this.timeFormatter),
                "Now you have " + tasks.getTasks().size() + " tasks in the list."};
        return String.join(System.lineSeparator(), confirmation);
    }

    public String confirmDeletion(Task removedTask, TaskList tasks) {
        String[] confirmation = {"Noted! I have removed this task:",
                smallIndentation + removedTask.toUiString(this.timeFormatter),
                "Now you have " + tasks.getTasks().size() + " tasks in the list."};
        return String.join(System.lineSeparator(), confirmation);
    }

    public String confirmMark(Task markedTask) {
        String[] confirmation = {"Good job! I have marked this task as done!",
                smallIndentation + markedTask.toUiString(this.timeFormatter)};
        return String.join(System.lineSeparator(), confirmation);
    }

    public String confirmUnmark(Task unmarkedTask) {
        String[] confirmation = {"Alright! I have marked this task as not done yet.",
                smallIndentation + unmarkedTask.toUiString(this.timeFormatter)};
        return String.join(System.lineSeparator(), confirmation);
    }

    /**
     * Displays an error message for invalid user input.
     *
     * @param message Error message to display.
     * @param userInput User input that caused the error.
     */
    public String showUserInputErrorMessage(String message, String userInput) {
        String[] errorMessage = {"Oops! We encountered a problem!",
                message,
                smallIndentation + "Received: \"" + userInput + "\"",
                "Please check your command and try again."};
        return String.join(System.lineSeparator(), errorMessage);
    }

    /**
     * Displays an error message when data cannot be loaded.
     *
     * @param message Error message to display.
     */
    public String showLoadDataErrorMessage(String message) {
        String[] errorMessage = {"Oops! We encountered a problem!",
                message,
                "We'll start with an empty task list instead.",
                "You can also exit and check your data file for issues."};
        return String.join(System.lineSeparator(), errorMessage);
    }

    /**
     * Displays an error message when data cannot be saved.
     *
     * @param message Error message to display.
     */
    public String showSaveDataErrorMessage(String message) {
        String[] errorMessage = {"Oops! We encountered a problem!",
                message,
                "Your task list could not be saved.",
                "Please fix the issue and try again."};
        return String.join(System.lineSeparator(), errorMessage);
    }
}
