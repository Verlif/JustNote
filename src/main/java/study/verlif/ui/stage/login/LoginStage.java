package study.verlif.ui.stage.login;

import javafx.stage.Modality;
import study.verlif.ui.stage.base.BaseStage;

public class LoginStage extends BaseStage {

    @Override
    public String loadResource() {
        return "/fxml/login.fxml";
    }

    @Override
    public void setAttr() {
        setResizable(false);
        setTitle("登陆");
        initModality(Modality.APPLICATION_MODAL);
    }
}
