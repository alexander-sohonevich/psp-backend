package UI_Application;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class Controller {
    @FXML
    private AnchorPane mainAnchorPane;

    public void initialize() {
        mainAnchorPane.setFocusTraversable(true);
    }
}
