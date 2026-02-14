package elyra;

import java.io.IOException;

import elyra.ui.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Elyra using FXML.
 */
public class Main extends Application {

    private Elyra elyra = new Elyra();

    @Override
    public void start(Stage stage) {
        try {
            assert Main.class.getResource("/view/MainWindow.fxml") != null
                    : "Missing resource: /view/MainWindow.fxml";

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            int STAGE_MIN_HEIGHT = 220;
            stage.setMinHeight(STAGE_MIN_HEIGHT);
            int STAGE_MIN_WIDTH = 417;
            stage.setMinWidth(STAGE_MIN_WIDTH);
            fxmlLoader.<MainWindow>getController().setElyra(elyra); // inject the Elyra instance

            stage.setTitle("Elyra");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
