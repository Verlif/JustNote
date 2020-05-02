package study.verlif.database.local.intfac;

import study.verlif.model.Note;

import java.util.ArrayList;

public interface LocalRecordDBConnector {

    /**
     * 通过记录id获取记录。
     * @return  <p> 记录实例。   </p>
     *          <p> null - 未找到相关记录。 </p>
     */
    Note.Record getRecordById(int recordId);

    /**
     * 通过参照的记录实例获取记录实例列表。
     * @param record    参照的记录实例
     * @return  查询结果集
     */
    ArrayList<Note.Record> getRecordList(Note.Record record);

    boolean saveOrModifyRecord(Note.Record record);

    void buildId(Note.Record record);

    void delRecord(int recordId);

    boolean reId(int oldId, int nowId);
}
