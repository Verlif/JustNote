package study.verlif.ui.stage.register;

import study.verlif.ui.stage.base.BaseStage;

public class RegisterStage extends BaseStage {

    @Override
    public String loadResource() {
        return "/fxml/register.fxml";
    }

    @Override
    public void setAttr() {
        setTitle("注册");
    }
}
