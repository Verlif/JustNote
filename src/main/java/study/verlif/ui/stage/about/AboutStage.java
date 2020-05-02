package study.verlif.ui.stage.about;

import study.verlif.ui.stage.base.BaseStage;

public class AboutStage extends BaseStage {
    @Override
    public String loadResource() {
        return "/fxml/about.fxml";
    }

    @Override
    public void setAttr() {
        setTitle("关于");
    }
}
