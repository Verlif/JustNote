package study.verlif.ui.stage.register;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import study.verlif.action.RegisterAction;
import study.verlif.manager.StageManager;
import study.verlif.manager.UserManager;
import study.verlif.manager.inner.Message;
import study.verlif.model.User;
import study.verlif.ui.alert.SimpleMsgAlert;
import study.verlif.ui.stage.base.BaseController;
import study.verlif.ui.stage.login.LoginStage;
import study.verlif.ui.stage.main.MainController;
import study.verlif.ui.waiting.WaitStage;

public class RegisterController extends BaseController {
    public VBox root;
    public TextField userNameView;
    public PasswordField userKeyView;
    public PasswordField userKeyReView;
    public Label checkTip;
    public Button buttonLogin;
    public Button buttonRegister;

    private UserManager userManager;
    private StageManager stageManager;

    public RegisterController() {
        userManager = UserManager.newInstance();
        stageManager = StageManager.newInstance();
    }

    @Override
    protected void init() {
        userKeyReView.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals(userKeyView.getText())) {
                    checkTip.setText("√");
                } else {
                    checkTip.setText("×");
                }
            }
        });
    }

    public void login() {
        LoginStage stage = stageManager.getStage(new LoginStage());
        stage.show();
        close();
    }

    public void register() {
        User user = getUserInfo();
        if (user != null) {
            WaitStage waitStage = stageManager.getStage(new WaitStage(root.getScene().getWindow()));
            RegisterAction registerAction = new RegisterAction(user) {
                @Override
                public void onError(String errorMsg) {
                    SimpleMsgAlert simpleMsgAlert = new SimpleMsgAlert("注册失败，失败原因: " + errorMsg, getStage());
                    simpleMsgAlert.show();
                }

                @Override
                public void onSuccess() {
                    sendMessageLogin();
                    SimpleMsgAlert simpleMsgAlert = new SimpleMsgAlert("注册成功，已自动登陆", getStage());
                    simpleMsgAlert.show();
                    close();
                }

                @Override
                public void onStop() {
                    waitStage.close();
                }
            };
            waitStage.show();
            registerAction.start();
        }
    }

    private User getUserInfo() {
        String name = userNameView.getText();
        String key = userKeyView.getText();
        if (name != null && key != null) {
            if (key.equals(userKeyReView.getText())) {
                User user = new User();
                user.setUserName(name);
                user.setUserKey(key);
                return user;
            } else {
                SimpleMsgAlert alert = new SimpleMsgAlert("两次密码不符，请重新输入！", getStage());
                alert.show();
            }
        } else {
            SimpleMsgAlert alert = new SimpleMsgAlert("请将信息补全！", getStage());
            alert.show();
        }
        return null;
    }

    private void sendMessageLogin() {
        Message message = new Message(MainController.WHAT_LOGIN);
        message.target(MainController.class);
        message.send();
    }
}
