package controllers.popup;

import Application.Main;
import javafx.fxml.Initializable;

public class p_Caution_selectable implements controllers.common.Popup, Initializable, Runnable {
    
    private short stat = -1;
    private final Object lock = new Object();

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        System.out.println("Caution_selectable popup initializing...");
        stat = -1;
    }

    @Override
    public void setTitle(String title) {}
    @Override
    public void setMessage(String message) {}
    @Override
    public void setTimeout(int timeout) {}

    @Override
    public void run() {
        System.out.println("Caution_selectable popup is running");
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
    }

    private void stop() {
        synchronized(lock) {
            lock.notify();
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
                    stat = 1;
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
