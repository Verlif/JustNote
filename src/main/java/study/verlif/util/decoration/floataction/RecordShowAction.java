package study.verlif.util.decoration.floataction;

import lombok.AllArgsConstructor;
import study.verlif.manager.StageManager;
import study.verlif.model.Note;
import study.verlif.ui.stage.common.FloatAction;
import study.verlif.ui.stage.record.show.ShowRecordStage;

@AllArgsConstructor
public class RecordShowAction implements FloatAction {

    private Note.Record record;

    @Override
    public String tag() {
        return "信息";
    }

    @Override
    public boolean action() {
        ShowRecordStage stage = StageManager.newInstance().getStage(new ShowRecordStage(record));
        if (stage.isShowing()) {
            stage.requestFocus();
        } else stage.show();
        return false;
    }
}
