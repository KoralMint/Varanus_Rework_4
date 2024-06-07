package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import Application.Main;
import Application.Objects.ScreenChanger;
import controllers.common.Screen;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class c_StartWithAuth implements Screen, Initializable {

    @FXML private AnchorPane bg_step1;
    @FXML private AnchorPane bg_step2;
    @FXML private Text text_description;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("StartAuth initializing...");
    }

    @Override
    public void receiveDataFromPrevious(Object data) { }

    @Override
    public void changeScreen(String fxml) {
        ScreenChanger sc = new ScreenChanger();
        // "/fxml/PortIdSelection.fxml"
        sc.changeScreen(fxml);
    }

    @Override
    public void popup(String fxml) {
        //認証失敗 > 最初の状態に戻るPopUp
    }

    @Override
    public void updateKeyBinding(){
        Main.PRIMARYSTAGE.getScene().setOnKeyPressed(e -> {
            switch(e.getCode()){
                case Z: // Blue
                case X: // Red

                case J: // <
                case L: // >
                case I: // ^
                case K: // v
                    break;

                default:
                    break;
            }
        });
    }
}
