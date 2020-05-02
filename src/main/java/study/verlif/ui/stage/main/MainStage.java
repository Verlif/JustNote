package study.verlif.ui.stage.main;

import study.verlif.manager.UserManager;
import study.verlif.ui.stage.base.BaseStage;

public class MainStage extends BaseStage {

    private final UserManager userManager;

    public MainStage() {
        userManager = UserManager.newInstance();
    }

    public String loadResource() {
        return "/fxml/main.fxml";
    }

    @Override
    public void setAttr() {
        refreshTitle();
    }

    public void refreshTitle() {
        if (userManager.isCheckOnline()) {
            setTitle(userManager.getLocalUser().getUserName() + " 的笔记本");
        } else setTitle("离线笔记本");
    }
}
