package study.verlif.ui.stage.base;

import javafx.fxml.Initializable;
import lombok.Data;
import study.verlif.manager.StageManager;

import java.net.URL;
import java.util.ResourceBundle;

@Data
public abstract class BaseController implements Initializable {

    private String tag;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }

    protected abstract void init();

    protected <T extends BaseStage> T getStage() {
        return StageManager.newInstance().getStage(tag);
    }

    protected void close() {
        BaseStage stage = getStage();
        if (stage != null) {
            stage.close();
        }
    }
}
