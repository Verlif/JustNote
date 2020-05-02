package study.verlif.ui.stage.share;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.apache.http.util.TextUtils;
import study.verlif.manager.NoteManger;
import study.verlif.manager.ShareManager;
import study.verlif.manager.StageManager;
import study.verlif.manager.inner.Handler;
import study.verlif.manager.inner.Message;
import study.verlif.model.Note;
import study.verlif.model.Share;
import study.verlif.ui.alert.SimpleMsgAlert;
import study.verlif.ui.stage.base.BaseController;
import study.verlif.ui.waiting.WaitStage;
import study.verlif.util.ConsoleUtil;

import java.util.ArrayList;
import java.util.List;

public class EditShareController extends BaseController {

    public Label noteName;
    public ChoiceBox<String> shareType;
    public ListView<HBox> usersBox;
    public TextField userAddArea;
    public Button saveButton;

    private Note note;
    private Share share;
    private List<HBox> userList;

    private final NoteManger noteManger;
    private final ShareManager shareManager;

    public EditShareController() {
        noteManger = NoteManger.newInstance();
        shareManager = ShareManager.newInstance();
        share = new Share();
        userList = new ArrayList<>();
    }

    @Override
    protected void init() {
        ObservableList<String> list = shareType.getItems();
        list.addAll("可读", "可写");
        shareType.getSelectionModel().select(0);
        saveButton.setDisable(usersBox.getItems().size() == 0);
        usersBox.getItems().addListener((ListChangeListener<HBox>) c -> saveButton.setDisable(c.getList().size() == 0));
    }

    public void setShare(Share share) {
        this.share = share;
        Note note = noteManger.getNoteById(share.getNoteId());
        if (note != null) {
            setNote(note);
            shareType.getSelectionModel().select(share.getShareDesc());
            for (int userId : share.getUserList()) {
                addUser(userId);
            }
        } else {
            new SimpleMsgAlert("未能查询到相关笔记本，请检查网络！", getStage()).showAndWait();
            close();
        }
    }

    public void setNote(Note note) {
        this.note = note;
        share.setNoteId(note.getNoteIdOL());
        noteName.setText(note.getNoteTitle());
    }

    public void addUser(int userId) {
        // 创建用户显示视图
        HBox hBox = new HBox();
        Label label = new Label(userId + "");
        label.setPrefWidth(300);
        HBox.setMargin(label, new Insets(3));
        Button button = new Button("删除");
        button.setAlignment(Pos.CENTER_RIGHT);
        HBox.setMargin(button, new Insets(3));
        button.setOnMouseClicked(event -> {
            removeUser(hBox, userId);
        });
        hBox.getChildren().addAll(label, button);
        userList.add(hBox);
        usersBox.getItems().add(hBox);
    }

    public void removeUser(HBox h, int userId) {
        share.getUserList().remove(new Integer(userId));
        userList.remove(h);
        usersBox.getItems().remove(h);
    }

    public void newUser() {
        if (!TextUtils.isEmpty(userAddArea.getText())) {
            try {
                int userId = Integer.parseInt(userAddArea.getText());
                share.getUserList().add(userId);
                addUser(userId);
                userAddArea.setText("");
            } catch (NumberFormatException e) {
                new SimpleMsgAlert("仅限用户纯数字id", getStage()).show();
            }
        }
    }

    public void save() {
        WaitStage waitStage = StageManager.newInstance().getStage(new WaitStage(getStage()));
        waitStage.show();
        new Thread(() -> {
            share.setShareType(shareType.getSelectionModel().getSelectedItem().equals("可读") ? Share.TYPE_READ : Share.TYPE_WRITE);
            boolean result = shareManager.addOrModifyShare(share);
            Platform.runLater(() -> {
                waitStage.close();
                if (result) {
                    notifyUI();
                    close();
                } else {
                    new SimpleMsgAlert("编辑共享设置失败，请检查网络。", getStage()).show();
                }
            });
        }).start();
    }

    public void cancel() {
        close();
    }

    private void notifyUI() {
        Message message = new Message();
        message.what = Message.What.WHAT_SHARE_UPDATE;
        message.send();
    }
}
