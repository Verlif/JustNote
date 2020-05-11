package study.verlif.manager;

import com.sun.org.apache.regexp.internal.RE;
import study.verlif.database.online.OnlineNoteDBCImpl;
import study.verlif.database.online.OnlineRecordDBCImpl;
import study.verlif.database.online.intfac.OnlineNoteDBC;
import study.verlif.database.local.LocalNoteDBConnImpl;
import study.verlif.database.local.LocalRecordDBConnImpl;
import study.verlif.database.local.intfac.LocalNoteDBConnector;
import study.verlif.database.local.intfac.LocalRecordDBConnector;
import study.verlif.database.online.intfac.OnlineRecordDBC;
import study.verlif.model.Note;

import java.util.ArrayList;

public class NoteManger {

    private static class NoteHandler {
        private static NoteManger instance = new NoteManger();
    }

    private LocalNoteDBConnector lndbc;
    private OnlineNoteDBC ondbc;
    private LocalRecordDBConnector lrdbc;
    private OnlineRecordDBC ordbc;
    private UserManager userManager;

    private NoteManger() {
        lndbc = new LocalNoteDBConnImpl();
        ondbc = new OnlineNoteDBCImpl();
        lrdbc = new LocalRecordDBConnImpl();
        ordbc = new OnlineRecordDBCImpl();
        userManager = UserManager.newInstance();
    }

    public static NoteManger newInstance() {
        return NoteHandler.instance;
    }

    /**
     * 优先从本地查询笔记本数据，其次在线查询。
     * @param noteId    笔记本的id。本地查询匹配noteId，在线查询匹配noteIdOL。
     * @return  <p> 查询的笔记本实例。   </p>
     *          <p> null - 未查询到相关笔记本信息。 </p>
     */
    public Note getNoteById(int noteId) {
        Note note = lndbc.getNoteById(noteId);
        if (note == null) {
            note = ondbc.getNoteById(noteId);
        }
        return note;
    }

    public Note getLocalNoteById(int noteId) {
        return lndbc.getNoteById(noteId);
    }

    public ArrayList<Note> getSharedNotes() {
        return ondbc.getSharedNotes();
    }

    public Note buildANewNote() {
        Note note = new Note();
        note.setCreatorId(userManager.getLocalUser().getUserId());
        note.setOwnerId(note.getCreatorId());
        note.setNoteTitle("【新笔记本】");
        return note;
    }

    /**
     * 获取当前账户下的所有拥有的笔记本
     * @return  笔记本实例集合
     */
    public ArrayList<Note> getSelfNoteList() {
        Note note = new Note();
        note.setOwnerId(userManager.getLocalUser().getUserId());
        return lndbc.getNoteList(note);
    }

    /**
     * 获取本地所有的笔记本
     * @return  笔记本实例集合
     */
    public ArrayList<Note> getLocalNoteList() {
        return lndbc.getNoteList(null);
    }

    public ArrayList<Note> getNotesByKey(String key) {
        ArrayList<Note> notes = getSelfNoteList();
        ArrayList<Note> results = new ArrayList<>();
        for (Note n : notes) {
            if (n.getNoteTitle().contains(key)
                    || (n.getNoteDesc() != null && n.getNoteDesc().contains(key))) {
                results.add(n);
            }
        }
        return results;
    }

    public ArrayList<Note.Record> getRecordsByKey(String key) {
        ArrayList<Note> notes = getSelfNoteList();
        ArrayList<Note.Record> result = new ArrayList<>();
        for (Note n : notes) {
            ArrayList<Note.Record> records = getLocalNoteRecords(n.getNoteId());
            for (Note.Record r : records) {
                if (r.getRecordTitle().contains(key)
                        || (r.getRecordContent() != null && r.getRecordContent().contains(key))) {
                    result.add(r);
                }
            }
        }
        return result;
    }

    /**
     * 保存笔记本信息到本地与网络中。在保存到本地时，需要用户处于登陆状态。
     * @param note  需要保存的笔记本实例。
     * @return  本地保存是否成功。线上服务器的保存状态不会在此返回。
     */
    public boolean createOrUpdateNote(Note note) {
        if (note.getNoteId() == 0) {
            lndbc.buildId(note);
        }
        if (userManager.isCheckOnline()) {
            if (!ondbc.saveOrModifyNote(note)) {
                return false;
            }
        }
        return lndbc.saveOrModifyNote(note);
    }

    public boolean updateSharedRecord(Note.Record record) {
        return ordbc.saveOrModifyRecord(record);
    }

    public boolean updateLocalNote(Note note) {
        return lndbc.saveOrModifyNote(note);
    }

    /**
     * 删除本地的笔记本数据。
     * @param noteId    目标笔记本的id
     */
    public void deleteLocalNote(int noteId) {
        lndbc.delNoteById(noteId);
    }

    /**
     * 删除线上的笔记本数据
     * @param noteId  目标笔记本的onlineId
     * @return  是否删除成功。
     */
    public boolean deleteOnlineNote(int noteId) {
        return ondbc.deleteNoteById(noteId);
    }

    public boolean renameNote(Note note, int newId) {
        return lndbc.reId(note.getNoteId(), newId);
    }

    /**
     * 获取记录数据。优先查询本地，其次查询线上服务器。
     * @param recordId  记录的id。查询本地时，匹配recordId；查询线上服务器时，匹配recordIdOL
     * @return  <p> 查询到的记录实例。   </p>
     *          <p> null - 未能查询到相关记录。   </p>
     */
    public Note.Record getRecordById(int recordId) {
        Note.Record record = lrdbc.getRecordById(recordId);
        if (record == null && recordId != 0) {
            record = ordbc.getRecordById(recordId);
        }
        return record;
    }

    public Note.Record getLocalRecordById(int recordId) {
        return lrdbc.getRecordById(recordId);
    }

    public Note.Record getOnlineRecordById(int recordIdOL) {
        return ordbc.getRecordById(recordIdOL);
    }

    /**
     * 获取笔记本下的所有记录，包括本地与线上。
     * @param noteId    笔记本id
     * @return  记录集合。
     */
    public ArrayList<Note.Record> getLocalNoteRecords(int noteId) {
        Note.Record record = new Note.Record();
        record.setNoteId(noteId);
        return lrdbc.getRecordList(record);
    }

    public ArrayList<Note.Record> getOnlineNoteRecords(int noteIdOL) {
        Note.Record record = new Note.Record();
        record.setNoteId(noteIdOL);
        return ordbc.getNoteRecords(noteIdOL);
    }

    public boolean createOrUpdateRecord(Note.Record record) {
        if (record.getRecordId() == 0) {
            lrdbc.buildId(record);
        }
        if (userManager.isCheckOnline()) {
            if (!ordbc.saveOrModifyRecord(record)) {
                return false;
            }
        }
        // 当记录是当前用户维护时可存于本地
        if (record.getCreatorId() == userManager.getLocalUser().getUserId()) {
            return lrdbc.saveOrModifyRecord(record);
        } else return true;
    }

    public boolean updateLocalRecord(Note.Record record) {
        return lrdbc.saveOrModifyRecord(record);
    }

    public void deleteLocalRecord(int recordId) {
        lrdbc.delRecord(recordId);
    }

    public boolean deleteOnlineRecord(int recordId) {
        return ordbc.deleteRecord(recordId);
    }

    public boolean renameRecord(Note.Record record, int nowId) {
        return lrdbc.reId(record.getNoteId(), nowId);
    }
}
