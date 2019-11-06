package UI_Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage window) throws Exception {
        Parent root = FXMLLoader.load(getClass().
                getResource("/serverUI.fxml"));

        window.setTitle("Server Settings");

        Scene scene = new Scene(root, 1000, 800);
        scene.getStylesheets().
                add(getClass().
                        getResource("/server_styles.css").
                        toExternalForm());

        window.setScene(scene);
        window.show();
    }


    public static void main(String[] args) {

        launch(args);
    }
}
