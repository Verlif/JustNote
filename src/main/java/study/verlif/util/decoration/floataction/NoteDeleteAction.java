package study.verlif.util.decoration.floataction;

import lombok.AllArgsConstructor;
import study.verlif.manager.NoteManger;
import study.verlif.manager.UserManager;
import study.verlif.manager.inner.Message;
import study.verlif.model.Note;
import study.verlif.ui.alert.ConfirmAlertBuilder;
import study.verlif.ui.alert.SimpleMsgAlert;
import study.verlif.ui.stage.common.FloatAction;
import study.verlif.ui.stage.main.MainController;

@AllArgsConstructor
public class NoteDeleteAction implements FloatAction {

    private final Note note;

    @Override
    public String tag() {
        return "删除";
    }

    @Override
    public boolean action() {
        new ConfirmAlertBuilder("确认删除本地与云端的笔记本: " + note.getNoteTitle()) {
            @Override
            public void confirm() {
                if (UserManager.newInstance().isCheckOnline()) {
                    if (!NoteManger.newInstance().deleteOnlineNote(note.getNoteId())) {
                        new SimpleMsgAlert("删除失败", null).show();
                        return;
                    }
                }
                NoteManger.newInstance().deleteLocalNote(note.getNoteId());
                Message message = new Message();
                message.what = Message.What.WHAT_NOTE_UPDATE;
                message.send();
            }
        }.show();
        return false;
    }
}
