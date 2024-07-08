package controllers;

import Application.Main;
import Application.Objects.HttpIO;
import Application.Objects.ScreenChanger;
import controllers.common.Screen;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.JsonNode;


//import java.lang.reflect.Field;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class c_LendStates implements Initializable, Screen{

    @FXML private VBox resources;
    @FXML private AnchorPane remain_bar;
    @FXML private Pane remain_body;
    @FXML private ImageView remain_terminal;
    @FXML private AnchorPane mainPane;
    
    /*functions-----------------------------------------*/
    

    @Override
    public void initialize(URL location, ResourceBundle resourcesbundle){
    	
		try {
			////resource
			VBox resource;
			resource = FXMLLoader.load(getClass().getResource("/fxml/screen/States_resource.fxml"));
			resources.getChildren().add(resource);
			VBox.setMargin(resource, new Insets(20.0, 10.0, 20.0, 20.0));

			//set text
			Text resource_name = (Text) resource.lookup("#resource_name");
			resource_name.setText("IPFactory モニタ");
			
			int i_remain = 99;
			Text resource_remain = (Text) resource.lookup("#resource_remain");
			resource_remain.setText( Integer.valueOf(i_remain).toString());
			
			System.out.println("resource pane deployed");
				
			////user element
				
			
			try {
				HttpIO get = new HttpIO("http://127.0.0.1:5000/api/portlendingstate/fetch");
            	JsonNode apiResponce = get.get();
				// System.out.println(apiResponce.toString());
				for ( JsonNode n : apiResponce.get("result")) {
					FXMLLoader user_loader = new FXMLLoader(getClass().getResource("/fxml/screen/States_resource_user.fxml"));
					AnchorPane pane = (AnchorPane) user_loader.load();
					
					resource.getChildren().add(pane);
					VBox.setMargin(pane, new Insets(0.0, 0.0, 5.0, 0.0));
					
					tc_StatesUser userctrl = user_loader.getController();
					userctrl.setUserdata(n.get("user_id").asInt(), n.get("user_name").asText(), n.get("time").asText());
				}
			}catch (FileNotFoundException exception) {
				System.out.println("[ERROR] no json!\n--> "+exception.toString());
			}catch (Exception exception) {
				System.out.println("[ERROR] while lending status setting");
				exception.printStackTrace();
			}
			
			
			//AddResource end
		} catch (IOException e) {
			e.printStackTrace();
		}
		//deployment initializing end
    }
	
	@Override
    public void reset() { }

	@Override
	public void changeScreen(String fxml) {
		ScreenChanger sc = new ScreenChanger();
		if(fxml.equals("/fxml/screen/PortIdSelection.fxml")){

		}else if(fxml.equals("/fxml/screen/Log.fxml")){

		}
		sc.changeScreen(fxml);
	}

	@Override
	public short popup(String fxml) { return 0; }

	@Override
	public void updateKeyBinding(){
        Main.PRIMARYSTAGE.getScene().setOnKeyPressed(e -> {
            switch(e.getCode()){
                case X: // Red
					changeScreen("/fxml/screen/PortIdSelection.fxml");
                    break;

                case J: // <
                case L: // >
					changeScreen("/fxml/screen/Log.fxml");
					break;

                case I: // ^
                    System.out.println("^ key pressed");
                    break;
                case K: // v
                    System.out.println("v  key pressed");
                    break;

                default:
                    break;
            }
        });
    }
}
