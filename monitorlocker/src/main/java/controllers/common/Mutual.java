package controllers.common;

// import

/* -- 機能、および実装したい物 --
 * [-] 前画面の状況の把握
 * [x] Key input
 * [ ] 外部への情報送受信
 * [x] ロード時に受信
 * [ ] 並行処理
 */

public interface Mutual{

    public void updateKeyBinding();

}

/* updateKeyBinding()雛形
    public void updateKeyBinding(){
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

                case J: // <
                    System.out.println("< key pressed");
                    break;
                case L: // >
                    System.out.println("> key pressed");
                    break;

                case I: // ^
                    System.out.println("^ key pressed");
                    break;
                case K: // v
                    System.out.println("v  key pressed");
                    break;

                default:
                    break;
            }
        });
    }
 */