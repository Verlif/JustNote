package study.verlif.ui.stage.note.show;

import javafx.application.Platform;
import javafx.scene.control.Label;
import study.verlif.manager.StageManager;
import study.verlif.manager.UserManager;
import study.verlif.model.Note;
import study.verlif.model.User;
import study.verlif.ui.stage.account.show.AccountShowStage;
import study.verlif.ui.stage.base.BaseController;
import study.verlif.ui.alert.SimpleMsgAlert;
import study.verlif.ui.stage.note.edit.EditNoteStage;
import study.verlif.util.TimeFormatUtil;

public class ShowNoteController extends BaseController {
    public Label titleView;
    public Label contentView;
    public Label createTimeView;
    public Label updateTimeView;
    public Label ownerView;
    public Label creatorView;

    private Note note;
    private final UserManager userManager;
    private final StageManager stageManager;

    public ShowNoteController() {
        this.userManager = UserManager.newInstance();
        this.stageManager = StageManager.newInstance();
    }

    @Override
    protected void init() {

    }

    public void setNote(Note note) {
        this.note = note;
        fillContent(note);
    }

    /**
     * 将Note实例数据填充至界面
     * @param note  Note实例
     */
    private void fillContent(Note note) {
        if (note != null) {
            Platform.runLater(() -> {
                titleView.setText(note.getNoteTitle());
                contentView.setText(note.getNoteDesc());
                createTimeView.setText(TimeFormatUtil.getDateString(note.getCreateTime()));
                updateTimeView.setText(TimeFormatUtil.getDateString(note.getUpdateTime()));
                // 设置等待数据信息
                ownerView.setText("...");
                creatorView.setText("...");
            });
            User owner = userManager.getUserById(note.getOwnerId());
            if (owner != null) {
                Platform.runLater(() -> ownerView.setText(owner.getUserName()));
            }
            User creator = userManager.getUserById(note.getCreatorId());
            if (creator != null) {
                Platform.runLater(() -> creatorView.setText(creator.getUserName()));
            }
        }
    }

    public void edit() {
        EditNoteStage stage = StageManager.newInstance().getStage(new EditNoteStage(note));
        if (stage.isShowing()) {
            new SimpleMsgAlert("编辑窗口只能同时存在一个，请先关闭编辑窗口。", getStage()).show();
        } else {
            stage.show();
            close();
        }
    }

    public void close() {
        getStage().close();
    }

    public void ownerInfo() {
        User user = userManager.getUserById(note.getOwnerId());
        toUserInfo(user);
    }

    public void creatorInfo() {
        User user = userManager.getUserById(note.getCreatorId());
        toUserInfo(user);
    }

    private void toUserInfo(User user) {
        if (user != null) {
            AccountShowStage stage = stageManager.getStage(new AccountShowStage(user), true);
            stage.show();
        }
    }
}
