package study.verlif.action;

import javafx.application.Platform;
import study.verlif.manager.CSMsgManager;
import study.verlif.manager.NoteManger;
import study.verlif.manager.StageManager;
import study.verlif.manager.UserManager;
import study.verlif.model.Note;
import study.verlif.model.Result;
import study.verlif.model.User;
import study.verlif.ui.progress.ProgressStage;
import study.verlif.ui.stage.main.MainController;
import study.verlif.ui.stage.main.MainStage;
import study.verlif.util.ConsoleUtil;
import study.verlif.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class SyncAction extends Action {

    private final StageManager stageManager;
    private final NoteManger noteManger;
    private final CSMsgManager csMsgManager;
    private final ProgressStage progressStage;

    private ArrayList<Integer> noteIds;
    private ArrayList<Note> notesUpdate;
    private ArrayList<Note> notesInsert;
    private ArrayList<Integer> recordIds;
    private ArrayList<Note.Record> recordsUpdate;
    private ArrayList<Note.Record> recordsInsert;

    public SyncAction() {
        stageManager = StageManager.newInstance();
        noteManger = NoteManger.newInstance();
        csMsgManager = CSMsgManager.newInstance();

        progressStage = stageManager.getStage(new ProgressStage(null));
        progressStage.setProgressTitle("同步");
    }

    @Override
    protected void onStart() {
        Platform.runLater(progressStage::show);
        buildList();
        startSync();
    }

    /**
     * <p>  从服务器获取当前账户的笔记本信息与记录信息。 </p>
     * <p>  填充notesDownload、recordsDownload。</p>
     */
    private void buildList() {
        User user = UserManager.newInstance().getLocalUser();

        progressStage.setProgress(0.1);

        Result result = csMsgManager.doSyncList(user.getUserId());

        progressStage.setProgress(0.2);

        ConsoleUtil.println("syncList: " + result);

        ArrayList<Note> notes = result.getDataList("notes", Note.class);
        ArrayList<Note.Record> records = result.getDataList("records", Note.Record.class);

        notesUpdate = new ArrayList<>();
        notesInsert = new ArrayList<>();
        noteIds = new ArrayList<>();
        recordsUpdate = new ArrayList<>();
        recordsInsert = new ArrayList<>();
        recordIds = new ArrayList<>();

        progressStage.setProgress(0.3);

        /*
         *  提取出未更新的笔记本。
         *  选择需要下载的笔记本。
         */
        for (Note n : notes) {
            Note note = noteManger.getLocalNoteById(n.getNoteId());
            // 判定本地的笔记本是否id冲突
            if (note != null && note.getNoteIdOL() != n.getNoteIdOL()) {
                int id;
                do {
                    id = IdGenerator.genInt6();
                } while (noteManger.getLocalNoteById(id) != null);
                noteManger.renameNote(note, id);
            }
            if (note == null || n.getUpdateTime().getTime() > note.getUpdateTime().getTime()) {
                noteIds.add(n.getNoteIdOL());
            } else if ((note.getUpdateTime().getTime() - n.getUpdateTime().getTime()) > 500) { // 设置更新阈值
                notesUpdate.add(note);
            }
        }

        progressStage.setProgress(0.4);

        /*
         *  添加未添加的笔记本
         */
        ArrayList<Note> localNotes = noteManger.getSelfNoteList();
        for (Note n : localNotes) {
            int i = 0;
            for (; i < notes.size(); i++) {
                if (n.getNoteIdOL() == notes.get(i).getNoteIdOL()) {
                    break;
                }
            }
            if (i == notes.size()) {
                notesInsert.add(n);
            }
        }

        progressStage.setProgress(0.5);

        /*
         *  提取未更新的记录。
         *  选择需要下载的记录。
         */
        for (Note.Record r : records) {
            Note.Record record = noteManger.getLocalRecordById(r.getRecordId());
            // 判定本地的记录是否id冲突
            if (record != null && record.getRecordIdOL() != r.getRecordIdOL()) {
                int id;
                do {
                    id = IdGenerator.genInt6();
                } while (noteManger.getLocalRecordById(id) != null);
                noteManger.renameRecord(record, id);
            }
            if (record == null || r.getUpdateTime().getTime() > record.getUpdateTime().getTime()) {
                recordIds.add(r.getRecordIdOL());
            } else if ((record.getUpdateTime().getTime() - r.getUpdateTime().getTime()) > 500) {
                recordsUpdate.add(record);
            }
        }

        progressStage.setProgress(0.6);

        /*
         *  添加未添加的记录。
         */
        for (Note n : localNotes) {
            ArrayList<Note.Record> LocalRecords = noteManger.getNoteRecords(n.getNoteId());
            for (Note.Record r : LocalRecords) {
                int i = 0;
                for (; i < records.size(); i++) {
                    if (r.getRecordIdOL() == records.get(i).getRecordIdOL()) {
                        break;
                    }
                }
                if (i == records.size()) {
                    recordsInsert.add(r);
                }
            }
        }

        progressStage.setProgress(0.7);
    }

    /**
     * 开始同步
     */
    private void startSync() {
        // 上传需要在服务器更新的数据
        Result uploadResult = csMsgManager.doSyncUpload(notesUpdate, notesInsert, recordsUpdate, recordsInsert);

        progressStage.setProgress(0.8);

        // 下载需要在本地更新的数据
        Result downloadResult = csMsgManager.doSyncDownLoad(noteIds, recordIds);

        progressStage.setProgress(0.9);

        List<Note> notes = downloadResult.getDataList("notes", Note.class);
        List<Note.Record> records = downloadResult.getDataList("records", Note.Record.class);

        for (Note n : notes) {
            noteManger.updateLocalNote(n);
        }
        for (Note.Record r : records) {
            noteManger.updateLocalRecord(r);
        }

        // 刷新主界面
        Platform.runLater(() -> {
            MainStage stage = stageManager.getStage(new MainStage());
            MainController controller = stage.getController();
            controller.init();
        });

        progressStage.setProgress(1);
        progressStage.close();

        ConsoleUtil.println(uploadResult);
        ConsoleUtil.println(downloadResult);
    }

    @Override
    boolean isRunning() {
        return false;
    }

}
