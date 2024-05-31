package controllers.common;

// import

/* -- 機能、および実装したい物 --
 * [x] 別のScreenに遷移する TODO
 * [-]   遷移時に情報を受信できる
 * [-]   同様、送信できる
 * [ ] Popupを出せる
 * [ ]   Popupにも情報を渡せる
 * [ ]   Popupが閉じられるまで処理を待機する
 * [?]   閉じたらKey inputを更新しなおす必要がある
 *     遷移先のコントローラクラスがわかっている
 */

public interface Screen extends Mutual{
    public void changeScreen(String fxml);
    public void popup(String fxml);
}