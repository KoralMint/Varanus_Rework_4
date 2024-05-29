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
		System.out.println("カレントワーキングディレクトリ: " + currentDirectory);


        resetUser();

        ScreenChanger screenChanger = new ScreenChanger();
        screenChanger.firstScreen("/fxml/PortIdSelection.fxml");
        

    }


    private static User user;
    private static boolean isUserAuthenticated;

    public static void resetUser() {
        if(user != null) {
            user = null;
        }
        user = new User();
        isUserAuthenticated = false;
    }
    // TODO setter: setUser
    public static User getUser() { return user; } // getter
    public static boolean isUserAuthenticated() { return isUserAuthenticated; } // getter
}
