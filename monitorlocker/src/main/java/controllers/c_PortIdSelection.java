package controllers;
import Application.Main;
import Application.Objects.HttpIO;
import Application.Objects.Color;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.JsonNode;

import controllers.common.Screen;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
public class c_PortIdSelection implements Screen, Initializable {

    @FXML private Label label_modeSwapLend;
    @FXML private Label label_modeSwapReturn;

    // TODO: port数を可変長にしたい　このファイルは現時点すべて8でハードコードされている
    @FXML private Pane bl_port1;
    @FXML private Pane bl_port2;
    @FXML private Pane bl_port3;
    @FXML private Pane bl_port4;
    @FXML private Pane bl_port5;
    @FXML private Pane bl_port6;
    @FXML private Pane bl_port7;
    @FXML private Pane bl_port8;

    @FXML private AnchorPane mainPane;
    @FXML private Text txt_portId;
    @FXML private Text txt_operationAvailableState;


    private int selectedPortId = 0;
    private boolean isOperationAvailable = false;
    private boolean isLendMode = true;
    Map<Integer, String> lendingPortsMap;
    //---------------------------------------------------------------------//

    @Override
    public void initialize(URL location, ResourceBundle resourcesbundle) {
    	print("PortIdSelection initializing...");

        print("Getting Lending State...");
        // Get Lending State
        try{
            HttpIO get = new HttpIO("GET", "http://127.0.0.1:5000/api/portlendingstate/fetch");
            JsonNode apiResponce = get.get();
            
            // organize apiResponce - { port_id: user_name, ... }
            lendingPortsMap = new HashMap<>();
            for(JsonNode result : apiResponce.get("result"))
            lendingPortsMap.put(result.get("port_id").asInt(), result.get("user_name").asText());
            print(lendingPortsMap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void changeSelectedPortId( int change ){

        // update selectedPortId

        if (lendingPortsMap == null) {
            print("changeSelectedPortId : organizedApiResponce is null");
            return;
        }

        int _selectedPortId = (selectedPortId + change + 8) % 8;
        change = change>=0 ? +1 : -1;
        int searchLimit = 8;
        while( lendingPortsMap.containsKey(_selectedPortId) == isLendMode && searchLimit-- > 0){
            // loop until a port suitable for the mode is found
            _selectedPortId = (_selectedPortId + change +8 ) % 8;
        }

        if(searchLimit == 0){
            isOperationAvailable = false;
            _selectedPortId = -1;
        }else{
            isOperationAvailable = true;
        }
        selectedPortId = _selectedPortId;

        // update GUI

        txt_portId.setText(String.valueOf(selectedPortId + 1));
        
        String _txt = "";
        if(isOperationAvailable){
            if(isLendMode) _txt = "持ち出し可能";
            else           _txt = String.format("%.13s%sさんが使用中", 
                                    lendingPortsMap.get(selectedPortId),
                                    lendingPortsMap.get(selectedPortId).length() > 13 ? "…" : "");
        }else{
            if(isLendMode) _txt = "持ち出せるモニターが\nありません";
            else           _txt = "使用中のモニターが\nありません";
        }
        txt_operationAvailableState.setText(_txt);

        Pane[] portPanes = {bl_port1, bl_port2, bl_port3, bl_port4, bl_port5, bl_port6, bl_port7, bl_port8};
        for(int i=0; i<portPanes.length; i++){
            Pane portPane = portPanes[i];
            portPane.setStyle("-fx-background-color: " + (lendingPortsMap.containsKey(i) ? Color.red : Color.blue));
            portPane.setOpacity(i==selectedPortId? 1.0 : 0.2);
        }
    }


    private void swapMode(){
        isLendMode = !isLendMode;
        label_modeSwapLend.setStyle("-fx-background-color: " + (isLendMode ? Color.blue : Color.none));
        label_modeSwapReturn.setStyle("-fx-background-color: " + (!isLendMode ? Color.red : Color.none));
        selectedPortId = 0;
        changeSelectedPortId(0);
    }
    @Override
    public void updateKeyBinding(){        
        if( Main.PRIMARYSTAGE.getScene() == null){
            return;
        }
        Main.PRIMARYSTAGE.getScene().setOnKeyPressed(e -> {
            switch(e.getCode()){
                // Z X, J L I K
                // B R, < > ^ v

                case Z: // Blue
                    print("Blue key pressed");
                    break;
                case X: // Red
                    print("Red key pressed");
                    break;

                case J: // <
                    swapMode();
                    break;
                case L: // >
                    swapMode();
                    break;

                case I: // ^
                    changeSelectedPortId(+1);
                    break;
                case K: // v
                    changeSelectedPortId(-1);
                    break;

                default:
                    break;
            }
        });
    }

    private void print(Object obj){
        System.out.println("c_PortIdSelection : " + obj);
    }
}
