package study.verlif.database.local.intfac;

import study.verlif.model.Note;

import java.util.ArrayList;

public interface LocalNoteDBConnector {

    /**
     * 通过笔记本id获取笔记本数据。
     * @return  <p> 获取的笔记本实例。   </p>
     *          <p> null - 没有对应的笔记本id。    </p>
     */
    Note getNoteById(int noteId);

    /**
     * 通过笔记本内部数据查询相关的笔记本列表。
     * @param note  参照实例
     * @return  <p> 查询的相关的笔记本列表数据实例。    </p>
     */
    ArrayList<Note> getNoteList(Note note);

    /**
     * 保存笔记本到本地
     * @param note  保存的笔记本实例
     * @return  <p> true - 保存成功。  </p>
     *          <p> false - 保存失败。 </p>
     */
    boolean saveOrModifyNote(Note note);

    void buildId(Note note);

    /**
     * 通过笔记本id删除笔记本
     * @param noteId    目标笔记本id
     */
    void delNoteById(int noteId);

    boolean reId(int oldId, int nowId);
}
