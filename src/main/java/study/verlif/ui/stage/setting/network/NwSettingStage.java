package study.verlif.ui.stage.setting.network;

import study.verlif.ui.stage.base.BaseStage;

public class NwSettingStage extends BaseStage {

    @Override
    public String loadResource() {
        return "/fxml/setting/nwSetting.fxml";
    }

    @Override
    public void setAttr() {
        setTitle("网络设置");
    }
}
