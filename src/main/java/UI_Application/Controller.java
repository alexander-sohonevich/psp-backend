package UI_Application;


import Server.Server;
import Server.TCPConnection;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;


public class Controller {

    public void initialize() {


        mainAnchorPane.setVisible(true);
        usersList.setVisible(true);



    }

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private ListView<TCPConnection> usersList;


}
