package study.verlif.util.decoration.floataction;

import study.verlif.manager.StageManager;
import study.verlif.model.Note;
import study.verlif.ui.stage.common.FloatAction;
import study.verlif.ui.stage.note.show.ShowNoteStage;

public class NoteShowAction implements FloatAction {

    private Note note;

    public NoteShowAction(Note note) {
        this.note = note;
    }

    @Override
    public String tag() {
        return "信息";
    }

    @Override
    public boolean action() {
        ShowNoteStage stage = StageManager.newInstance().getStage(new ShowNoteStage(note));
        if (stage.isShowing()) {
            stage.requestFocus();
        } else stage.show();
        return false;
    }
}
