package study.verlif.util;

public class ResFileUtil {

    public static String getFileUrl(String pathInRes) {
        if (pathInRes.startsWith("/")) {
            return "file:src/main/resources" + pathInRes;
        } else return "file:src/main/resources/" + pathInRes;
    }
}
