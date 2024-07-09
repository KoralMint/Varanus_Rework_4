package controllers;

import controllers.common.Screen;
import Application.Main;
import Application.Objects.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.JsonNode;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class c_StartWithAuth implements Screen, Initializable {

    @FXML private AnchorPane mainPane;
    @FXML private AnchorPane bg_step1;
    @FXML private AnchorPane bg_step2;
    @FXML private Text text_description;

    private int status = 0; // 0: 操作待ち, 1: 認証待ち, 2: 認証成功 | 3: 認証失敗, 4: 認証エラー | 5: タイムアウト, 6: カードタイプ不一致 , 7: 中断

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("StartAuth initializing...");
        status = 0;
    }

    private void auth() {
        Platform.runLater(() -> { 
            bg_step1.setStyle("-fx-background-color: #ffffff;");
            bg_step2.setStyle("-fx-background-color: #ccdeff;");
        });

        WaitUtils nfcWait = new WaitUtils();
        CompletableFuture<Short> nfcFuture = new CompletableFuture<>();

        nfcWait.waitForTask(() -> {

            NfcController nfc = new NfcController();
            // 認証中
            status = 1;
            boolean _success = nfc.readNfc();
            System.out.println("NFC: "+nfc.getStatus());
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
                    status = 4;
                    System.out.println("認証エラー");
                    e.printStackTrace();
                }
            } else {
                // 読取失敗
                if (nfc.getStatusCode() == -1){
                    System.out.println("リーダータイムアウト");
                    status = 5;
                } else if (nfc.getStatusCode() == -2){
                    System.out.println("カードタイプ不一致");
                    status = 6;
                } else if (nfc.getStatusCode() == -3){
                    System.out.println("中断");
                    status = 7;
                } else {
                    System.out.println("読取失敗");
                    status = -1;
                
                }
            }
    

            nfcFuture.complete((short)1);
        }, -1);

        nfcFuture.whenCompleteAsync((result, ex) -> {
            nfcWait.shutdown();
            Platform.runLater(() -> { afterAuth(); });
        });

        
    }

    private void afterAuth(){
        // 認証終了
        if (status == 2) {
            changeScreen("/fxml/screen/PortIdSelection.fxml");
        }else{
            String title, message, popupType; int timeout;
            switch (status) {
                case 3:
                    title = "認証失敗";
                    message = "ユーザーが見つかりませんでした。\n続行すると、貸出/返却処理ができません。\n続行しますか？";
                    popupType = PopupGen.type.Caution_selectable;
                    timeout = -1;
                    break;            
                case 4:
                    title = "認証エラー";
                    message = "サーバーとの通信に失敗しました。\n";
                    popupType = PopupGen.type.Error_closeonly;
                    timeout = 3000;
                    break;
                case 5:
                    title = "タイムアウト";
                    message = "リーダーとの通信がタイムアウトしました。\n";
                    popupType = PopupGen.type.Error_closeonly;
                    timeout = 3000;    
                    break;
                case 6:
                    title = "カードタイプ不一致";
                    message = "対応していないカードが検出されました。\n";
                    popupType = PopupGen.type.Error_closeonly;
                    timeout = 3000;
                    break;
                case 7:
                    title = "中断";
                    message = "操作が中断されました。\n";
                    popupType = PopupGen.type.Caution_continueonly;
                    timeout = 3000;
                    break;
                default:
                    title = "エラー";
                    message = "不明なエラーが発生しました。\n";
                    popupType = PopupGen.type.Error_closeonly;
                    timeout = 3000;
                    break;
            }

            PopupGen popupGen = new PopupGen(popupType);
            popupGen.setTitle(title);
            popupGen.setMessage(message);
            popupGen.setTimeout(timeout);
            popupGen.show(mainPane);
            
            WaitUtils ppWait = new WaitUtils();
            CompletableFuture<Short> ppFuture = new CompletableFuture<>();
            ppWait.waitForResponseAsync(popupGen, ppFuture);
            
            ppFuture.whenComplete((result, ex) -> {
                ppWait.shutdown();
                if ( status == 3 && result == 1 ){
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
        status = 0;
        Platform.runLater(() -> { 
            bg_step1.setStyle("-fx-background-color: #ccdeff;");
            bg_step2.setStyle("-fx-background-color: #ffffff;");
        });
        updateKeyBinding();
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
