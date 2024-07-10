package controllers;

import controllers.common.Screen;
import Application.Main;
import Application.Objects.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public class c_Operation implements Screen, Initializable{

    @FXML private Arc arc_autoFinish;
    @FXML private GridPane grid_portPosition;
    @FXML private AnchorPane mainPane;
    @FXML private Text txt_autoFInishInteraction;
    @FXML private Text txt_operationType;
    @FXML private Text txt_portId;

    private int selectedPortId;
    private boolean isLendMode = true;
    private short operationMode = 0; // 0: 貸出, 1: 返却, 2: 代理返却

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        System.out.println("Operation initializing...");

        selectedPortId = c_PortIdSelection.getSelectedPortId();
        isLendMode = c_PortIdSelection.getIsLendMode();
        operationMode = c_PortIdSelection.getOperationMode();
        setPortDisplay();
    }

    private void setPortDisplay(){
        txt_portId.setText(String.valueOf(selectedPortId + 1));
        txt_operationType.setText( operationMode == 0? "貸出" : operationMode == 1? "返却" : "代理返却" );
        for (Node node : grid_portPosition.getChildren()) {
            // get column&row
            Integer colIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);
            if (colIndex == null) colIndex = 0;
            if (rowIndex == null) rowIndex = 0;
            // get node type: pane / polygon / other
            if (node instanceof Pane) {
                Pane pane = (Pane) node;
                // id
                int id = rowIndex*2 + (colIndex-1)/2;
                if (id == selectedPortId) {
                    pane.setStyle(String.format(
                        "-fx-background-radius: 10; -fx-background-color: %s;"
                        , isLendMode? Color.blue : Color.red));
                }
            }else if (node instanceof Polygon) {
                Polygon polygon = (Polygon) node;
                //id
                int id = rowIndex*2 + colIndex/4;
                if (id == selectedPortId) {
                    polygon.setRotate((colIndex==0? -90 : 90) + (isLendMode? 0 : 180));
                    polygon.setFill( isLendMode? Color.Paint.blue : Color.Paint.red);
                    polygon.setOpacity(1);
                } else {
                    polygon.setOpacity(0);
                }
            }
        }
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
                        "完了", isLendMode? "ご利用ありがとうございます。": "ご利用ありがとうございました。",
                        5000,
                        (result) -> {
                            // TODO send api
                            Main.resetUser();
                            c_PortIdSelection.staticReset();
                            changeScreen("/fxml/screen/StartWithAuthentication.fxml");
                        });
                    break;
                case X: // Red
                    // cancel
                    System.out.println("Cancel");
                    popup(mainPane, PopupGen.type.Caution_selectable, 
                        "キャンセルしますか？", "キャンセルすると最初の画面に戻ります。",
                        -1,
                        (result1) -> {
                            if ((short)result1 == 1) {
                                Platform.runLater(() -> {
                                popup(mainPane, PopupGen.type.Caution_continueonly, 
                                    "キャンセルされました", ( isLendMode ? "鍵穴を抜いたままにしないようご注意ください。" : "鍵穴を挿していないことをご確認ください。" ),
                                    10000,
                                    (result2) -> {
                                        // TODO send api
                                        Main.resetUser();
                                        c_PortIdSelection.staticReset();
                                        changeScreen("/fxml/screen/StartWithAuthentication.fxml");
                                    });
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
