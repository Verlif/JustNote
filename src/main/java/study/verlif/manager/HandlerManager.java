package study.verlif.manager;

import org.apache.http.util.TextUtils;
import study.verlif.manager.inner.Handler;
import study.verlif.manager.inner.Message;

import java.util.HashMap;

public class HandlerManager {

    private static class HandlerHandler {
        private static HandlerManager instance = new HandlerManager();
    }

    private final HashMap<String, Handler> handlerHashMap;

    private HandlerManager() {
        handlerHashMap = new HashMap<>();
    }

    public static HandlerManager newInstance() {
        return HandlerHandler.instance;
    }

    public void addHandler(Handler handler) {
        handlerHashMap.put(handler.getOwner(), handler);
    }

    public void removeHandler(Handler handler) {
        handlerHashMap.remove(handler.getTag());
    }

    /**
     * 将Message交由相关的Handler处理。
     * @param message   需要处理的Handler
     */
    public void handlerMessage(Message message) {
        new Thread(() -> {
            String tag = message.getTag();
            if (!TextUtils.isEmpty(tag)) {
                synchronized (handlerHashMap) {
                    for (Handler handler : handlerHashMap.values()) {
                        if (handler.getTag().equals(tag)) {
                            handler.handlerMessage(message);
                        }
                    }
                }
            } else {
                synchronized (handlerHashMap) {
                    for (Handler handler : handlerHashMap.values()) {
                        handler.handlerMessage(message);
                    }
                }
            }
        }).start();
    }

}
