package controllers;

import controllers.common.Screen;
import Application.Main;
import controllers.c_PortIdSelection;
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

    int selectedPortId;
    boolean isLendMode = false;

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        System.out.println("Operation initializing...");

        selectedPortId = c_PortIdSelection.getSelectedPortId();
        isLendMode = c_PortIdSelection.getIsLendMode();
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
                    popup(mainPane, PopupGen.type.Success_closeonly, 
                        "完了", null, selectedPortId, null);
                    break;
                case X: // Red
                    // cancel
                    System.out.println("Cancel");
                    popup(mainPane, PopupGen.type.Caution_selectable, 
                        "キャンセルしますか？", "キャンセルすると最初の画面に戻ります。",
                        -1,
                        (result1) -> {
                            if ((short)result1 == 1) {
                                popup(mainPane, PopupGen.type.Caution_continueonly, 
                                    "キャンセルされました", ( isLendMode ? "鍵穴を抜いたままにしないようご注意ください。" : "鍵穴を挿していないことをご確認ください。" ),
                                    10000,
                                    (result2) -> {
                                        changeScreen("/fxml/screen/StartWithAuthentication.fxml"); //TODO
                                    });
                            }else{
                                reset();
                            }
                            
                        });

                    
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
