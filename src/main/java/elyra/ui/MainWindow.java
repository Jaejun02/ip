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
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Elyra instance with default greeting */
    public void setElyra(Elyra elyra) {
        this.elyra = elyra;
        dialogContainer.getChildren().add(DialogBox.getDukeDialog(this.elyra.getGreeting(), elyraImage));
        if (elyra.haveLoadError()) {
            dialogContainer.getChildren().add(DialogBox.getDukeDialog(this.elyra.getLoadDataErrorMessage(),
                    elyraImage));
        }
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        ExecutionResult result = elyra.getResponse(input);
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
