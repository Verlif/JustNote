package study.verlif.ui.stage.main;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import org.apache.http.util.TextUtils;
import study.verlif.action.SyncAction;
import study.verlif.manager.NoteManger;
import study.verlif.manager.StageManager;
import study.verlif.manager.UserManager;
import study.verlif.manager.inner.Handler;
import study.verlif.manager.inner.Message;
import study.verlif.model.Note;
import study.verlif.model.User;
import study.verlif.ui.alert.SimpleMsgAlert;
import study.verlif.ui.stage.about.AboutStage;
import study.verlif.ui.stage.account.show.AccountShowStage;
import study.verlif.ui.stage.base.BaseController;
import study.verlif.ui.stage.common.FloatAction;
import study.verlif.ui.stage.common.FloatStage;
import study.verlif.ui.stage.login.LoginStage;
import study.verlif.ui.stage.note.edit.EditNoteStage;
import study.verlif.ui.stage.record.edit.EditRecordStage;
import study.verlif.ui.stage.search.SearchController;
import study.verlif.ui.stage.search.SearchStage;
import study.verlif.ui.stage.setting.network.NwSettingStage;
import study.verlif.ui.stage.setting.software.SwSettingStage;
import study.verlif.util.TextUtil;
import study.verlif.util.TimeFormatUtil;
import study.verlif.util.decoration.RightClick;

import java.util.ArrayList;
import java.util.List;

public class MainController extends BaseController {

    public static final int WHAT_LOGIN = 1;

    public BorderPane root;
    public Menu menuAccount;
    public MenuItem menuLogin_login;
    public MenuItem menuLogin_account;
    public TextField searchArea;
    public ListView<Note> selfNoteListView;
    public ListView<Note> otherNoteListView;
    public ListView<Note> publicNoteListView;
    public ListView<Note.Record> recordListView;
    public TitledPane recordListTitle;
    public Label recordCreateTimeView;
    public Label recordUpdateTimeView;
    public Label recordTitleView;
    public TextArea recordContentView;

    private final StageManager stageManager;
    private final UserManager userManager;
    private final NoteManger noteManger;

    private boolean isPrimaryButton;        // 是否时左键点击，用于右键快捷弹窗
    private Note selectedNote;
    private Note.Record selectedRecord;

    public MainController() {
        stageManager = StageManager.newInstance();
        userManager = UserManager.newInstance();
        noteManger = NoteManger.newInstance();

        new Handler() {
            @Override
            public void handlerMessage(Message message) {
                switch (message.what) {
                    case WHAT_LOGIN: {
                        Platform.runLater(() -> {
                            // 刷新主窗口标题
                            MainStage stage = getStage();
                            stage.refreshTitle();
                            // 重新加载主界面
                            init();
                        });
                        break;
                    }
                    case Message.What.WHAT_NOTE_UPDATE: {
                        // 刷新笔记本列表
                        loadLocalNotes();
                        break;
                    }
                    case Message.What.WHAT_RECORD_UPDATE: {
                        Platform.runLater(() -> {
                            setRecordList(null);
                        });
                        break;
                    }
                }
            }
        };
        isPrimaryButton = true;
    }

    public void init() {
        setNoteList();
        setUserName();

        // 搜索框的Enter反馈
        searchArea.setOnKeyPressed(event -> {
            if (searchArea.isFocused() && event.getCode() == KeyCode.ENTER) {
                search();
            }
        });

        // 创建拖拽响应
//        ListViewHover.OnDragDoneCallBack oddcb = note -> {
//            if (selectedRecord != null && note != null) {
//                // 过滤相同笔记本
//                if (selectedRecord.getNoteId() != note.getNoteId()) {
//                    selectedRecord.setNoteId(note.getNoteId());
//                    if (noteManger.createOrUpdateRecord(selectedRecord)) {
//                        setRecordList(selectedNote);
//                    } else new SimpleMsgAlert("无法移动记录！", getStage()).show();
//                }
//            }
//        };
    }

    public void login() {
        LoginStage loginStage = stageManager.getStage(new LoginStage());
        loginStage.show();
    }

    /**
     * 设置已缓存的用户名，显示于登陆按钮
     */
    private void setUserName() {
        final int sizeLimit = 6;
        // 初始化用户
        User user = userManager.getLocalUser();
        // 避免用户名过长，对用户名进行缩减
        String userName = user.getUserName();
        menuAccount.setText(TextUtil.cutString(userName, sizeLimit));
    }

    /**
     * 设置笔记本列表，从本地缓存中读取
     */
    private void setNoteList() {
        loadLocalNotes();
        loadPublicNotes();
    }

    /**
     * 加载个人的笔记本
     */
    private void loadLocalNotes() {
        new Thread(() -> {
            ArrayList<Note> notes = noteManger.getLocalNoteList();
            ArrayList<Note> selfNotes = new ArrayList<>();
            ArrayList<Note> otherNotes = new ArrayList<>();
            for (Note note : notes) {
                if (note.getOwnerId() == userManager.getLocalUser().getUserId()) {
                    selfNotes.add(note);
                } else if (note.getOwnerId() == 0) {
                    otherNotes.add(note);
                }
            }
            Platform.runLater(() -> {
                setPersonalNotes(selfNotes);
                setOtherNotes(otherNotes);
            });
        }).start();
    }

    /**
     * 设置个人笔记本列表
     *
     * @param notes 设置的笔记本实例列表
     */
    private void setPersonalNotes(List<Note> notes) {
        selfNoteListView.getItems().clear();
        selfNoteListView.getItems().addAll(notes);
        setNoteListListener(selfNoteListView);
    }

    /**
     * 设置其他笔记本列表
     *
     * @param notes 设置的笔记本实例列表
     */
    private void setOtherNotes(List<Note> notes) {
        otherNoteListView.getItems().clear();
        otherNoteListView.getItems().addAll(notes);
        setNoteListListener(otherNoteListView);
    }

    /**
     * 设置笔记本列表鼠标事件
     *
     * @param listView 需要监听的视图
     */
    private void setNoteListListener(ListView<Note> listView) {
        // 开启笔记本列表点击事件
        listView.setOnMouseClicked(event -> {
            isPrimaryButton = event.getButton() != MouseButton.SECONDARY;
            selectedNote = listView.getFocusModel().getFocusedItem();
            if (selectedNote != null) {
                selectNote(selectedNote);
                // 开启选择浮动窗口
                if (!isPrimaryButton) {
                    ArrayList<FloatAction> list;
                    if (selectedNote.getOwnerId() != userManager.getLocalUser().getUserId()) {
                        list = RightClick.getSharedNoteRC(selectedNote);
                    } else list = RightClick.getDefaultNoteRC(selectedNote);
                    FloatStage stage = stageManager.getStage(new FloatStage(list));
                    stage.show();
                }
            }
        });
        // 设置笔记本列表单项选择
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedNote = newValue;
        });
    }


    /**
     * 选择笔记本
     *
     * @param note 选择的笔记本实例
     */
    private void selectNote(Note note) {
        if (note != null) {
            // 将记录列表标题设置为选中笔记本标题
            recordListTitle.setText(note.getNoteTitle());
            // 设置记录列表
            setRecordList(note);
        } else {
            // 将记录列表标题设置为选中笔记本标题
            recordListTitle.setText("记录列表");
            // 设置记录列表
            setRecordList(null);
        }
    }

    /**
     * 加载与当前用户相关的共享笔记本，需要联网并登陆
     */
    private void loadPublicNotes() {
        new Thread(() -> {
            ArrayList<Note> notes = noteManger.getSharedNotes();
            setNoteListListener(publicNoteListView);
            Platform.runLater(() -> {
                publicNoteListView.getItems().clear();
                publicNoteListView.getItems().addAll(notes);
            });
        }).start();
    }

    /**
     * 设置记录列表，从本地缓存中读取
     *
     * @param note 记录列表的上级笔记本
     */
    private void setRecordList(Note note) {
        setRecordInfo(null);
        // 先将列表缓存清除
        recordListView.getItems().clear();
        if (note == null) {
            // 先将列表缓存清除
            recordListTitle.setText("记录列表");
            return;
        }
        new Thread(() -> {
            ArrayList<Note.Record> records;
            if (note.getOwnerId() == 0 || note.getOwnerId() == userManager.getLocalUser().getUserId()) {
                records = noteManger.getLocalNoteRecords(note.getNoteId());
            } else {
                records = noteManger.getOnlineNoteRecords(note.getNoteIdOL());
            }
            Platform.runLater(() -> {
                // 载入当前记录列表
                recordListView.getItems().addAll(records);
                // 点击记录列表项时，设置记录信息到界面
                recordListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    setRecordInfo(newValue);
                });
            });
            // 开启笔记本列表点击事件
            recordListView.setOnMouseClicked(event -> {
                isPrimaryButton = event.getButton() != MouseButton.SECONDARY;
                if (selectedRecord != null) {
                    setRecordInfo(selectedRecord);
                    // 开启选择浮动窗口
                    if (!isPrimaryButton) {
                        ArrayList<FloatAction> list = RightClick.getRecordRC(selectedRecord);
                        FloatStage stage = stageManager.getStage(new FloatStage(list));
                        stage.show();
                    }
                }
            });
            // 设置笔记本列表单项选择
            recordListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedRecord = newValue);
        }).start();
    }

    /**
     * 展示记录信息
     *
     * @param record 被展示的记录实例
     */
    private void setRecordInfo(Note.Record record) {
        if (record != null) {
            recordTitleView.setText(record.getRecordTitle());
            recordCreateTimeView.setText(TimeFormatUtil.getDateString(record.getCreateTime()));
            recordUpdateTimeView.setText(TimeFormatUtil.getDateString(record.getUpdateTime()));
            recordContentView.setText(record.getRecordContent());
        } else {
            recordTitleView.setText("");
            recordCreateTimeView.setText("");
            recordUpdateTimeView.setText("");
            recordContentView.setText("");
        }
    }

    public void checkOnline() {
        if (userManager.isCheckOnline()) {
            menuLogin_account.setVisible(true);
            menuLogin_login.setText("重新登陆");
        } else {
            menuLogin_account.setVisible(false);
            menuLogin_login.setText("登陆");
        }
    }

    /**
     * 显示账户信息
     */
    public void account() {
        User user = userManager.getLocalUser();
        AccountShowStage accountShowStage = stageManager.getStage(new AccountShowStage(user));
        accountShowStage.show();
    }

    public void addNote() {
        EditNoteStage stage = stageManager.getStage(new EditNoteStage(), true);
        stage.show();
    }

    public void addRecord() {
        EditRecordStage stage = stageManager.getStage(new EditRecordStage(null), true);
        stage.show();
    }

    public void sync() {
        if (userManager.isCheckOnline()) {
            SyncAction action = new SyncAction();
            action.start();
        } else {
            new SimpleMsgAlert("请先登陆账号！", getStage()).show();
        }
    }

    public void swSetting() {
        SwSettingStage stage = stageManager.getStage(new SwSettingStage());
        stage.show();
        stage.requestFocus();
    }

    public void nwSetting() {
        NwSettingStage stage = stageManager.getStage(new NwSettingStage());
        stage.show();
        stage.requestFocus();
    }

    public void about() {
        AboutStage stage = stageManager.getStage(new AboutStage());
        stage.show();
        stage.requestFocus();
    }

    /**
     * 搜索
     */
    public void search() {
        String key = searchArea.getText();
        if (!TextUtils.isEmpty(key)) {
            SearchStage searchStage = stageManager.getStage(new SearchStage());
            searchStage.show();
            SearchController controller = searchStage.getController();
            controller.searchArea.setText(key);
            controller.search();
        }
    }
}
