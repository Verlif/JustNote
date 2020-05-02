package study.verlif.util.decoration.floataction;

import lombok.AllArgsConstructor;
import study.verlif.manager.StageManager;
import study.verlif.model.Note;
import study.verlif.ui.alert.SimpleMsgAlert;
import study.verlif.ui.stage.common.FloatAction;
import study.verlif.ui.stage.record.edit.EditRecordStage;

@AllArgsConstructor
public class RecordEditAction implements FloatAction {

    private Note.Record record;

    @Override
    public String tag() {
        return "编辑";
    }

    @Override
    public boolean action() {
        EditRecordStage stage = StageManager.newInstance().getStage(new EditRecordStage(record));
        if (stage.isShowing()) {
            new SimpleMsgAlert("记录编辑窗口同时只能出现一个，请先关闭编辑窗口", null).show();
        } else stage.show();
        return false;
    }
}
