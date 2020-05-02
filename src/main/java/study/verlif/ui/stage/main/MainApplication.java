package study.verlif.ui.stage.main;

import study.verlif.manager.StageManager;
import study.verlif.ui.stage.base.BaseApplication;
import study.verlif.ui.stage.base.BaseStage;
import study.verlif.ui.stage.pre.PreLoginStage;

public class MainApplication extends BaseApplication {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public BaseStage getStage() {
        return StageManager.newInstance().getStage(new PreLoginStage());
    }
}
