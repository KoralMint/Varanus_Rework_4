package controllers.popup;

import Application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class p_Success_closeonly implements controllers.common.Popup, Initializable, Runnable {
    
    private short stat = -1;
    private int timeout = -1;
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
        txt_title.setText(title);
    }
    @Override
    public void setMessage(String message) {
        txt_msg.setText(message);
    }

    @Override
    public void waitForResponse() {
        Thread t = new Thread(this);
        t.start();
        if (timeout > 0) {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stat = 0;
            stop();
        }
    }

    @Override
    public void run() {
        System.out.println("Success_closeonly popup is running");
        synchronized (lock) {
            while (stat<0) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        // thread closed
        System.out.println("Success_closeonly popup thread closed");
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
                    System.out.println("Blue key pressed");
                    stat = 0;
                    stop();
                    break;
                case X: // Red
                    System.out.println("Red key pressed");
                    stat = 0;
                    stop();
                    break;

                default:
                    break;
            }
        });
    }
}
