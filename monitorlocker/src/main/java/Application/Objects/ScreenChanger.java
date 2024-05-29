package Application.Objects;

import Application.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class ScreenChanger {
    private FXMLLoader loader = null;
    public FXMLLoader getLoader() { return loader; }
    private Object data = null;

    public ScreenChanger() {
    }

    public void firstScreen(String fxml) {
            try {
                loader = new FXMLLoader(Main.class.getResource(fxml));
                if(data != null) {
                    ((controllers.common.Mutual)loader.getController()).setRevievingData(data);
                }
                
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
    
            ((controllers.common.Mutual)loader.getController()).updateKeyBinding();
    }

    // TODO not tested
    public void changeScreen(String fxml) {
        try {
            loader = new FXMLLoader(Main.class.getResource(fxml));
            if(data != null) {
                ((controllers.common.Mutual)loader.getController()).setRevievingData(data);
            }

            Main.PRIMARYSTAGE.getScene().setRoot(loader.load());
        
        } catch (IllegalStateException e) {
            System.out.println("[ERROR] ScreenChanger: fxml name invalid!!");
            System.exit(-1);
        } catch (Exception e) {
            System.out.println("[ERROR] ScreenChanger: fxml load failed!!");
            e.printStackTrace();
        }

        ((controllers.common.Mutual)loader.getController()).updateKeyBinding();

    }

    public void setTitle(String title) {
        Main.PRIMARYSTAGE.setTitle(title);
    }

    public void setSendData( Object data ) {
        this.data = data;
    }
}
