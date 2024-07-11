package Application;

import com.fasterxml.jackson.databind.JsonNode;

import Application.Objects.*;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage PRIMARYSTAGE = null;
    public static String db_api_host = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        PRIMARYSTAGE = primaryStage;
        System.out.println("Starting Varansus GUI Main..");
        
		// カレントワーキングディレクトリを取得
        String currentDirectory = System.getProperty("user.dir");
		System.out.println("* カレントワーキングディレクトリ: " + currentDirectory);
        //java, javafxランタイムのバージョンを取得
        System.out.println("* Java Runtime Version: " + System.getProperty("java.runtime.version"));
        System.out.println("* JavaFX Runtime Version: " + System.getProperty("javafx.runtime.version"));

        load_config();

        resetUser();

        ScreenChanger screenChanger = new ScreenChanger();
        screenChanger.firstScreen("/fxml/screen/StartWithAuthentication.fxml");


    }

    ///////////////////////////////////////////////////

    private static User user;
    private static boolean isUserAuthenticated;

    public static void resetUser() {
        if(user != null) {
            user = null;
        }
        user = new User();
        isUserAuthenticated = false;
    }
    public static void setUser(User _user) {
        user = _user;
        isUserAuthenticated = true;
    }
    public static User getUser() { return user; }
    public static boolean isUserAuthenticated() { return isUserAuthenticated; }

    ///////////////////////////////////////////////////
    
    // shellscriptを動かせれば鍵操作できる
    
    ///////////////////////////////////////////////////

    private void load_config(){
        
        ConfigLoader configLoader = new ConfigLoader();

        // 開発環境
        // configLoader.loadConfig("src/main/resources/config.properties");

        // 実行環境
        configLoader.loadConfig("config.properties");

        db_api_host = configLoader.getProperty("db_api_host");
        System.out.println(db_api_host);
    }

    private static boolean isHostAvailable_cleared = false;
    public static boolean isHostAvailable(){
        if (db_api_host == null) return false;
        if (isHostAvailable_cleared) return true;

        try {
            HttpIO get = new HttpIO(db_api_host+"/connection_test");
            JsonNode apiResponce = get.get();
            if (apiResponce.get("stats").asText().equals("ok")){
                isHostAvailable_cleared = true;
                return true;
            } else
                return false;
        } catch (Exception e) {
            return false;
        }
    }
}
