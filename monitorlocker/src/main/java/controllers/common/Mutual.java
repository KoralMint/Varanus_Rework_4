package controllers.common;

// import

/* -- 機能、および実装したい物 --
 * [ ] 前画面の状況の把握
 * [x] Key input
 * [ ] 外部への情報送受信
 * [ ] 並行処理
 */

public interface Mutual{

    public void updateKeyBinding();
}

/* updateKeyBinding()雛形
    public void updateKeyBinding(){        
        if( Main.PRIMARYSTAGE.getScene() == null){
            return;
        }
        Main.PRIMARYSTAGE.getScene().setOnKeyPressed(e -> {
            switch(e.getCode()){
                // Z X, J L I K
                // B R, < > ^ v

                case Z: // Blue
                    print("Blue key pressed");
                    break;
                case X: // Red
                    print("Red key pressed");
                    break;

                case J: // <
                    print("< key pressed");
                    break;
                case L: // >
                    print("> key pressed");
                    break;

                case I: // ^
                    print("^ key pressed");
                    break;
                case K: // v
                    print("v  key pressed");
                    break;

                default:
                    break;
            }
        });
    }
 */