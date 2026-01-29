package elyra.ui;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.IntStream;

import elyra.task.Task;
import elyra.task.TaskList;

public class Ui {
    private final String splitter = "-".repeat(60);
    private final String indentation = " ".repeat(4);
    private final String smallIndentation = " ".repeat(2);
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(
        "MMM dd uuuu, h:mm a", Locale.ENGLISH);

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

    public void showUserInputList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            formatPrint("You currently don't have any tasks in your list.");
            return;
        }

        String[] formattedUserInputs = IntStream.range(0, tasks.size())
            .mapToObj(i -> (i + 1) + ". " + tasks.get(i).toUiString(this.timeFormatter))
            .toArray(String[]::new);
        formatPrint(formattedUserInputs);

    }

    public void confirmAddition(Task newTask, TaskList tasks) {
        String[] confirmation = {"Got it! I have added this task:",
            smallIndentation + newTask.toUiString(this.timeFormatter),
            "Now you have " + tasks.getTasks().size() + " tasks in the list."};
        formatPrint(confirmation);
    }

    public void confirmDeletion(Task removedTask, TaskList tasks) {
        String[] confirmation = {"Noted! I have removed this task:",
            smallIndentation + removedTask.toUiString(this.timeFormatter),
            "Now you have " + tasks.getTasks().size() + " tasks in the list."};
        formatPrint(confirmation);
    }

    public void confirmMark(Task markedTask) {
        String[] confirmation = {"Good job! I have marked this task as done!",
            smallIndentation + markedTask.toUiString(this.timeFormatter)};
        formatPrint(confirmation);
    }

    public void confirmUnmark(Task unmarkedTask) {
        String[] confirmation = {"Alright! I have marked this task as not done yet.",
            smallIndentation + unmarkedTask.toUiString(this.timeFormatter)};
        formatPrint(confirmation);
    }

    public void showUserInputErrorMessage(String message, String userInput) {
        String[] errorMessage = {"Oops! We encountered a problem!",
            message,
            smallIndentation + "Received: \"" + userInput + "\"",
            "Please check your command and try again."};
        formatPrint(errorMessage);
    }

    public void showLoadDataErrorMessage(String message) {
        String[] errorMessage = {"Oops! We encountered a problem!",
            message,
            "We'll start with an empty task list instead.",
            "You can exit anytime and check your data file for issues."};
        formatPrint(errorMessage);
    }

    public void showSaveDataErrorMessage(String message) {
        String[] errorMessage = {"Oops! We encountered a problem!",
            message,
            "Your task list could not be saved.",
            "Please fix the issue and try again."};
        formatPrint(errorMessage);
    }
}
