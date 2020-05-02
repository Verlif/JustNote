package study.verlif.action;

import javafx.application.Platform;
import study.verlif.manager.UserManager;
import study.verlif.model.User;

public abstract class RegisterAction extends Action {

    private User user;
    private UserManager userManager;

    public RegisterAction(User user) {
        this.user = user;
        this.userManager = UserManager.newInstance();
    }

    @Override
    protected void onStart() {
        if (userManager.registerUser(user)) {
            Platform.runLater(this::onSuccess);
        } else {
            Platform.runLater(() -> onError("注册失败"));
        }
    }

    /**
     * 当注册失败时，会返回失败原因
     *
     * @param errorMsg 失败说明
     */
    public abstract void onError(String errorMsg);

    public abstract void onSuccess();

    @Override
    public boolean isRunning() {
        return false;
    }
}
