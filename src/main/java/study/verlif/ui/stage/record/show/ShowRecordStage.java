package study.verlif.ui.stage.record.show;

import lombok.AllArgsConstructor;
import study.verlif.model.Note;
import study.verlif.ui.stage.base.BaseStage;

@AllArgsConstructor
public class ShowRecordStage extends BaseStage {

    private Note.Record record;

    @Override
    public String loadResource() {
        return "/fxml/record/showRecord.fxml";
    }

    @Override
    public void setAttr() {
        setTitle("记录信息");
        ShowRecordController controller = getController();
        controller.setRecord(record);
    }

    @Override
    public String getTag() {
        return "r" + record.getRecordId();
    }
}
