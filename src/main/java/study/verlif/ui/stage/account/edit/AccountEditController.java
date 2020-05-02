package study.verlif.ui.stage.account.edit;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.http.util.TextUtils;
import study.verlif.manager.StageManager;
import study.verlif.manager.UserManager;
import study.verlif.model.User;
import study.verlif.ui.alert.SimpleMsgAlert;
import study.verlif.ui.stage.base.BaseController;
import study.verlif.ui.waiting.WaitStage;

public class AccountEditController extends BaseController {

    public TextField nameView;
    public TextField emailView;
    public TextArea descView;

    private UserManager userManager;
    private StageManager stageManager;

    public AccountEditController() {
        userManager = UserManager.newInstance();
        stageManager = StageManager.newInstance();
    }

    @Override
    protected void init() {
        User user = userManager.getLocalUser();
        nameView.setText(user.getUserName());
        emailView.setText(user.getUserEmail());
        descView.setText(user.getUserDesc());
    }

    private User buildUser() {
        User user = userManager.getLocalUser();
        if (TextUtils.isEmpty(nameView.getText())) {
            new SimpleMsgAlert("请将用户名补充完整！", getStage()).show();
            return null;
        }
        user.setUserName(nameView.getText());
        user.setUserEmail(emailView.getText());
        user.setUserDesc(descView.getText());
        return user;
    }

    public void save() {
        User user = buildUser();
        if (user != null) {
            WaitStage stage = stageManager.getStage(new WaitStage(getStage()));
            stage.show();
            new Thread(() -> {
                if (userManager.updateUser(user)) {
                    Platform.runLater(() -> {
                        new SimpleMsgAlert("更新成功", getStage()).show();
                        stage.close();
                        close();
                    });
                } else {
                    Platform.runLater(() -> {
                        new SimpleMsgAlert("更新失败", getStage()).show();
                        stage.close();
                    });
                }
            }).start();
        }

    }

    public void cancel() {
        close();
    }
}
