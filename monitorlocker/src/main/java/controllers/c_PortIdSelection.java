package controllers;
import Application.Main;
import Application.Objects.HttpIO;

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

    @Override
    public void initialize(URL location, ResourceBundle resourcesbundle) {
    	print("PortIdSelection initializing...");

        print("Getting Lending State...");
        // Get Lending State
        try{
            HttpIO get = new HttpIO("GET", "http://127.0.0.1:5000/api/portlendingstate/fetch");
            JsonNode apiResponce = get.get();
            
            // organize apiResponce - { port_id: user_id, ... }
            Map<Integer, Integer> organizedApiResponce = new HashMap<>();
            for(JsonNode result : apiResponce.get("result"))
                organizedApiResponce.put(result.get("port_id").asInt(), result.get("user_id").asInt());
            print(organizedApiResponce);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateKeyBinding(){        
        if( Main.PRIMARYSTAGE.getScene() == null){
            return;
        }
        Main.PRIMARYSTAGE.getScene().setOnKeyPressed(e -> {
            switch(e.getCode()){
                // Z X, J K I L
                // B R, < > ^ v

                case Z: // Blue
                    print("Blue key pressed");
                    break;
                case X: // Red
                    print("Red key pressed");
                    break;

                case J: // <
                    print("< key pressed");
                    break;
                case K: // >
                    print("> key pressed");
                    break;

                case I: // ^
                    print("^ key pressed");
                    break;
                case L: // v
                    print("v  key pressed");
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
