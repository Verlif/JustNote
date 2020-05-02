package study.verlif.ui.stage.account.show;

import study.verlif.model.User;
import study.verlif.ui.stage.base.BaseStage;

public class AccountShowStage extends BaseStage {

    private User user;

    public AccountShowStage(User user) {
        this.user = user;
    }

    @Override
    public String loadResource() {
        return "/fxml/account/accountShow.fxml";
    }

    @Override
    public void setAttr() {
        setTitle("账户信息");
        AccountShowController controller = getController();
        controller.setUser(user);
    }
}
