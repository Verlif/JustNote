package study.verlif.database.online.intfac;

import study.verlif.model.Note;

import java.util.ArrayList;

public interface OnlineRecordDBC {
    Note.Record getRecordById(int onlineId);
    ArrayList<Note.Record> getNoteRecords(int noteIdOL);
    boolean saveOrModifyRecord(Note.Record record);
    boolean deleteRecord(int onlineId);
}
