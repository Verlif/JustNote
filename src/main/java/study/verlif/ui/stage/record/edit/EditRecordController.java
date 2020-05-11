package study.verlif.ui.stage.record.edit;

import javafx.scene.control.*;
import org.apache.http.util.TextUtils;
import study.verlif.manager.LocalNotesManager;
import study.verlif.manager.NoteManger;
import study.verlif.manager.StageManager;
import study.verlif.manager.UserManager;
import study.verlif.manager.inner.Message;
import study.verlif.model.Note;
import study.verlif.ui.alert.SimpleMsgAlert;
import study.verlif.ui.stage.base.BaseController;

public class EditRecordController extends BaseController {

    public TextField titleView;
    public TextArea contentView;
    public ListView<Note> noteList;
    public TitledPane noteListTitle;
    public Button saveButton;

    private final LocalNotesManager localNotesManager;
    private final NoteManger noteManger;
    private final UserManager userManager;
    private final StageManager stageManager;

    private Note selectedNote;
    private Note.Record record;

    public EditRecordController() {
        localNotesManager = LocalNotesManager.newInstance();
        noteManger = NoteManger.newInstance();
        userManager = UserManager.newInstance();
        stageManager = StageManager.newInstance();
    }

    @Override
    protected void init() {
        // 新建笔记本选项
        Note note = noteManger.buildANewNote();
        noteList.getItems().add(note);
        // 加载其他笔记本
        noteList.getItems().addAll(localNotesManager.getNoteList());
        // 当前记录保存的笔记本指向选择的笔记本
        noteList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedNote = newValue;
            noteListTitle.setText(newValue.getNoteTitle());
        });
        saveButton.setDisable(true);
        // 屏蔽保存按钮
        titleView.textProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(TextUtils.isEmpty(newValue)));
    }

    public void save() {
        this.record = buildRecord();
        if (this.record != null) {
            noteManger.createOrUpdateRecord(this.record);
            // 提醒主页面更新
            notifyUI();
            cancel();
        }
    }

    public void cancel() {
        getStage().close();
    }

    public void setRecord(Note.Record record) {
        this.record = record;
        // 是否是共享笔记判定
        if (record.getCreatorId() != userManager.getLocalUser().getUserId()) {
            selectedNote = new Note();
            selectedNote.setNoteId(record.getNoteId());
            noteListTitle.setText("共享笔记编辑");
            noteListTitle.setDisable(true);
        }
        fillContent(record);
    }

    private void fillContent(Note.Record record) {
        titleView.setText(record.getRecordTitle());
        contentView.setText(record.getRecordContent());
        for (Note note : noteList.getItems()) {
            if (note.getNoteId() == record.getNoteId()) {
                noteList.getSelectionModel().select(note);
                noteListTitle.setText(note.getNoteTitle());
                break;
            }
        }
    }

    private Note.Record buildRecord() {
        // 判定数据是否完整
        if (TextUtils.isEmpty(titleView.getText())) {
            new SimpleMsgAlert("请将标题补充完整", getStage()).show();
            return null;
        }
        // 判定选择的笔记本
        if (selectedNote == null) {
            new SimpleMsgAlert("请选择一个笔记本用于存放本记录", getStage()).show();
            return null;
        } else if (selectedNote.getNoteId() == 0) {
            // 新建笔记本
            noteManger.createOrUpdateNote(selectedNote);
            notifyUI();
        }
        if (record == null) {
            record = new Note.Record();
            record.setCreatorId(userManager.getLocalUser().getUserId());
        }
        record.setNoteId(selectedNote.getNoteId());
        if (record.getCreatorId() == 0) {
            record.setCreatorId(userManager.getLocalUser().getUserId());
        }
        record.setRecordTitle(titleView.getText());
        record.setRecordContent(contentView.getText());
        return record;
    }

    private void notifyUI() {
        Message message = new Message();
        message.what = Message.What.WHAT_RECORD_UPDATE;
        message.send();
    }
}
