package study.verlif.util.decoration.floataction;

import lombok.AllArgsConstructor;
import study.verlif.manager.StageManager;
import study.verlif.model.Note;
import study.verlif.ui.alert.SimpleMsgAlert;
import study.verlif.ui.stage.common.FloatAction;
import study.verlif.ui.stage.note.share.ShareNoteStage;

@AllArgsConstructor
public class NoteShareAction implements FloatAction {

    private Note note;

    @Override
    public String tag() {
        return "共享设置";
    }

    @Override
    public boolean action() {
        ShareNoteStage stage = StageManager.newInstance().getStage(new ShareNoteStage(note), true);
        if (stage.isShowing()) {
            new SimpleMsgAlert("请先关闭另一个共享设置窗口。", null).show();
        } else stage.show();
        return false;
    }
}
