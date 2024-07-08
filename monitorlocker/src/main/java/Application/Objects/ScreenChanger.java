package Application.Objects;

import Application.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class ScreenChanger {
    private FXMLLoader loader = null;
    public FXMLLoader getLoader() { return loader; }

    public ScreenChanger() {
    }

    public void firstScreen(String fxml) {
            try {
                loader = new FXMLLoader(Main.class.getResource(fxml));
                
                Scene scene = new Scene(loader.load(), 1024, 768);
                Main.PRIMARYSTAGE.setScene(scene);
                Main.PRIMARYSTAGE.show();
            
            } catch (IllegalStateException e) {
                System.out.println("[ERROR] ScreenChanger: fxml name invalid!!");
                System.exit(-1);
            } catch (Exception e) {
                System.out.println("[ERROR] ScreenChanger: fxml load failed!!");
                e.printStackTrace();
            }
    
            ((controllers.common.Screen)loader.getController()).updateKeyBinding();
    }
    
    public void changeScreen(String fxml) {
        try {
            loader = new FXMLLoader(Main.class.getResource(fxml));

            Main.PRIMARYSTAGE.getScene().setRoot(loader.load());
        
        } catch (IllegalStateException e) {
            System.out.println("[ERROR] ScreenChanger: fxml name invalid!!");
            System.exit(-1);
        } catch (Exception e) {
            System.out.println("[ERROR] ScreenChanger: fxml load failed!!");
            e.printStackTrace();
        }

        ((controllers.common.Screen)loader.getController()).updateKeyBinding();

    }

    public void setTitle(String title) {
        Main.PRIMARYSTAGE.setTitle(title);
    }
}
