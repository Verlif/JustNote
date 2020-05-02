package study.verlif.manager;

import study.verlif.model.Note;

import java.io.*;
import java.util.ArrayList;

public class NotesFileManager {
    private static class NotesFileHandler {
        private static NotesFileManager instance = new NotesFileManager();
    }

    private NotesFileManager() {
    }

    public static NotesFileManager newInstance() {
        return NotesFileHandler.instance;
    }

    private static final String NOTES_DIR = "notes";
    private static final String RECORDS_DIR = NOTES_DIR + "/records";

    /**
     * 获取Notes的数据文件夹
     *
     * @return File对象
     */
    public File getNotesDir() {
        File file = new File(NOTES_DIR);
        if (file.exists()) {
            return file;
        } else {
            return file.mkdirs() ? file : null;
        }
    }

    /**
     * 通过Note的文件名获取Note实例
     *
     * @param fileName Note储存时的文件名
     * @return Note实例
     */
    public Note getNoteByName(String fileName) {
        FileFilter ff = pathname -> pathname.getName().equals(fileName);
        File[] files = getNotesDir().listFiles(ff);
        if (files != null && files.length == 1) {
            File dataFile = files[0];
            try {
                return (Note) new ObjectInputStream(new FileInputStream(dataFile)).readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取所有存在文件中的Note实例
     *
     * @return Note实例集合
     */
    public ArrayList<Note> getAllNotes() {
        File[] files = getNotesDir().listFiles();
        ArrayList<Note> notes = new ArrayList<>();
        try {
            if (files != null) {
                FileInputStream fis;
                ObjectInputStream ois;
                for (File f : files) {
                    if (f.isFile()) {
                        fis = new FileInputStream(f);
                        ois = new ObjectInputStream(fis);
                        notes.add((Note) ois.readObject());
                        fis.close();
                        ois.close();
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return notes;
    }

    /**
     * 将Note保存至文件中
     *
     * @param note     需要保存的Note实例
     * @param fileName 保存时的文件名
     * @return <p> true - 保存成功。</p>
     * <p> false - 保存失败。可能是权限不足。   </p>
     */
    public boolean saveNoteFile(Note note, String fileName) {
        File file = new File(NOTES_DIR + File.separator + fileName);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(note);
            oos.flush();
            oos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除Note文件
     * @param fileName  相关Note文件名
     */
    public void deleteNoteFile(String fileName) {
        File file = new File(getNotesDir() + File.separator + fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 获取Records的数据文件夹
     *
     * @return File对象
     */
    public File getRecordsDir() {
        File file = new File(RECORDS_DIR);
        if (file.exists()) {
            return file;
        } else {
            return file.mkdirs() ? file : null;
        }
    }

    /**
     * 通过Record的文件名获取Record实例
     *
     * @param fileName Record储存时的文件名
     * @return Record实例
     */
    public Note.Record getRecordByName(String fileName) {
        FileFilter ff = pathname -> pathname.getName().equals(fileName);
        File[] files = getRecordsDir().listFiles(ff);
        if (files != null && files.length == 1) {
            File dataFile = files[0];
            try {
                return (Note.Record) new ObjectInputStream(new FileInputStream(dataFile)).readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将Record保存至文件中
     *
     * @param record   需要保存的Record实例
     * @param fileName 保存时的文件名
     * @return <p> true - 保存成功。</p>
     * <p> false - 保存失败。可能是权限不足。    </p>
     */
    public boolean saveRecordFile(Note.Record record, String fileName) {
        File file = new File(RECORDS_DIR + File.separator + fileName);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(record);
            oos.flush();
            oos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取所有存在文件中的Record实例
     *
     * @return Record实例集合
     */
    public ArrayList<Note.Record> getAllRecords() {
        File[] files = getRecordsDir().listFiles();
        ArrayList<Note.Record> records = new ArrayList<>();
        try {
            if (files != null) {
                for (File f : files) {
                    records.add((Note.Record) new ObjectInputStream(new FileInputStream(f)).readObject());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return records;
    }

    /**
     * 获取目标笔记本下的所有记录
     * @param noteId    目标笔记本的id
     * @return  记录集合
     */
    public ArrayList<Note.Record> getNoteRecords(int noteId) {
        ArrayList<Note.Record> records = getAllRecords();
        ArrayList<Note.Record> returns = new ArrayList<>();
        for (Note.Record r : records) {
            if (r.getNoteId() == noteId) {
                returns.add(r);
            }
        }
        return returns;
    }

    /**
     * 删除Record文件
     * @param fileName  相关Record文件名
     */
    public void deleteRecordFile(String fileName) {
        File file = new File(getRecordsDir() + File.separator + fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    public boolean renameRecord(String oldName, String nowName) {
        File file = new File(getRecordsDir() + File.separator + oldName);
        if (file.exists()) {
            return file.renameTo(new File(getRecordsDir() + File.separator + nowName));
        } else return false;
    }

    public boolean renameNote(String oldName, String nowName) {
        File file = new File(getNotesDir() + File.separator + oldName);
        if (file.exists()) {
            return file.renameTo(new File(getRecordsDir() + File.separator + nowName));
        } else return false;
    }

}
