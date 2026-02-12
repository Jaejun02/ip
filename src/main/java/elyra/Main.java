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
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            int stageMinHeight = 220;
            stage.setMinHeight(stageMinHeight);
            int stageMinWidth = 417;
            stage.setMinWidth(stageMinWidth);
            fxmlLoader.<MainWindow>getController().setElyra(elyra); // inject the Duke instance

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
