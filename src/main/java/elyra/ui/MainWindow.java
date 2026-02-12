package elyra.ui;

import elyra.Elyra;
import elyra.command.ExecutionResult;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Elyra elyra;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.png"));
    private Image elyraImage = new Image(this.getClass().getResourceAsStream("/images/Elyra.png"));

    @FXML
    public void initialize() {
        assert scrollPane != null : "scrollPane was not injected (FXML mismatch?)";
        assert dialogContainer != null : "dialogContainer was not injected (FXML mismatch?)";
        assert userInput != null : "userInput was not injected (FXML mismatch?)";
        assert sendButton != null : "sendButton was not injected (FXML mismatch?)";

        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Duke instance */
    public void setElyra(Elyra elyra) {
        assert elyra != null : "setElyra called with null";
        assert this.elyra == null : "setElyra should only be called once";
        this.elyra = elyra;
        dialogContainer.getChildren().add(DialogBox.getDukeDialog(this.elyra.getGreeting(), elyraImage));
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        assert elyra != null : "handleUserInput called before setElyra";
        String input = userInput.getText();
        assert input != null : "TextField#getText returned null (unexpected)";

        ExecutionResult result = elyra.getResponse(input);
        assert result != null : "Elyra#getResponse returned null (unexpected)";
        assert result.response() != null : "result.response is null";

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDukeDialog(result.response(), elyraImage)
        );
        userInput.clear();

        if (result.isExit()) {
            PauseTransition delay = new PauseTransition(Duration.seconds(1.0));
            delay.setOnFinished(e -> Platform.exit());
            delay.play();
        }
    }
}
