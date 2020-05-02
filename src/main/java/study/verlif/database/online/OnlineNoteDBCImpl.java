package study.verlif.database.online;

import study.verlif.database.online.intfac.OnlineNoteDBC;
import study.verlif.manager.CSMsgManager;
import study.verlif.model.Note;
import study.verlif.model.Result;
import study.verlif.util.ConsoleUtil;

import java.util.ArrayList;

public class OnlineNoteDBCImpl implements OnlineNoteDBC {

    private final CSMsgManager csMsgManager;

    public OnlineNoteDBCImpl() {
        csMsgManager = CSMsgManager.newInstance();
    }

    @Override
    public Note getNoteById(int onlineId) {
        Result result = csMsgManager.doGetNoteById(onlineId);
        if (result.ok()) {
            return result.getDataObject(Note.class);
        } else return null;
    }

    @Override
    public ArrayList<Note> getSelfNotes(int userId) {
        Result result = csMsgManager.doGetUserNote(userId);
        if (result.ok()) {
            return result.getDataList("notes", Note.class);
        }
        return null;
    }

    @Override
    public boolean saveOrModifyNote(Note note) {
        Result result;
        if (note.getNoteIdOL() == 0) {
            result = csMsgManager.doSaveNote(note);
        } else {
            result = csMsgManager.doModifyNote(note);
        }
        if (result.ok()) {
            Note resultNote = result.getDataObject(Note.class);
            note.setNoteIdOL(resultNote.getNoteIdOL());
            note.setCreateTime(resultNote.getCreateTime());
            note.setUpdateTime(resultNote.getUpdateTime());
            return true;
        } else return false;
    }

    @Override
    public boolean deleteNoteById(int onlineId) {
        Result result = csMsgManager.doDeleteNote(onlineId);
        return result.ok();
    }

    private void waitASec() {
        try {
            Thread.sleep((long) (Math.random() * 500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
