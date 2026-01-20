public class Chatbot {
    private final String name;

    public Chatbot(String name) {
        this.name = name;
    }

    public void run() {
        String splitter = "-".repeat(60);
        String greeting = "Hello! I am " + name + ", your personal chatbot.\nWhat can I do for you today?";
        String farewell = "Goodbye! Hope to see you again!";

        System.out.println(splitter);
        System.out.println(greeting);
        System.out.println(splitter);
        System.out.println(farewell);
        System.out.println(splitter);
    }
}
