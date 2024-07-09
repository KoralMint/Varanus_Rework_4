package controllers;


import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import Application.Main;
import Application.Objects.*;
import controllers.common.Screen;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class c_LendingLog implements Initializable, Screen{

    @FXML private VBox logs;
    @FXML private Text mode_txt;
    @FXML private AnchorPane mainPane;
    
    /*functions-----------------------------------------*/

    @Override
    public void initialize(URL location, ResourceBundle resourcesbundle){
    	System.out.println("log starting up..");

        try {
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode node = mapper.createObjectNode();
                node.put("limit", 10);
                node.put("from", 0);
                node.put("to", 9);
				HttpIO post = new HttpIO("http://127.0.0.1:5000/api/lendinglog/fetch", mapper.writeValueAsString(node));
            	JsonNode apiResponce = post.post();

				// System.out.println(apiResponce.toString());
				for ( JsonNode n : apiResponce.get("result")) {
					FXMLLoader logelm_loader = new FXMLLoader(getClass().getResource("/fxml/screen/Log_element.fxml"));
					AnchorPane pane = (AnchorPane) logelm_loader.load();
					
					logs.getChildren().add(pane);
					VBox.setMargin(pane, new Insets(0.0, 0.0, 5.0, 0.0));
					String datetime = n.get("time").asText();
                    String fmtDatetime = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                        .format(DateTimeFormatter.ofPattern("MM/dd HH:mm"));
                    ((Text) pane.lookup("#datetime") ).setText(fmtDatetime);
                    ((Text) pane.lookup("#user_id") ).setText(n.get("user_id").asText());
                    ((Text) pane.lookup("#user_name") ).setText(n.get("user_name").asText());
                    ((Text) pane.lookup("#port_id") ).setText(n.get("port_id").asText());
                    ((Text) pane.lookup("#is_lent") ).setText(n.get("is_lent").asText());

                }
			}catch (FileNotFoundException exception) {
				System.out.println("[ERROR] no json!\n--> "+exception.toString());
			}catch (Exception exception) {
				System.out.println("[ERROR] while lending status setting");
				exception.printStackTrace();
			}
            // TODO: ログが10個を超えた場合、スクロールによって次の10個を取得する
    }
    
    @Override
    public void reset() { }

	@Override
	public void changeScreen(String fxml) {
		ScreenChanger sc = new ScreenChanger();
		if(fxml.contains("PortIdSelection")){

		}else if(fxml.contains("States")){

		}
		sc.changeScreen(fxml);
	}

	@Override
	public void updateKeyBinding(){
        Main.PRIMARYSTAGE.getScene().setOnKeyPressed(e -> {
            switch(e.getCode()){
                case X: // Red
					changeScreen("/fxml/screen/PortIdSelection.fxml");
                    break;

                case J: // <
                case L: // >
					changeScreen("/fxml/screen/States.fxml");
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