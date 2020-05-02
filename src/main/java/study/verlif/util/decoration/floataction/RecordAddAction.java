package study.verlif.util.decoration.floataction;

import study.verlif.manager.StageManager;
import study.verlif.model.Note;
import study.verlif.ui.stage.common.FloatAction;
import study.verlif.ui.stage.record.edit.EditRecordStage;

public class RecordAddAction implements FloatAction {

    private Note note;

    public RecordAddAction(Note note) {
        this.note = note;
    }

    @Override
    public String tag() {
        return "新建记录";
    }

    @Override
    public boolean action() {
        Note.Record record = new Note.Record();
        record.setNoteId(note.getNoteId());
        EditRecordStage stage = StageManager.newInstance().getStage(new EditRecordStage(record), true);
        stage.show();
        return false;
    }
}
