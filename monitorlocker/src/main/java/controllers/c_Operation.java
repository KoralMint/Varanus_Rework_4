package controllers;

import controllers.common.Screen;
import Application.Main;
import Application.Objects.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Arc;
import javafx.scene.text.Text;

public class c_Operation implements Screen, Initializable{

    @FXML private Arc arc_autoFinish;
    @FXML private GridPane grid_portPosition;
    @FXML private AnchorPane mainPane;
    @FXML private Text txt_autoFInishInteraction;
    @FXML private Text txt_operationType;
    @FXML private Text txt_portId;

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        System.out.println("Operation initializing...");
    }

    @Override
    public void reset() {
        updateKeyBinding();
    }

    @Override
    public void changeScreen(String fxml) {
        ScreenChanger sc = new ScreenChanger();
        // "/fxml/screen/StartWithAuthentication.fxml"
        // "/fxml/screen/PortIdSelection.fxml"
        sc.changeScreen(fxml);
    }

    @Override
    public void updateKeyBinding(){
        Main.PRIMARYSTAGE.getScene().setOnKeyPressed(e -> {
            switch(e.getCode()){
                case Z: // Blue
                    // finish
                    System.out.println("Finish");
                    break;
                case X: // Red
                    // cancel
                    System.out.println("Cancel");
                    break;

                case J: // <
                case L: // >
                case I: // ^
                case K: // v

                default:
                    break;
            }
        });
    }
}
