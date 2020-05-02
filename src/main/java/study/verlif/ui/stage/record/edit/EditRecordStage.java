package study.verlif.ui.stage.record.edit;

import javafx.application.Platform;
import study.verlif.manager.NoteManger;
import study.verlif.model.Note;
import study.verlif.ui.stage.base.BaseStage;

public class EditRecordStage extends BaseStage {

    private Note.Record record;

    public EditRecordStage(Note.Record record) {
        this.record = record;
    }

    @Override
    public String loadResource() {
        return "/fxml/record/editRecord.fxml";
    }

    @Override
    public void setAttr() {
        setTitle("编辑记录");
        new Thread(() -> {
            EditRecordController controller = getController();
            if (record != null) {
                Note.Record temp;
                if (record.getRecordId() != 0) {
                    temp = NoteManger.newInstance().getRecordById(record.getRecordId());
                    if (temp != null) {
                        Platform.runLater(() -> controller.setRecord(temp));
                    }
                } else if (record.getRecordIdOL() != 0) {
                    temp = NoteManger.newInstance().getRecordById(record.getRecordIdOL());
                    if (temp != null) {
                        Platform.runLater(() -> controller.setRecord(temp));
                    }
                } else Platform.runLater(() -> controller.setRecord(record));
            }
        }).start();
    }
    
    @Override
    public String getTag() {
        if (record != null) {
            return super.getTag() + record.getNoteId();
        } else return super.getTag();
    }
}
