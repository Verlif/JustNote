package study.verlif.ui.stage.search;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import org.apache.http.util.TextUtils;
import study.verlif.manager.NoteManger;
import study.verlif.manager.StageManager;
import study.verlif.manager.inner.Handler;
import study.verlif.manager.inner.Message;
import study.verlif.model.Note;
import study.verlif.ui.stage.base.BaseController;
import study.verlif.ui.stage.record.edit.EditRecordStage;
import study.verlif.ui.stage.record.show.ShowRecordStage;
import study.verlif.ui.waiting.WaitStage;
import study.verlif.util.ConsoleUtil;
import study.verlif.util.decoration.floataction.RecordDeleteAction;

import java.util.ArrayList;
import java.util.List;

public class SearchController extends BaseController {

    public TextField searchArea;
    public VBox noteListView;
    public ListView<Note.Record> recordListView;
    public TextArea recordContentView;

    private Note.Record selectedRecord;

    private final NoteManger noteManger;
    private final StageManager stageManager;

    public SearchController() {
        noteManger = NoteManger.newInstance();
        stageManager = StageManager.newInstance();

        new Handler() {
            @Override
            public void handlerMessage(Message message) {
                if (message.what == Message.What.WHAT_RECORD_UPDATE
                        || message.what == Message.What.WHAT_NOTE_UPDATE) {
                    Platform.runLater(() -> search());
                }
            }
        };
    }

    @Override
    protected void init() {
        searchArea.setOnKeyPressed(event -> {
            if (searchArea.isFocused() && event.getCode() == KeyCode.ENTER) {
                search();
            }
        });
    }

    public void search() {
        selectRecord(null);
        String key = searchArea.getText();
        if (!TextUtils.isEmpty(key)) {
            WaitStage stage = stageManager.getStage(new WaitStage(getStage()));
            stage.show();
            new Thread(() -> {
                List<Note> notes = noteManger.getNotesByKey(key);
                List<Note.Record> records = noteManger.getRecordsByKey(key);

                Platform.runLater(() -> {
                    setNoteList(notes);
                    setRecordList(records);
                    stage.close();
                });
            }).start();
        }
    }

    private void setNoteList(List<Note> notes) {
        ObservableList<Node> list = noteListView.getChildren();
        list.clear();
        for (Note n : notes) {
            TitledPane titledPane = new TitledPane();
            titledPane.setText(n.getNoteTitle());

            ListView<Note.Record> recordListView = new ListView<>();
            ArrayList<Note.Record> records = noteManger.getNoteRecords(n.getNoteId());
            recordListView.getItems().addAll(records);
            setListViewListener(recordListView);
            titledPane.setExpanded(false);
            titledPane.setContent(recordListView);
            list.add(titledPane);
        }
    }

    private void setRecordList(List<Note.Record> records) {
        ObservableList<Note.Record> list = recordListView.getItems();
        list.clear();
        list.addAll(records);
        setListViewListener(recordListView);
    }

    private void setListViewListener(ListView<Note.Record> listView) {
        listView.setOnMouseClicked(event -> {
            selectRecord(listView.getSelectionModel().getSelectedItem());
        });
    }

    private void selectRecord(Note.Record record) {
        selectedRecord = record;
        if (record != null) {
            recordContentView.setText(record.getRecordContent());
        } else {
            recordContentView.setText("");
        }
    }

    public void info() {
        if (selectedRecord != null) {
            ShowRecordStage stage = stageManager.getStage(new ShowRecordStage(selectedRecord));
            stage.show();
            stage.requestFocus();
        }
    }

    public void modify() {
        if (selectedRecord != null) {
            EditRecordStage stage = stageManager.getStage(new EditRecordStage(selectedRecord));
            stage.show();
            stage.requestFocus();
        }
    }

    public void delete() {
        if (selectedRecord != null) {
            RecordDeleteAction action = new RecordDeleteAction(selectedRecord);
            action.action();
        }
    }
}
