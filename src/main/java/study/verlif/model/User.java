package study.verlif.model;

import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@RequiredArgsConstructor
public class User extends JSONBuilder implements Serializable {
    private int userId;
    @NonNull private String userName;
    @NonNull private String userKey;
    private String userDesc;
    private String userEmail;
    private Timestamp createTime;

    public void setCreateTime(long time) {
        this.createTime = new Timestamp(time);
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
