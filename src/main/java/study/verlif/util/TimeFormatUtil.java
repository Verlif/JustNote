package study.verlif.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeFormatUtil {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");    // 时间格式

    /**
     * 格式化时间戳获取时间文本
     * @param timestamp 目标时间戳
     * @return  格式化后的时间文本
     */
    public static String getDateString(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        } else return sdf.format(timestamp);
    }
}
