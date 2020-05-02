package study.verlif.ui.stage.note.edit;

import study.verlif.manager.NoteManger;
import study.verlif.model.Note;
import study.verlif.ui.stage.base.BaseStage;

public class EditNoteStage extends BaseStage {

    private Note note;
    private NoteManger noteManger;

    public EditNoteStage() {
        noteManger = NoteManger.newInstance();
    }

    public EditNoteStage(Note note) {
        this.note = note;
        noteManger = NoteManger.newInstance();
    }

    @Override
    public String loadResource() {
        return "/fxml/note/editNote.fxml";
    }

    @Override
    public void setAttr() {
        setTitle("编辑笔记本");
        if (note != null) {
            new Thread(() -> {
                EditNoteController controller = getController();
                if (note.getNoteId() != 0) {
                    controller.setNote(noteManger.getNoteById(note.getNoteId()));
                } else controller.setNote(noteManger.getNoteById(note.getNoteIdOL()));
            }).start();
        }
    }

    @Override
    public String getTag() {
        if (note != null) {
            return super.getTag() + note.getNoteId();
        } else return super.getTag();
    }
}
