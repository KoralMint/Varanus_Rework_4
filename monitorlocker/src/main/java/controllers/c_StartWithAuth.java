package controllers;

import controllers.common.Popup;
import controllers.common.Screen;
import Application.Main;
import Application.Objects.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.JsonNode;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class c_StartWithAuth implements Screen, Initializable {

    @FXML private AnchorPane mainPane;
    @FXML private AnchorPane bg_step1;
    @FXML private AnchorPane bg_step2;
    @FXML private Text text_description;

    private int status = 0; // 0: 操作待ち, 1: 認証待ち, 2: 認証成功, 3: 認証失敗 4: 認証エラー 5: 読取失敗

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("StartAuth initializing...");
        status = 0;
    }

    private void auth() {
        NfcController nfc = new NfcController();
        // 認証中
        status = 1;
        boolean _success = nfc.readNfc();
        if(_success && nfc.hasTagId()) {
            // 読取成功
            System.out.print("読取成功 - ");
            
            try{
                HttpIO getUser = new HttpIO("http://127.0.0.1:5000/api/userdata/fetch");
                String userdataReq = "[\"user_name\", \"user_id\", \"user_authority\", \"email\", \"discord_id\"]";
                JsonNode apiResponce = getUser.post( String.format("{\"tag_id\": \"%s\", \"request\":%s}", nfc.getTagId(), userdataReq) );
                if (apiResponce.get("stats").asText().equals("ok")){
                    // 認証成功
                    status = 2;
                    System.out.print("認証成功 - ");

                    User _user = new User(
                        nfc.getTagId(),
                        apiResponce.get("user_name").asText(),
                        apiResponce.get("user_id").asText(),
                        apiResponce.get("user_authority").asInt(),
                        apiResponce.get("email").asText(),
                        apiResponce.get("discord_id").asText()
                    );
                    Main.setUser(_user);
                    System.out.printf( "Welcome %s ( %s )\n", _user.getUserName(), _user.getUserId() );
                }else{
                    // 認証失敗
                    status = 3;
                    System.out.println("認証失敗");
                    System.out.println(apiResponce.get("stats").asText());
                }

            } catch (Exception e) {
                status = 3;
                System.out.println("認証エラー");
                e.printStackTrace();
            }
        } else {
            // 読取失敗
            status = 3;
            System.out.println("読取失敗");
        }

        // 認証終了
        if (status == 2) {
            changeScreen("/fxml/screen/PortIdSelection.fxml");
        }else{
            String title, message;
            switch (status) {
                case 3:
                    title = "認証失敗";
                    message = "ユーザーが見つかりませんでした。\n";
                    break;            
                case 4:
                    title = "認証エラー";
                    message = "サーバーとの通信に失敗しました。\n";
                    break;
                case 5:
                    title = "読取失敗";
                    message = "カード情報が取得できませんでした。\n";
                    break;
                default:
                    title = "エラー";
                    message = "不明なエラーが発生しました。\n";
                    break;
            }
            message += "続行すると、貸出/返却処理ができません。\n続行しますか？";

            PopupGen popupGen = new PopupGen(PopupGen.type.Caution_selectable);
            popupGen.setTitle(title);
            popupGen.setMessage(message);
            popupGen.show(mainPane);
            
            WaitUtils waitUtils = new WaitUtils();
            CompletableFuture<Short> future = new CompletableFuture<>();
            waitUtils.waitForResponseAsync(popupGen, future);
            
            future.whenComplete((result, ex) -> {
                waitUtils.shutdown();
                if (result == 1){
                    changeScreen("/fxml/screen/PortIdSelection.fxml");
                } else {
                    popupGen.close();
                    reset();
                }
            });
        }
    }

    @Override
    public void reset() {
        updateKeyBinding();
        status = 0;
    }

    @Override
    public void changeScreen(String fxml) {
        ScreenChanger sc = new ScreenChanger();
        // "/fxml/PortIdSelection.fxml"
        sc.changeScreen(fxml);
    }

    @Override
	public short popup(String type, String title, String message, int timeout){
        
        return 0;    
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
                    if(this.status == 0)
                        auth();
                    break;

                default:
                    break;
            }
        });
    }
}
