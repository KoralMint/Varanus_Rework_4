package controllers;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.JsonNode;

import Application.Main;
import Application.Objects.Color;
import Application.Objects.HttpIO;
import Application.Objects.ScreenChanger;
import controllers.common.Screen;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
public class c_PortIdSelection implements Screen, Initializable {


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
    @FXML private AnchorPane pane_nextStep;
    @FXML private Text txt_portId;
    @FXML private Text txt_portLendingState;
    @FXML private Text btntxt_nextStepInteraction;


    private static boolean firstTime = true;
    private static int selectedPortId;
    private boolean isLendMode = true;
    Map<Integer, String> lendingPortsMap;
    public int getSelectedPortId(){ return selectedPortId; }
    public boolean getIsLendMode(){ return isLendMode; }

    //---------------------------------------------------------------------//

    @Override
    public void initialize(URL location, ResourceBundle resourcesbundle) {
    	System.out.println("PortIdSelection initializing...");

        // Get Lending State
        try{
            HttpIO get = new HttpIO("http://127.0.0.1:5000/api/portlendingstate/fetch");
            JsonNode apiResponce = get.get();
            
            if(apiResponce == null || apiResponce.get("result") == null) {
                System.out.println("Error: apiResponce is null");
                return;
            }

            // organize apiResponce - { port_id: user_name, ... }
            lendingPortsMap = new HashMap<>();
            for(JsonNode result : apiResponce.get("result"))
                lendingPortsMap.put(result.get("port_id").asInt(), result.get("user_name").asText());
            lendingPortsMap.put(6, "koral");
            System.out.println(lendingPortsMap);
        }catch(Exception e){
            e.printStackTrace();
        }

        // disable button
        if(!Main.isUserAuthenticated()){
            pane_nextStep.setDisable(true);
            pane_nextStep.setOpacity(0.3);
        }
        // init cursor
        selectedPortId = (selectedPortId>0 ? selectedPortId : 0);
        if(firstTime) {
            firstTime = false;
            if (Main.isUserAuthenticated()){
                String _userName = Main.getUser().getUserName();
                if(lendingPortsMap.containsValue(_userName)){
                    // select first port that matches the user
                    for (Map.Entry<Integer, String> entry : lendingPortsMap.entrySet()) {
                        if(entry.getValue().equals(_userName)){
                            selectedPortId = entry.getKey();
                            break;
                        }
                    }
                }else{
                    // select first port that is not lending
                    for (int i = 0; i < 8; i++) {
                        if(!lendingPortsMap.containsKey(i)){
                            selectedPortId = i;
                            break;
                        }
                    }
                }
            }
        }
        isLendMode = !lendingPortsMap.containsKey(selectedPortId);
        movePortCursor(0,0);
    }


    private void movePortCursor(int _x, int _y){
        final int boundX=1, boundY=3;
        // get cursor position
        int x = selectedPortId % (boundX+1);
        int y = selectedPortId / (boundX+1);
        // move cursor
        x+=_x; y+=_y;
        x= Math.max(0, Math.min(x, boundX));
        y= Math.max(0, Math.min(y, boundY));
        // set cursor position
        selectedPortId = x + y * (boundX+1);

        // apply mode
        txt_portId.setText(String.valueOf(selectedPortId + 1));
        isLendMode = !lendingPortsMap.containsKey(selectedPortId);
        btntxt_nextStepInteraction.setText(isLendMode? "持出" : "返却");
        
        // apply port state
        if(isLendMode)
            txt_portLendingState.setText(Main.isUserAuthenticated() ? "持ち出し可能" : "未使用");
        else
            txt_portLendingState.setText(
                String.format("%.13s%sさんが使用中", 
                lendingPortsMap.get(selectedPortId),
                lendingPortsMap.get(selectedPortId).length() > 13 ? "…" : "") );

        
        Pane[] portPanes = {bl_port1, bl_port2, bl_port3, bl_port4, bl_port5, bl_port6, bl_port7, bl_port8};
        for(int i=0; i<portPanes.length; i++){
            Pane portPane = portPanes[i];
            portPane.setStyle("-fx-background-color: " + (lendingPortsMap.containsKey(i) ? Color.red : Color.blue));
            portPane.setOpacity(i==selectedPortId? 1.0 : 0.2);
        }
    }

    @Override
    public void reset() {
        firstTime = true;
        selectedPortId = 0;
        isLendMode = true;
    }

    @Override
    public void changeScreen(String fxml) {
        ScreenChanger sc = new ScreenChanger();
        if(fxml.equals("/fxml/States.fxml")){
        }else if(true){
            
        }
        sc.changeScreen(fxml);
    }
    @Override
    public void popup(String fxml) {

    }

    @Override
    public void updateKeyBinding(){
        Main.PRIMARYSTAGE.getScene().setOnKeyPressed(e -> {
            switch(e.getCode()){
                case Z: // Blue
                    if(Main.isUserAuthenticated()){
                        if(isLendMode){
                            // lend
                            // continue
                        }else{
                            // return
                            if( lendingPortsMap.get(selectedPortId).equals(Main.getUser().getUserName())){ // user is owner 
                                // continue
                            }else{
                                // popup: user is not owner
                                // confirm > continue
                            }
                        }
                    }else{
                        // popup: user not authenticated
                    }
                    break;
                case X: // Red
                    changeScreen("/fxml/screen/States.fxml");
                    break;

                case J: // <
                    movePortCursor(-1,0);
                    break;
                case L: // >
                    movePortCursor(+1,0);
                    break;

                case I: // ^
                    movePortCursor(0,-1);
                    break;
                case K: // v
                    movePortCursor(0,+1);
                    break;

                default:
                    break;
            }
        });
    }
}
