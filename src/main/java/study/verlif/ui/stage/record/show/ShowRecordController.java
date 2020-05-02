package study.verlif.ui.stage.record.show;

import javafx.scene.control.Label;
import study.verlif.manager.NoteManger;
import study.verlif.manager.StageManager;
import study.verlif.manager.UserManager;
import study.verlif.model.Note;
import study.verlif.model.User;
import study.verlif.ui.stage.account.show.AccountShowStage;
import study.verlif.ui.stage.base.BaseController;
import study.verlif.ui.stage.record.edit.EditRecordStage;
import study.verlif.util.TimeFormatUtil;

public class ShowRecordController extends BaseController {

    public Label titleView;
    public Label contentView;
    public Label createTimeView;
    public Label updateTimeView;
    public Label ownerView;
    public Label belongNote;

    private Note.Record record;
    private final UserManager userManager;
    private final NoteManger noteManger;
    private final StageManager stageManager;

    public ShowRecordController() {
        this.userManager = UserManager.newInstance();
        this.noteManger = NoteManger.newInstance();
        this.stageManager = StageManager.newInstance();
    }

    @Override
    protected void init() {

    }

    public void setRecord(Note.Record record) {
        this.record = record;
        fillContent(record);
    }

    private void fillContent(Note.Record record) {
        titleView.setText(record.getRecordTitle());
        contentView.setText(record.getRecordContent());
        createTimeView.setText(TimeFormatUtil.getDateString(record.getCreateTime()));
        updateTimeView.setText(TimeFormatUtil.getDateString(record.getUpdateTime()));
        User user = userManager.getUserById(record.getCreatorId());
        if (user != null) {
            ownerView.setText(user.getUserName());
        } else ownerView.setText("未知");
        Note note = noteManger.getNoteById(record.getNoteId());
        if (note != null) {
            belongNote.setText(note.getNoteTitle());
        } else belongNote.setText("流浪的笔记，没有归属");
    }

    public void edit() {
        EditRecordStage stage = stageManager.getStage(new EditRecordStage(record));
        stage.show();
        close();
    }

    public void close() {
        getStage().close();
    }

    public void ownerInfo() {
        User user = userManager.getUserById(record.getCreatorId());
        if (user != null) {
            AccountShowStage stage = stageManager.getStage(new AccountShowStage(user), true);
            stage.show();
        }
    }
}
