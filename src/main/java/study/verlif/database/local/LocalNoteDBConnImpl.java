package study.verlif.database.local;

import com.sun.istack.internal.Nullable;
import study.verlif.database.local.intfac.LocalNoteDBConnector;
import study.verlif.manager.NoteManger;
import study.verlif.manager.NotesFileManager;
import study.verlif.manager.UserManager;
import study.verlif.model.Note;
import study.verlif.util.IdGenerator;

import java.util.ArrayList;
import java.util.Date;

public class LocalNoteDBConnImpl implements LocalNoteDBConnector {

    private static final String SUFFIX = ".data";
    private NotesFileManager notesFileManager;
    private UserManager userManager;

    public LocalNoteDBConnImpl() {
        this.notesFileManager = NotesFileManager.newInstance();
        this.userManager = UserManager.newInstance();
    }

    @Override
    public Note getNoteById(int noteId) {
        return notesFileManager.getNoteByName(getFileName(noteId));
    }

    @Override
    public ArrayList<Note> getNoteList(@Nullable Note note) {
        ArrayList<Note> notes = notesFileManager.getAllNotes();
        if (note != null) {
            int ownerId = note.getOwnerId();
            ArrayList<Note> owner = new ArrayList<>();
            for (Note n : notes) {
                if (n.getOwnerId() == ownerId || n.getOwnerId() == 0) {
                    owner.add(n);
                }
            }
            return owner;
        } else {
            return notes;
        }
    }

    @Override
    public boolean saveOrModifyNote(Note note) {
        // 当noteId为0时，表示新建的Note
        if (note.getNoteId() == 0) {
            buildId(note);
            note.setCreatorId(userManager.getLocalUser().getUserId());
            note.setOwnerId(note.getCreatorId());
            if (note.getCreateTime() == null) {
                note.setCreateTime(new Date().getTime());
            }
        }
        if (note.getUpdateTime() == null) {
            note.setUpdateTime(new Date().getTime());
        }
        return notesFileManager.saveNoteFile(note, getFileName(note));
    }

    @Override
    public void buildId(Note note) {
        int id;
        do {
            id = IdGenerator.genInt();
        } while (getNoteById(id) != null);
        note.setNoteId(id);
    }

    @Override
    public void delNoteById(int noteId) {
        notesFileManager.deleteNoteFile(getFileName(noteId));
    }

    private String getFileName(Note note) {
        return getFileName(note.getNoteId());
    }

    @Override
    public boolean reId(int oldId, int nowId) {
        Note note = getNoteById(oldId);
        if (note != null) {
            note.setNoteId(nowId);
            if (notesFileManager.renameNote(getFileName(oldId), getFileName(nowId))) {
                ArrayList<Note.Record> records = NoteManger.newInstance().getNoteRecords(oldId);
                for (Note.Record r : records) {
                    r.setNoteId(nowId);
                }
                return true;
            } else return false;
        } else return false;
    }

    private String getFileName(int noteId) {
        return noteId + SUFFIX;
    }
}
