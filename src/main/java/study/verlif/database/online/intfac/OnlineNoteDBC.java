package study.verlif.database.online.intfac;

import study.verlif.model.Note;

import java.util.ArrayList;

public interface OnlineNoteDBC {

    /**
     * 通过笔记本的onlineId查询
     * @param onlineId  线上模式的笔记本id
     * @return  <p> 笔记本实例。  </p>
     *          <p> null - 未查询到相关数据</p>
     */
    Note getNoteById(int onlineId);

    /**
     * 获取用户的所有笔记本
     * @param userId    用户id
     * @return  笔记本集合
     */
    ArrayList<Note> getSelfNotes(int userId);

    /**
     * 保存笔记本数据到线上服务器
     * @param note  保存的笔记本实例
     * @return  <p> true - 保存成功。    </p>
     *          <p> false - 保存失败。可能是权限不足或数据出错。  </p>
     */
    boolean saveOrModifyNote(Note note);

    /**
     * 删除线上服务器中的相关笔记本信息
     * @param onlineId  目标笔记本的线上id。
     * @return  <p> true - 删除成功。    </p>
     *          <p> false - 删除失败。可能是权限不足。  </p>
     */
    boolean deleteNoteById(int onlineId);

}
