package controllers.common;

import java.util.concurrent.CompletableFuture;

import Application.Objects.PopupGen;
import Application.Objects.RunnableWithArg;
import Application.Objects.WaitUtils;
import javafx.scene.layout.AnchorPane;


// import

/* -- 機能、および実装したい物 --
 * [x] 別のScreenに遷移する
 * [-]   遷移時に情報を受信できる
 * [-]   同様、送信できる
 * [x] Popupを出せる
 * [x]   Popupにも情報を渡せる
 * [x]   Popupが閉じられるまで処理を待機する
 * [x]   閉じたらKey inputを更新しなおす必要がある
 *     遷移先のコントローラクラスがわかっている
 */

public interface Screen extends Mutual{
    public void reset();
    public void changeScreen(String fxml);
    public default void popup(AnchorPane mainPane, String type, String title, String message, int timeout, RunnableWithArg onClose){
        PopupGen popupGen = new PopupGen(type);
        popupGen.setTitle(title);
        popupGen.setMessage(message);
        popupGen.setTimeout(timeout);
        popupGen.show(mainPane);

        WaitUtils ppWait = new WaitUtils();
        CompletableFuture<Short> ppFuture = new CompletableFuture<>();
        ppWait.waitForResponseAsync(popupGen, ppFuture);
        
        ppFuture.whenComplete( (result, ex) -> {
            ppWait.shutdown();
            popupGen.close();
            System.out.println("runするでぇ");
            if (onClose != null) onClose.run(result);
        });
    }
}