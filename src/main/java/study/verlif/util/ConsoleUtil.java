package study.verlif.util;

import java.util.Date;

public class ConsoleUtil {

    public static void println(Object text) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb
                .append(new Date())
                .append(" - Thread-")
                .append(Thread.currentThread().getId())
                .append(" - ");
        if (stack.length > 4) {
            String[] temp;
            for (int i = 4; i > 1; i--) {
                temp = stack[i].getClassName().split("\\.");
                sb.append(temp[temp.length - 1]).append("(").append(stack[i].getMethodName()).append(").");
            }
        }
        sb.append(" -\n\t\t").append(text);
        System.out.println(sb.toString());
    }
}
