package study.verlif.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

@Data
@EqualsAndHashCode(callSuper = true)
public class Share extends JSONBuilder implements Serializable {

    /**
     * 可读权限
     */
    public static final int TYPE_READ = 1001;
    /**
     * 可读可写权限
     */
    public static final int TYPE_WRITE = 1002;

    private int shareId;    // 共享id
    private int noteId;     // 共享的笔记本id
    private int shareType;  // 共享方式
    private String shareDesc;    // 共享说明
    @JSONField(deserialize = false)
    private ArrayList<Integer> userList;    // 共享用户列表
    private String userListString;
    private Timestamp createTime;   // 创建时间

    public void setUserList(ArrayList<Integer> userList) {
        this.userList = userList;
        userListString = Arrays.toString(userList.toArray());
    }

    public void setUserList(String string) {
        setUserListString(string);
    }

    public ArrayList<Integer> getUserList() {
        if (userList == null) {
            userList = new ArrayList<>();
            if (userListString != null) {
                String[] list = userListString.replace("[", "").replace("]", "").replace(" ", "").split(",");
                for (String s : list) {
                    userList.add(Integer.valueOf(s));
                }
            }
        }
        return userList;
    }

    public void setUserListString(String userListString) {
        this.userListString = userListString;
        getUserList();
    }

    public String getShareDesc() {
        return shareType == TYPE_READ ? "可读" : "可写";
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public void setCreateTime(long time) {
        this.createTime = new Timestamp(time);
    }
}
