package study.verlif.ui.waiting;

import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import study.verlif.ui.stage.base.BaseStage;

public class WaitStage extends BaseStage {

    public WaitStage(Window owner) {
        initOwner(owner);
        setX(owner.getX() + owner.getWidth() / 2 - 120);
        setY(owner.getY() + owner.getHeight() / 2 - 50);
    }

    @Override
    public String loadResource() {
        return "/fxml/waiting.fxml";
    }

    @Override
    public void setAttr() {
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);
    }
}
