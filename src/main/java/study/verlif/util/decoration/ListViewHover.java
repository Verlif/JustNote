package study.verlif.util.decoration;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Font;
import javafx.util.Callback;
import study.verlif.model.Note;

public class ListViewHover {

    public static void setNoteHover(ListView<Note> listView, OnDragDoneCallBack oddcb) {
        listView.setCellFactory(new Callback<ListView<Note>, ListCell<Note>>() {
            @Override
            public ListCell<Note> call(ListView<Note> param) {
                // 标记选择的列表项
                Label label = new Label();
                // 标记选择的笔记本
                final Note[] selectedNote = new Note[1];
                ListCell<Note> cell = new ListCell<Note>() {
                    @Override
                    protected void updateItem(Note item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty && item != null) {
                            selectedNote[0] = item;
                            label.setText(item.toString());
                            setGraphic(label);
                        }
                    }
                };
                cell.setOnDragEntered(event -> {
                    if (selectedNote[0] != null) {
                        param.getFocusModel().focus(param.getItems().indexOf(selectedNote[0]));
                    }
                });
                cell.setOnDragOver(event -> event.acceptTransferModes(TransferMode.MOVE));
                cell.setOnDragDropped(event -> {
                    if (oddcb != null) {
                        oddcb.selectNote(selectedNote[0]);
                    }
                });
                setCell(cell, label);
                return cell;
            }
        });
    }

    public static void setRecordHover(ListView<Note.Record> listView) {
        listView.setCellFactory(new Callback<ListView<Note.Record>, ListCell<Note.Record>>() {
            @Override
            public ListCell<Note.Record> call(ListView<Note.Record> param) {
                Label label = new Label();
                ListCell<Note.Record> cell = new ListCell<Note.Record>() {
                    @Override
                    protected void updateItem(Note.Record item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty && item != null) {
                            label.setText(item.toString());
                            setGraphic(label);
                        }
                    }
                };
                // 开启拖拽
                cell.setOnDragDetected(event -> {
                    Dragboard db = cell.startDragAndDrop(TransferMode.COPY_OR_MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(label.getText());
                    db.setContent(content);
                });
                setCell(cell, label);
                return cell;
            }
        });
    }

    private static void setCell(ListCell<?> cell, Label label) {
        cell.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                label.setFont(new Font(16));
            } else {
                label.setFont(new Font(12));
            }
        });
    }

    public interface OnDragDoneCallBack {
        /**
         * 当记录被拖拽到笔记本时触发。
         * @param note  拖拽目标笔记本
         */
        void selectNote(Note note);
    }
}
