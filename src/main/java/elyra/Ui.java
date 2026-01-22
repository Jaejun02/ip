package elyra;

import java.util.ArrayList;
import java.util.stream.IntStream;

import elyra.task.Task;
import elyra.task.TaskList;

public class Ui {
    private final String splitter = "-".repeat(60);
    private final String indentation = " ".repeat(4);
    private final String smallIndentation = " ".repeat(2);

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
                .mapToObj(i -> (i + 1) + ". " + tasks.get(i).toString())
                .toArray(String[]::new);
        formatPrint(formattedUserInputs);

    }

    public void confirmAddition(Task newTask, TaskList tasks) {
        String[] confirmation = {"Got it! I have added this task:",
                smallIndentation + newTask.toString(),
                "Now you have " + tasks.getTasks().size() + " tasks in the list."};
        formatPrint(confirmation);
    }

    public void confirmMark(Task markedTask) {
        String[] confirmation = {"Good job! I have marked this task as done!",
                smallIndentation + markedTask.toString()};
        formatPrint(confirmation);
    }

    public void confirmUnmark(Task unmarkedTask) {
        String[] confirmation = {"Alright! I have marked this task as not done yet.",
                smallIndentation + unmarkedTask.toString()};
        formatPrint(confirmation);
    }

    public void showErrorMessage(String message, String userInput) {
        String[] errorMessage = {"Oops! We encountered a problem!",
                message,
                smallIndentation + "Received: \"" + userInput + "\"",
                "Please check your command and try again."};
        formatPrint(errorMessage);
    }
}
