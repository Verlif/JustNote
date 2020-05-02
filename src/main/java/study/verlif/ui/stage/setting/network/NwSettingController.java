package study.verlif.ui.stage.setting.network;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import study.verlif.manager.SettingManager;
import study.verlif.manager.StageManager;
import study.verlif.ui.stage.base.BaseController;

public class NwSettingController extends BaseController {

    public ChoiceBox<String> httpType;
    public TextField ip;

    private SettingManager settingManager;

    public NwSettingController() {
        settingManager = SettingManager.newInstance();
    }

    @Override
    protected void init() {
        SettingManager.Setting setting = settingManager.getSetting();
        httpType.getItems().addAll("http://", "https://");
        httpType.getSelectionModel().select(setting.getHttpType());
        ip.setText(settingManager.getSetting().ip.replace("http://", "").replace("https://", ""));
    }

    public void save() {
        buildSetting();
        settingManager.saveSetting();
        close();
    }

    private void buildSetting() {
        settingManager.getSetting().setIp(httpType.getValue() + ip.getText());
    }

    public void close() {
        NwSettingStage stage = StageManager.newInstance().getStage(new NwSettingStage());
        stage.close();
    }
}
