package study.verlif.ui.stage.pre;

import javafx.event.EventHandler;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import study.verlif.action.LoginAction;
import study.verlif.manager.StageManager;
import study.verlif.manager.UserManager;
import study.verlif.model.User;
import study.verlif.ui.alert.SimpleMsgAlert;
import study.verlif.ui.stage.base.BaseStage;
import study.verlif.ui.stage.main.MainStage;
import study.verlif.util.ConsoleUtil;

public class PreLoginStage extends BaseStage {

    @Override
    public String loadResource() {
        return "/fxml/preLogin.fxml";
    }

    @Override
    public void setAttr() {
        setTitle("登陆");
        initModality(Modality.APPLICATION_MODAL);
        setOnShowing(event -> {
            User user = UserManager.newInstance().getLocalUser();
            if (user.getUserId() != 0) {
                LoginAction action = new LoginAction(user) {
                    @Override
                    public void getResult(boolean ifLogin) {
                        if (ifLogin) {
                            if (isShowing()) {
                                toMainStage();
                            }
                            close();
                        } else {
                            SimpleMsgAlert alert = new SimpleMsgAlert("登陆失败，请检查网络或用户信息是否完整！", null) {
                                @Override
                                protected void confirm() {
                                    toMainStage();
                                    close();
                                }
                            };
                            alert.show();
                        }
                    }
                };
                action.start();
            } else {
                close();
                toMainStage();
            }
        });
    }

    private void toMainStage() {
        StageManager.newInstance().getStage(new MainStage()).show();
    }
}
