package study.verlif.manager.inner;

import lombok.Getter;
import study.verlif.manager.HandlerManager;

import java.io.Serializable;

public class Message implements Serializable {

    public interface What {
        int WHAT_RECORD_UPDATE = 1000;
        int WHAT_NOTE_UPDATE = 2000;
        int WHAT_SHARE_UPDATE = 3000;
    }

    /**
     * 消息标签
     */
    @Getter
    private String tag;
    /**
     * 消息类型
     */
    public int what;
    /**
     * 消息附加实例
     */
    public Object obj;

    public Message() {}

    public Message(int what) {
        this.what = what;
    }

    public Message(Class<?> target) {
        target(target);
    }

    /**
     * 消息接收类
     * @param target    期望此消息的接受对象类
     */
    public void target(Class<?> target) {
        this.tag = target.getSimpleName();
    }

    public void send() {
        HandlerManager.newInstance().handlerMessage(this);
    }
}
