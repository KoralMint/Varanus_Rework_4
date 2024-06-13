package Application;

import Application.Objects.ScreenChanger;
import Application.Objects.User;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage PRIMARYSTAGE = null;

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
}
// shellscriptを動かせれば鍵操作できる