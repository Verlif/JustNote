package study.verlif.ui.progress;

import javafx.application.Platform;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import study.verlif.ui.stage.base.BaseStage;

public class ProgressStage extends BaseStage {

    public ProgressStage(Window window) {
        if (window != null) {
            initOwner(window);
        }
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNIFIED);
    }

    @Override
    public String loadResource() {
        return "/fxml/progress.fxml";
    }

    @Override
    public void setAttr() {
        setTitle("进度");
        initModality(Modality.WINDOW_MODAL);
    }

    public void setProgress(double progress) {
        ProgressController controller = getController();
        Platform.runLater(() -> controller.setProgress(progress));
    }

    public void setProgressTitle(String title) {
        ProgressController controller = getController();
        controller.setTitle(title);
    }
}
