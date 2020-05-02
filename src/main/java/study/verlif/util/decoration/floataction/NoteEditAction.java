package study.verlif.util.decoration.floataction;

import study.verlif.manager.StageManager;
import study.verlif.model.Note;
import study.verlif.ui.stage.common.FloatAction;
import study.verlif.ui.stage.note.edit.EditNoteStage;

public class NoteEditAction implements FloatAction {

    private StageManager stageManager;
    private Note note;

    public NoteEditAction(Note note) {
        this.stageManager = StageManager.newInstance();
        this.note = note;
    }

    @Override
    public String tag() {
        return "编辑";
    }

    @Override
    public boolean action() {
        EditNoteStage stage = stageManager.getStage(new EditNoteStage(note));
        stage.show();
        stage.requestFocus();
        return false;
    }
}
