package study.verlif.util.decoration;

import study.verlif.model.Note;
import study.verlif.ui.stage.common.FloatAction;
import study.verlif.util.decoration.floataction.*;

import java.util.ArrayList;

public class RightClick {

    public static ArrayList<FloatAction> getNoteRC(Note note) {
        ArrayList<FloatAction> list = new ArrayList<>();
        list.add(new NoteNameAction(note.getNoteTitle()));
        list.add(new NoteEditAction(note));
        list.add(new RecordAddAction(note));
        list.add(new NoteShowAction(note));
        list.add(new NoteShareAction(note));
        list.add(new NoteDeleteAction(note));
        return list;
    }

    public static ArrayList<FloatAction> getRecordRC(Note.Record record) {
        ArrayList<FloatAction> list = new ArrayList<>();
        list.add(new RecordEditAction(record));
        list.add(new RecordShowAction(record));
        list.add(new RecordDeleteAction(record));
        return list;
    }

}
