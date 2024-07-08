package controllers.common;

// import

/* -- 機能、および実装したい物 --
 *     親となるScreenが存在する
 * [ ]   のち、親Screenに情報を返せる
 *       そのためPopupが閉じられた後もインスタンスは維持される
 *     遷移はしない
 *     基本的に、情報を残さない
 * [ ] 自動で閉じられる(デフォ1.5秒)
 */

public interface Popup extends Mutual{
    public void setTitle(String title);
    public void setMessage(String message);
    // public void setTimeout(int timeout);
    public void waitForResponse();
    public void cansel();
    public short getStat();
}