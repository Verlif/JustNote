package study.verlif.action;

import javafx.application.Platform;

public abstract class Action {

    /**
     * 开始执行动作
     */
    public void start() {
        new Thread(() -> {
            onStart();
            Platform.runLater(this::onStop);
        }).start();
    }

    protected abstract void onStart();

    /**
     * 停止动作，动作一旦停止，则被销毁
     */
    public void stop() {}

    /**
     * 检测是否正在执行动作
     * @return  是否正在执行动作
     */
    abstract boolean isRunning();

    /**
     * 当动作停止时触发
     */
    public void onStop() {}
}
