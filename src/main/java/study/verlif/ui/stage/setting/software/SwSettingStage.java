package study.verlif.ui.stage.setting.software;

import study.verlif.ui.stage.base.BaseStage;

public class SwSettingStage extends BaseStage {

    @Override
    public String loadResource() {
        return "/fxml/setting/swSetting.fxml";
    }

    @Override
    public void setAttr() {
        setTitle("软件设置");
    }
}
