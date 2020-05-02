package study.verlif.database.local;

import com.sun.istack.internal.Nullable;
import study.verlif.database.local.intfac.LocalRecordDBConnector;
import study.verlif.manager.NotesFileManager;
import study.verlif.model.Note;
import study.verlif.util.IdGenerator;

import java.util.ArrayList;
import java.util.Date;

public class LocalRecordDBConnImpl implements LocalRecordDBConnector {

    private static final String SUFFIX = ".data";
    private NotesFileManager notesFileManager;

    public LocalRecordDBConnImpl() {
        notesFileManager = NotesFileManager.newInstance();
    }

    @Override
    public Note.Record getRecordById(int recordId) {
        return notesFileManager.getRecordByName(getFileName(recordId));
    }

    @Override
    public ArrayList<Note.Record> getRecordList(@Nullable Note.Record record) {
        if (record == null) {
            return notesFileManager.getAllRecords();
        } else {
            if (record.getNoteId() != 0) {
                return notesFileManager.getNoteRecords(record.getNoteId());
            }
        }
        return null;
    }

    @Override
    public boolean saveOrModifyRecord(Note.Record record) {
        // 当recordId为0时，表示新建的Record
        if (record.getRecordId() == 0) {
            buildId(record);
            if (record.getCreateTime() == null) {
                record.setCreateTime(new Date().getTime());
            }
        }
        if (record.getUpdateTime() == null) {
            record.setUpdateTime(new Date().getTime());
        }
        return notesFileManager.saveRecordFile(record, getFileName(record));
    }

    @Override
    public void buildId(Note.Record record) {
        int id;
        do {
            id = IdGenerator.genInt();
        } while (getRecordById(id) != null);
        record.setRecordId(id);
    }

    @Override
    public void delRecord(int recordId) {
        notesFileManager.deleteRecordFile(getFileName(recordId));
    }

    private String getFileName(Note.Record record) {
        return getFileName(record.getRecordId());
    }

    private String getFileName(int recordId) {
        return recordId + SUFFIX;
    }

    @Override
    public boolean reId(int oldId, int nowId) {
        Note.Record record = getRecordById(oldId);
        if (record != null) {
            return notesFileManager.renameRecord(getFileName(oldId), getFileName(nowId));
        } else return false;
    }
}
