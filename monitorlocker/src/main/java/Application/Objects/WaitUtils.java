package Application.Objects;

import java.util.concurrent.*;

public class WaitUtils {
    private final Object lock = new Object();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void waitForResponseAsync(PopupGen popupGen, CompletableFuture<Short> future) {
        Task task = () -> {
            popupGen.waitForResponse();
            future.complete(popupGen.getResponse()); // 結果を通知
        };
        int timeout = popupGen.getTimeout();
        waitForTask(task, timeout, () -> {popupGen.close();});
    }

    public void waitForTask(Task task) {
        waitForTask(task, -1, null);
    }
    public void waitForTask(Task task, int timeoutMillis) {
        waitForTask(task, timeoutMillis, null);
    }
    public void waitForTask(Task task, int timeoutMillis, Task onTimeout) {
        Thread t = new Thread(() -> {
            task.run(); // タスク実行
            synchronized (lock) {
                lock.notify(); // 待機中のスレッドを再開
            }
        });
        t.start();
        if (timeoutMillis > 0) {
            System.out.println("timeout: " + timeoutMillis);
            // タイムアウト処理をスケジュール
            scheduler.schedule(() -> {
                synchronized (lock) {
                    lock.notify(); // タイムアウト時にも待機中のスレッドを再開
                }
                if (onTimeout != null) {
                    onTimeout.run(); // タイムアウト時の処理
                }
            }, timeoutMillis, TimeUnit.MILLISECONDS);
        }
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    public interface Task {
        void run();
    }
}
