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
import study.verlif.util.ConsoleUtil;

@AllArgsConstructor
public class RecordDeleteAction implements FloatAction {

    private Note.Record record;

    @Override
    public String tag() {
        return "删除";
    }

    @Override
    public boolean action() {
        new ConfirmAlertBuilder("确认删除本地与云端的记录: " + record.getRecordTitle()) {
            @Override
            public void confirm() {
                if (UserManager.newInstance().isCheckOnline()) {
                    if (!NoteManger.newInstance().deleteOnlineRecord(record.getRecordId())) {
                        new SimpleMsgAlert("云端删除失败", null).show();
                        return;
                    }
                }
                NoteManger.newInstance().deleteLocalRecord(record.getRecordId());
                Message message = new Message();
                message.what = Message.What.WHAT_RECORD_UPDATE;
                message.send();
            }
        }.show();
        return false;
    }
}
