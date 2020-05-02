package study.verlif.util;

public class TextUtil {

    /**
     * 缩减字符长度。
     * @param source    源字符串
     * @param limit 限定长度
     * @return  缩减后的字符串
     */
    public static String cutString(String source, int limit) {
        if (source.length() > limit) {
            source = source.substring(0, limit) + "...";
        }
        return source;
    }
}
