package study.verlif.ui.stage.share;

import study.verlif.model.Note;
import study.verlif.model.Share;
import study.verlif.ui.stage.base.BaseStage;

public class EditShareStage extends BaseStage {

    @Override
    public String loadResource() {
        return "/fxml/share/editShare.fxml";
    }

    @Override
    public void setAttr() {
        setTitle("共享编辑");
        setResizable(false);
    }

    public void setShare(Share share) {
        EditShareController controller = getController();
        controller.setShare(share);
    }

    public void setNote(Note note) {
        EditShareController controller = getController();
        controller.setNote(note);
    }
}
