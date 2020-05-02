package study.verlif.action;

import javafx.application.Platform;
import study.verlif.manager.UserManager;
import study.verlif.model.User;

public abstract class LoginAction extends Action {

    private User user;
    private boolean isStop;


    public LoginAction(User user) {
        this.user = user;
        this.isStop = false;
    }

    @Override
    protected void onStart() {
        if (!isStop) {
            Platform.runLater(() -> getResult(UserManager.newInstance().login(user)));
        }
    }

    @Override
    public void stop() {
        isStop = true;
    }

    @Override
    public boolean isRunning() {
        return !isStop;
    }

    /**
     * 是否成功登陆的回调
     *
     * @param ifLogin 登陆状态
     */
    public abstract void getResult(boolean ifLogin);
}
