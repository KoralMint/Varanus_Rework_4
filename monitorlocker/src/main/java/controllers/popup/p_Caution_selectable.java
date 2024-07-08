package controllers.popup;

import Application.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class p_Caution_selectable implements controllers.common.Popup, Initializable {
    
    private short stat = -1;
    private final Object lock = new Object();

    @FXML private javafx.scene.text.Text txt_title;
    @FXML private javafx.scene.text.Text txt_msg;

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        System.out.println("Caution_selectable popup initializing...");
        stat = -1;
    }

    @Override
    public void setTitle(String title) {
        Platform.runLater(() -> { txt_title.setText(title); });
    }
    @Override
    public void setMessage(String message) {
        Platform.runLater(() -> { txt_msg.setText(message); });
    }

    @Override
    public void waitForResponse() {
        System.out.println("Caution_selectable popup is running");

        synchronized (lock) {
            while (stat < 0) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Caution_selectable popup thread closed");
    }

    private void stop() {
        synchronized(lock) {
            lock.notify();
        }
    }

    @Override
    public void cansel() {
        if (stat < 0) {
            stat = 0;
            stop();
        }
    }

    @Override
    public short getStat() { return stat; }

    @Override
    public void updateKeyBinding() {
        Main.PRIMARYSTAGE.getScene().setOnKeyPressed(e -> {
            switch(e.getCode()){
                // Z X, J L I K
                // B R, < > ^ v
                case Z: // Blue
                    System.out.println("Blue key pressed > continue");
                    stat = 1;
                    stop();
                    break;
                case X: // Red
                    System.out.println("Red key pressed > cancel");
                    stat = 0;
                    stop();
                    break;

                default:
                    break;
            }
        });
    }
}
