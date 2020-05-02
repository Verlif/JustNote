package study.verlif.ui.stage.note.edit;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.http.util.TextUtils;
import study.verlif.manager.NoteManger;
import study.verlif.manager.StageManager;
import study.verlif.manager.UserManager;
import study.verlif.manager.inner.Message;
import study.verlif.model.Note;
import study.verlif.ui.stage.base.BaseController;
import study.verlif.ui.stage.main.MainController;
import study.verlif.ui.alert.SimpleMsgAlert;

public class EditNoteController extends BaseController {

    public TextField titleView;
    public TextArea contentView;
    public Button saveButton;

    private final UserManager userManager;
    private final NoteManger noteManger;
    private StageManager stageManager;

    private Note note;

    public EditNoteController() {
        userManager = UserManager.newInstance();
        noteManger = NoteManger.newInstance();
        stageManager = StageManager.newInstance();
    }

    @Override
    protected void init() {
        saveButton.setDisable(true);
        // 屏蔽保存按钮
        titleView.textProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(TextUtils.isEmpty(newValue)));
    }

    public void save() {
        Note note = buildThisNote();
        if (note != null) {
            noteManger.createOrUpdateNote(note);
            // 提醒主界面更新
            notifyUI();
            cancel();
        }
    }

    /**
     * 将界面输入传入构建Note实例
     * @return  Note实例
     */
    private Note buildThisNote() {
        if (TextUtils.isEmpty(titleView.getText())) {
            new SimpleMsgAlert("请输入笔记本名", getStage()).show();
            return null;
        }
        if (this.note == null) {
            note = new Note();
            note.setOwnerId(userManager.getLocalUser().getUserId());
            note.setCreatorId(note.getOwnerId());
        }
        note.setNoteTitle(titleView.getText());
        note.setNoteDesc(contentView.getText());
        return note;
    }
    
    public void setNote(Note note) {
        this.note = note;
        titleView.setText(note.getNoteTitle());
        contentView.setText(note.getNoteDesc());
    }

    public void cancel() {
        getStage().close();
    }

    private void notifyUI() {
        Message message = new Message();
        message.what = Message.What.WHAT_NOTE_UPDATE;
        message.send();
    }
}
