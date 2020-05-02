package study.verlif.manager;

import study.verlif.database.local.LocalNoteDBConnImpl;
import study.verlif.database.local.LocalRecordDBConnImpl;
import study.verlif.database.local.intfac.LocalNoteDBConnector;
import study.verlif.database.local.intfac.LocalRecordDBConnector;
import study.verlif.model.Note;

import java.util.ArrayList;

public class LocalNotesManager {

    private static class SelfNotesHandler {
        private static LocalNotesManager instance = new LocalNotesManager();
    }

    private UserManager userManager;
    private LocalNoteDBConnector sndbc;
    private LocalRecordDBConnector srdbc;

    private LocalNotesManager() {
        userManager = UserManager.newInstance();
        sndbc = new LocalNoteDBConnImpl();
        srdbc = new LocalRecordDBConnImpl();
    }

    public static LocalNotesManager newInstance() {
        return SelfNotesHandler.instance;
    }

    public Note getNoteById(int noteId) {
        return sndbc.getNoteById(noteId);
    }

    public ArrayList<Note> getNoteList() {
        return sndbc.getNoteList(null);
    }

    public Note.Record getRecordById(int recordId) {
        return srdbc.getRecordById(recordId);
    }

    public ArrayList<Note.Record> getRecordList(int noteId) {
        Note.Record record = new Note.Record();
        record.setNoteId(noteId);
        return srdbc.getRecordList(record);
    }

}
