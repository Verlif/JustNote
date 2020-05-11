package study.verlif.database.online;

import study.verlif.database.online.intfac.OnlineRecordDBC;
import study.verlif.manager.CSMsgManager;
import study.verlif.model.Note;
import study.verlif.model.Result;

import java.util.ArrayList;
import java.util.Date;

public class OnlineRecordDBCImpl implements OnlineRecordDBC {

    private final CSMsgManager csMsgManager;

    public OnlineRecordDBCImpl() {
        csMsgManager = CSMsgManager.newInstance();
    }

    @Override
    public Note.Record getRecordById(int recordIdOL) {
        Result result = csMsgManager.doGetRecordById(recordIdOL);
        if (result.ok()) {
            return result.getDataObject(Note.Record.class);
        } else return null;
    }

    @Override
    public ArrayList<Note.Record> getNoteRecords(int noteIdOL) {
        Result result = csMsgManager.doGetNoteRecords(noteIdOL);
        if (result.ok()) {
            return result.getDataList("records", Note.Record.class);
        } else return new ArrayList<>();
    }

    @Override
    public boolean saveOrModifyRecord(Note.Record record) {
        Result result;
        if (record.getRecordIdOL() == 0) {
            result = csMsgManager.doSaveRecord(record);
        } else result = csMsgManager.doModifyRecord(record);
        if (result.ok()) {
            Note.Record resultRecord = result.getDataObject(Note.Record.class);
            record.setRecordIdOL(resultRecord.getRecordIdOL());
            record.setCreateTime(resultRecord.getCreateTime());
            record.setUpdateTime(resultRecord.getUpdateTime());
            return true;
        } else return false;
    }

    @Override
    public boolean deleteRecord(int recordId) {
        Result result = csMsgManager.doDeleteRecord(recordId);
        return result.ok();
    }

}
