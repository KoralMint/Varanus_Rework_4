package Application;
import java.io.IOException;

import Application.Objects.User;
import controllers.common.Mutual;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

        try {
            
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/PortIdSelection.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1024, 768);
            primaryStage.setScene(scene);
            ((Mutual)loader.getController()).updateKeyBinding();
            primaryStage.show();

        } catch (IllegalStateException e) {
			System.out.println("[ERROR] Main: fxml name invalid!!");
        } catch (IOException e) {
			System.out.println("[ERROR] Main: fxml load failed!!");
			e.printStackTrace();
        }
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
