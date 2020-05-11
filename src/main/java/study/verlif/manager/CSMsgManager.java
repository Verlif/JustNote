package study.verlif.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import study.verlif.model.Note;
import study.verlif.model.Result;
import study.verlif.model.Share;
import study.verlif.model.User;
import study.verlif.util.ConsoleUtil;
import study.verlif.util.FileUtil;
import study.verlif.util.HttpConnector;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class CSMsgManager {

    private static final String TOKEN_FILE = "token.tk";

    private final HttpConnector httpConnector;
    private final SettingManager settingManager;
    private final FileUtil fileUtil;

    public static CSMsgManager newInstance() {
        return new CSMsgManager();
    }

    private CSMsgManager() {
        httpConnector = new HttpConnector();
        settingManager = SettingManager.newInstance();
        fileUtil = new FileUtil();
    }

    public Result doLogin(User user) {
        String s = doPost("login/login", user);
        Result result = getResult(s);
        if (result.ok()) {
            String token = result.getDataObject(String.class);
            if (token != null) {
                saveToken(token);
            }
        }
        return result;
    }

    public Result doRegister(User user) {
        String s = doPost("login/register", user);
        Result result = getResult(s);
        String token = result.getDataObject(String.class);
        if (token != null) {
            saveToken(token);
        }
        return result;
    }

    public Result doUpdateUser(User user) {
        String s = doPost("user/update", user);
        return getResult(s);
    }

    public Result doSyncList(int userId) {
        String s = doPost("sync/serverList");
        return getResult(s);
    }

    public Result doSyncUpload(
            ArrayList<Note> notesUpdate, ArrayList<Note> notesInsert,
            ArrayList<Note.Record> recordsUpdate, ArrayList<Note.Record> recordsInsert
    ) {
        HashMap<String, String> header = new HashMap<>();
        header.put("token", getToken());
        HashMap<String, Serializable> param = new HashMap<>();
        param.put("notes.update", notesUpdate);
        param.put("notes.insert", notesInsert);
        param.put("records.update", recordsUpdate);
        param.put("records.insert", recordsInsert);
        String s = httpConnector.doPost(buildUrl("sync/upload"), header, param);
        return getResult(s);
    }

    public Result doSyncDownLoad(ArrayList<Integer> noteIds, ArrayList<Integer> recordIds) {
        HashMap<String, String> header = new HashMap<>();
        header.put("token", getToken());
        HashMap<String, Serializable> param = new HashMap<>();
        param.put("noteIds", noteIds);
        param.put("recordIds", recordIds);
        String s = httpConnector.doPost(buildUrl("sync/download"), header, param);
        return getResult(s);
    }

    public Result doGetUser(int userId) {
        String s = doPost("user/get/id", userId);
        return getResult(s);
    }

    public Result doGetNoteById(int noteId) {
        String s = doPost("note/get/id", noteId);
        return getResult(s);
    }

    public Result doGetUserNote(int userId) {
        String s = doPost("note/get/user", userId);
        return getResult(s);
    }

    public Result doGetSharedNote() {
        String s = doPost("note/get/shared");
        return getResult(s);
    }

    public Result doSaveNote(Note note) {
        String s = doPost("note/save", note);
        return getResult(s);
    }

    public Result doModifyNote(Note note) {
        String s = doPost("note/modify", note);
        return getResult(s);
    }

    public Result doDeleteNote(int noteId) {
        String s = doPost("note/delete/id", noteId);
        return getResult(s);
    }

    public Result doGetRecordById(int recordId) {
        String s = doPost("record/get/id", recordId);
        return getResult(s);
    }

    public Result doGetNoteRecords(int noteId) {
        HashMap<String, String> header = new HashMap<>();
        header.put("token", getToken());
        HashMap<String, Serializable> param = new HashMap<>();
        param.put("noteId", noteId);
        String s = httpConnector.doPost(buildUrl("record/get/note"), header, param);
        return getResult(s);
    }

    public Result doSaveRecord(Note.Record record) {
        String s = doPost("record/save", record);
        return getResult(s);
    }

    public Result doModifyRecord(Note.Record record) {
        String s = doPost("record/modify", record);
        return getResult(s);
    }

    public Result doDeleteRecord(int recordId) {
        String s = doPost("record/delete/id", recordId);
        return getResult(s);
    }

    public Result doGetNoteShare(int noteIdOL) {
        String s = doPost("share/get/note", noteIdOL);
        return getResult(s);
    }

    public Result doSaveShare(Share share) {
        String s = doPost("share/save", share);
        return getResult(s);
    }

    public Result doModifyShare(Share share) {
        String s = doPost("share/modify", share);
        return getResult(s);
    }

    public Result doDeleteShare(int shareId) {
        String s = doPost("share/delete", shareId);
        return getResult(s);
    }

    private Result getResult(String jsonString) {
        try {
            JSONObject json = JSON.parseObject(jsonString);
            Result result = new Result();
            if (json != null) {
                result.setCode(json.getIntValue("code"));
                result.setMsg(json.getString("msg"));
                result.setData(json.getString("data"));
            } else {
                result.setCode(Result.CODE_BAD_NW);
                result.setMsg("网络错误");
            }
            return result;
        } catch (JSONException e) {
            return new Result(Result.CODE_BAD_NW, "网络问题");
        }

    }

    private String doPost(String mapper, Serializable serializable) {
        HashMap<String, String> header = new HashMap<>();
        header.put("token", getToken());
        HashMap<String, Serializable> param = new HashMap<>();
        if (serializable != null) {
            param.put(serializable.getClass().getSimpleName().toLowerCase(), serializable);
        }
        return httpConnector.doPost(buildUrl(mapper), header, param);
    }

    private String doPost(String mapper) {
        return doPost(mapper, null);
    }

    private void saveToken(String token) {
        File file = new File(TOKEN_FILE);
        fileUtil.saveTextToFile(token, file);
    }

    private String getToken() {
        File file = new File(TOKEN_FILE);
        if (file.exists()) {
            return fileUtil.getStringFromFile(file);
        } else return "";
    }

    private String buildUrl(String mapping) {
        return settingManager.getSetting().getIp() + "/" + mapping;
    }
}
