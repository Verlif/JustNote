package study.verlif.ui.stage.note.share;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import study.verlif.manager.ShareManager;
import study.verlif.manager.StageManager;
import study.verlif.manager.UserManager;
import study.verlif.manager.inner.Handler;
import study.verlif.manager.inner.Message;
import study.verlif.model.Note;
import study.verlif.model.Share;
import study.verlif.model.User;
import study.verlif.ui.alert.ConfirmAlertBuilder;
import study.verlif.ui.stage.base.BaseController;
import study.verlif.ui.stage.share.EditShareStage;
import study.verlif.util.TimeFormatUtil;

import java.util.List;

public class ShareNoteController extends BaseController {

    public BorderPane root;
    public Label noteNameView;
    public ListView<VBox> listView;
    public ProgressIndicator progress;
    public Label progressDesc;

    private Note note;
    private List<Share> shareList;

    private final UserManager userManager;
    private final ShareManager shareManager;
    private final StageManager stageManager;

    public ShareNoteController() {
        userManager = UserManager.newInstance();
        shareManager = ShareManager.newInstance();
        stageManager = StageManager.newInstance();

        new Handler() {
            @Override
            public void handlerMessage(Message message) {
                if (message.what == Message.What.WHAT_SHARE_UPDATE) {
                    refreshList(note.getNoteIdOL());
                }
            }
        };
    }

    @Override
    protected void init() {
    }

    public void setNote(Note note) {
        this.note = note;
        noteNameView.setText(note.getNoteTitle());
        refreshList(note.getNoteIdOL());
    }

    private void fillContent(List<Share> shareList) {
        double pro = 1.0 / shareList.size();
        progress.setVisible(true);
        listView.getItems().clear();
        for (Share s : shareList) {
            VBox vBox = new VBox();
            ObservableList<Node> children = vBox.getChildren();
            // 创建时间标签
            Label time = new Label(TimeFormatUtil.getDateString(s.getCreateTime()));
            VBox.setMargin(time, new Insets(3));
            children.add(time);
            // 创建共享类型说明
            Label des = new Label(s.getShareDesc());
            VBox.setMargin(des, new Insets(3));
            children.add(des);
            // 创建共享人列表
            // 当共享人数大于3时，使用TitlePane收纳
            if (s.getUserList().size() > 3) {
                TitledPane userList = new TitledPane();
                userList.setText("共享人列表");
                userList.setExpanded(true);
                // 设置列表
                ListView<String> list = new ListView<>();
                for (int userId : s.getUserList()) {
                    User user = userManager.getUserById(userId);
                    if (user != null) {
                        list.getItems().add(user.getUserName());
                    } else list.getItems().add("未知用户");
                }
                userList.setContent(list);
                VBox.setMargin(userList, new Insets(3));
                children.add(userList);
            } else {
                for (int userId : s.getUserList()) {
                    User user = userManager.getUserById(userId);
                    Label userName;
                    if (user != null) {
                        userName = new Label(user.getUserName());
                    } else userName = new Label("未知用户");
                    VBox.setMargin(userName, new Insets(3, 3, 3, 10));
                    children.add(userName);
                }
            }
            // 创建共享记录的操作按钮
            HBox hBox = new HBox();
            Button modifyButton = new Button("编辑");
            modifyButton.setOnMouseClicked(event -> modifyShare(s));
            HBox.setMargin(modifyButton, new Insets(3));
            Button deleteButton = new Button("删除");
            deleteButton.setOnMouseClicked(event -> deleteShare(s));
            HBox.setMargin(deleteButton, new Insets(3));
            hBox.getChildren().addAll(modifyButton, deleteButton);
            vBox.getChildren().add(hBox);

            Platform.runLater(() -> {
                listView.getItems().add(vBox);
                progress.setProgress(progress.getProgress() + pro);
            });
        }
        Platform.runLater(() -> {
            progress.setVisible(false);
            progressDesc.setText("OK");
        });
    }

    private void refreshList(int noteIdOL) {
        if (userManager.isCheckOnline()) {
            // 获取共享列表
            new Thread(() -> {
                shareList = shareManager.getNoteShareList(noteIdOL);
                Platform.runLater(() -> fillContent(shareList));
            }).start();
        } else {
            progressDesc.setText("Fail to connect server!");
        }
    }

    private void modifyShare(Share share) {
        EditShareStage stage = stageManager.getStage(new EditShareStage());
        stage.setShare(share);
        stage.show();
    }

    private void deleteShare(Share share) {
        new ConfirmAlertBuilder("确定删除此共享记录？") {
            @Override
            public void confirm() {
                shareManager.deleteShare(share.getShareId());
                refreshList(note.getNoteIdOL());
            }
        }.show();
    }

    public void addShare() {
        EditShareStage stage = stageManager.getStage(new EditShareStage());
        stage.setNote(note);
        stage.show();
    }
}
