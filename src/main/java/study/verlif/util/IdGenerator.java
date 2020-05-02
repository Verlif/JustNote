package study.verlif.util;

import java.util.Date;
import java.util.UUID;

public class IdGenerator {

    public static int genInt() {
        return (int) (new Date().getTime() / 1000);
    }

    public static int genInt6() {
        return (int) (Math.random() * 100000000);
    }

    public static String genString() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
}
