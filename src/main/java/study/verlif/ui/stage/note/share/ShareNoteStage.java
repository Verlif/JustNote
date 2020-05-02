package study.verlif.ui.stage.note.share;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import study.verlif.model.Note;
import study.verlif.ui.stage.base.BaseStage;

@RequiredArgsConstructor
public class ShareNoteStage extends BaseStage {

    @NonNull private final Note note;

    @Override
    public String loadResource() {
        return "/fxml/note/shareNote.fxml";
    }

    @Override
    public void setAttr() {
        setTitle("共享笔记本设置");
        ShareNoteController controller = getController();
        controller.setNote(note);
    }
}
