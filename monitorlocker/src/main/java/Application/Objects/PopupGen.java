package Application.Objects;

import controllers.common.Popup;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class PopupGen {

    private Popup popup = null;
    private String fxml;
    private String title, message;

    private int timeout = 3000;
    private AnchorPane parent;

    // Caution_selectable | Caution_continueonly | Success_closeonly | Error_closeonly
    public static class type {
        public static final String Caution_selectable = "Caution_selectable";
        public static final String Caution_continueonly = "Caution_continueonly";
        public static final String Success_closeonly = "Success_closeonly";
        public static final String Error_closeonly = "Error_closeonly";
    }

    public PopupGen(String type) {
        fxml = "/fxml/popup/"+type+".fxml";
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    public int getTimeout() {
        return this.timeout;
    }

    public void show(AnchorPane parent) {
        this.parent = parent;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        try {
            parent.getChildren().add(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        

        popup = loader.getController();
        popup.setTitle(this.title);
        popup.setMessage(this.message);
        // popup.setTimeout(this.timeout);
        popup.updateKeyBinding();
    }

    public void waitForResponse() {
        popup.waitForResponse();
    }
    public short getResponse() {
        return popup.getStat();
    }
    public void close() {
        if (popup == null || parent == null) {
            return;
        }
        popup.cansel();
        System.out.println("popup closing");
        Node nodeToRemove = parent.lookup("#pane_popup");
        if (nodeToRemove != null) {
            Platform.runLater(() -> { parent.getChildren().remove(nodeToRemove); });
        }else{
            System.err.println("id: popup_pane not found");
        }
    }
}
