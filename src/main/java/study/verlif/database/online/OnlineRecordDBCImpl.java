package study.verlif.database.online;

import study.verlif.database.online.intfac.OnlineRecordDBC;
import study.verlif.manager.CSMsgManager;
import study.verlif.model.Note;
import study.verlif.model.Result;

import java.util.ArrayList;
import java.util.Date;

public class OnlineRecordDBCImpl implements OnlineRecordDBC {

    private CSMsgManager csMsgManager;

    public OnlineRecordDBCImpl() {
        csMsgManager = CSMsgManager.newInstance();
    }

    @Override
    public Note.Record getRecordById(int onlineId) {
        Result result = csMsgManager.doGetRecordById(onlineId);
        if (result.ok()) {
            return result.getDataObject(Note.Record.class);
        } else return null;
    }

    @Override
    public ArrayList<Note.Record> getNoteRecords(int noteId) {
        Result result = csMsgManager.doGetNoteRecords(noteId);
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
    public boolean deleteRecord(int onlineId) {
        Result result = csMsgManager.doDeleteRecord(onlineId);
        return result.ok();
    }

}
