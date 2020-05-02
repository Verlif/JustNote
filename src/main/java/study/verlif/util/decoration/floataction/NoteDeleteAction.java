package study.verlif.util.decoration.floataction;

import lombok.AllArgsConstructor;
import study.verlif.manager.NoteManger;
import study.verlif.manager.inner.Message;
import study.verlif.model.Note;
import study.verlif.ui.alert.ConfirmAlertBuilder;
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
                NoteManger.newInstance().deleteLocalNote(note.getNoteId());
                NoteManger.newInstance().deleteOnlineNote(note.getNoteIdOL());
                Message message = new Message();
                message.what = Message.What.WHAT_NOTE_UPDATE;
                message.send();
            }
        }.show();
        return false;
    }
}
