package study.verlif.manager.inner;

import lombok.Data;
import study.verlif.manager.HandlerManager;
import study.verlif.util.ConsoleUtil;

import java.io.Serializable;

@Data
public abstract class Handler implements Serializable {

    public interface Tag {
        String LOGIN_SUCCESS = "login";
        String ADD_NOTE = "newNote";
    }

    public Handler() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String[] names = stack[3].getClassName().split("\\.");
        owner = names[names.length - 1];
        tag = owner;
        HandlerManager.newInstance().addHandler(this);
    }

    private String owner;
    private String tag;

    public abstract void handlerMessage(Message message);

    /**
     * 开始Handler处理
     */
    public void start() {
        if (owner == null) {
            try {
                throw new Exception("缺失owner");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        HandlerManager.newInstance().addHandler(this);
    }

    /**
     * 使Handler失效，从管理器中移除
     */
    public void finish() {
        HandlerManager.newInstance().removeHandler(this);
    }
}
