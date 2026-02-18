package elyra;

import javafx.application.Application;

/**
 * A launcher class to workaround classpath issues.
 */
public class Launcher {
    /**
     * Launches the Elyra application by invoking the JavaFX Application.launch method
     * with the Main class as the entry point.
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
