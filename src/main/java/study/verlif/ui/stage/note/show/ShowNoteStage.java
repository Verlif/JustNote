package study.verlif.ui.stage.note.show;

import study.verlif.manager.NoteManger;
import study.verlif.model.Note;
import study.verlif.ui.stage.base.BaseStage;

public class ShowNoteStage extends BaseStage {

    private Note note;
    private NoteManger noteManger;

    public ShowNoteStage(Note note) {
        this.note = note;
        noteManger = NoteManger.newInstance();
    }

    @Override
    public String loadResource() {
        return "/fxml/note/showNote.fxml";
    }

    @Override
    public void setAttr() {
        setTitle("笔记本信息");
        new Thread(() -> {
            ShowNoteController controller = getController();
            if (note.getNoteId() != 0) {
                controller.setNote(noteManger.getNoteById(note.getNoteId()));
            } else controller.setNote(noteManger.getNoteById(note.getNoteIdOL()));
        }).start();
    }

    @Override
    public String getTag() {
        return super.getTag() + note.getNoteId();
    }
}
