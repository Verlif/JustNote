package study.verlif.ui.stage.login;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import study.verlif.action.LoginAction;
import study.verlif.action.RegisterAction;
import study.verlif.action.SyncAction;
import study.verlif.manager.StageManager;
import study.verlif.manager.UserManager;
import study.verlif.manager.inner.Message;
import study.verlif.model.User;
import study.verlif.ui.stage.base.BaseController;
import study.verlif.ui.stage.main.MainController;
import study.verlif.ui.alert.SimpleMsgAlert;
import study.verlif.ui.stage.main.MainStage;
import study.verlif.ui.stage.register.RegisterStage;
import study.verlif.ui.waiting.WaitStage;
import study.verlif.util.ConsoleUtil;

public class LoginController extends BaseController {

    public VBox root;
    public Button buttonLogin;
    public Button buttonRegister;
    public TextField userNameView;
    public TextField userKeyView;

    private final StageManager stageManager;
    private final UserManager userManager;

    public LoginController() {
        stageManager = StageManager.newInstance();
        userManager = UserManager.newInstance();
    }

    protected void init() {
        User user = userManager.getLocalUser();
        if (user.getUserId() != 0) {
            userNameView.setText(user.getUserName());
            userKeyView.setText(user.getUserKey());
        }
    }

    public void login() {
        User user = getUserInfo();
        if (user != null) {
            WaitStage waitStage = stageManager.getStage(new WaitStage(root.getScene().getWindow()));
            LoginAction loginAction = new LoginAction(user) {
                @Override
                public void getResult(boolean ifLogin) {
                    SimpleMsgAlert simpleMsgAlert;
                    if (ifLogin) {
                        sendMessageLogin();
                        simpleMsgAlert = new SimpleMsgAlert("登陆成功", getStage()) {
                            @Override
                            protected void confirm() {
                                // 登陆成功后关闭登陆窗口
                                getStage().close();
                                // 登陆后自动同步一次
                                new SyncAction().start();
                            }
                        };
                    } else {
                        simpleMsgAlert = new SimpleMsgAlert("登陆失败，请检查账户与密码是否正确", getStage());
                    }
                    simpleMsgAlert.show();
                    waitStage.close();
                }

                @Override
                public void onStop() {
                    waitStage.close();
                }
            };
            waitStage.show();
            loginAction.start();
        }
    }

    public void register() {
        RegisterStage stage = stageManager.getStage(new RegisterStage());
        stage.show();
        close();
    }

    /**
     * 获取输入的用户信息
     * @return  用户实例。当信息不足时，返回null，并显示提示弹窗。
     */
    private User getUserInfo() {
        String userName = userNameView.getText();
        String userKey = userKeyView.getText();
        if (!userName.equals("") && !userKey.equals("")) {
            return new User(userName, userKey);
        } else {
            SimpleMsgAlert simpleMsgAlert = new SimpleMsgAlert("请将信息补充完整", getStage());
            simpleMsgAlert.showAndWait();
            return null;
        }
    }

    private void sendMessageLogin() {
        Message message = new Message(MainController.WHAT_LOGIN);
        message.target(MainController.class);
        message.send();
    }
}
