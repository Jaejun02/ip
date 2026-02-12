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

    /** Initializes the main window and binds the scroll pane to the dialog container height */
    @FXML
    public void initialize() {
        assert scrollPane != null : "scrollPane was not injected (FXML mismatch?)";
        assert dialogContainer != null : "dialogContainer was not injected (FXML mismatch?)";
        assert userInput != null : "userInput was not injected (FXML mismatch?)";
        assert sendButton != null : "sendButton was not injected (FXML mismatch?)";

        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Elyra instance with default greeting */
    public void setElyra(Elyra elyra) {
        assert elyra != null : "setElyra called with null";
        assert this.elyra == null : "setElyra should only be called once";
        this.elyra = elyra;
        dialogContainer.getChildren().add(DialogBox.getElyraDialog(this.elyra.getGreeting(), elyraImage));
        if (elyra.haveLoadError()) {
            dialogContainer.getChildren().add(DialogBox.getElyraDialog(this.elyra.getLoadDataErrorMessage(),
                    elyraImage));
        }
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Elyra's reply and then appends them to
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
                DialogBox.getElyraDialog(result.response(), elyraImage)
        );
        userInput.clear();

        if (result.isExit()) {
            double delaySeconds = 1.0;
            PauseTransition delay = new PauseTransition(Duration.seconds(delaySeconds));
            delay.setOnFinished(e -> Platform.exit());
            delay.play();
        }
    }
}
