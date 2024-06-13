package controllers.popup;

import Application.Main;

public class p_Caution_selectable implements controllers.common.Popup {

    

    @Override
    public void updateKeyBinding() {
        Main.PRIMARYSTAGE.getScene().setOnKeyPressed(e -> {
            switch(e.getCode()){
                // Z X, J L I K
                // B R, < > ^ v
                case Z: // Blue
                    System.out.println("Blue key pressed");
                    break;
                case X: // Red
                    System.out.println("Red key pressed");
                    break;

                default:
                    break;
            }
        });
    }
}
