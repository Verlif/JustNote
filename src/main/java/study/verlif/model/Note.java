package study.verlif.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = true)
public class Note extends JSONBuilder implements Serializable {
    private int noteId;     // 笔记本Id
    private int noteIdOL;   // 笔记本在线id
    private String noteTitle;   // 笔记本标题
    private String noteDesc;    // 笔记本描述
    private int creatorId;      // 笔记本创建者id
    private int ownerId;        // 笔记本拥有者id
    private Timestamp createTime;   // 笔记本创建时间
    private Timestamp updateTime;   // 笔记本更新时间
    private boolean isShared;       // 是否处于共享状态

    public Note() {
        isShared = false;
    }

    public void setCreateTime(long time) {
        this.createTime = new Timestamp(time);
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(long time) {
        this.updateTime = new Timestamp(time);
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 为了兼容listView，修改toString
     * @return  笔记本的标题
     */
    public String toString() {
        return noteTitle;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Record extends JSONBuilder implements Serializable {
        private int recordId;   // 记录的id
        private int recordIdOL;     // 记录在线id
        private String recordTitle;     // 记录标题
        private String recordContent;   // 记录内容
        private int noteId;         // 所属笔记id
        private int creatorId;      // 记录创建者id
        private Timestamp createTime;       // 记录创建时间
        private Timestamp updateTime;       // 记录更新时间

        public void setCreateTime(long time) {
            this.createTime = new Timestamp(time);
        }

        public void setCreateTime(Timestamp createTime) {
            this.createTime = createTime;
        }

        public void setUpdateTime(long time) {
            this.updateTime = new Timestamp(time);
        }

        public void setUpdateTime(Timestamp updateTime) {
            this.updateTime = updateTime;
        }

        /**
         * 为了兼容listView，修改toString
         * @return  笔记的标题
         */
        public String toString() {
            return recordTitle;
        }
    }
}
