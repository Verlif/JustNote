package study.verlif.ui.stage.record.edit;

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
        if (record != null) {
            EditRecordController controller = getController();
            controller.setRecord(record);
        }
    }
    
    @Override
    public String getTag() {
        if (record != null) {
            return super.getTag() + record.getNoteId();
        } else return super.getTag();
    }
}
