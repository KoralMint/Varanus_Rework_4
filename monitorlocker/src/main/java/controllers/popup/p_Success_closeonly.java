package controllers.popup;

import Application.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class p_Success_closeonly implements controllers.common.Popup, Initializable {

    private short stat = -1;
    private final Object lock = new Object();

    @FXML private javafx.scene.text.Text txt_title;
    @FXML private javafx.scene.text.Text txt_msg;

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        System.out.println("Success_closeonly popup initializing...");
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
        synchronized (lock) {
            while (stat < 0) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
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
        }
        stop();
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
                case X: // Red
                    System.out.println("Blue/Red key pressed > close");
                    stat = 0;
                    stop();
                    break;

                default:
                    break;
            }
        });
    }
}
