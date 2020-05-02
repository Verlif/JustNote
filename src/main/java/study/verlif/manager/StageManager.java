package study.verlif.manager;

import com.sun.istack.internal.NotNull;
import study.verlif.ui.stage.base.BaseStage;

import java.util.HashMap;

public class StageManager {

    private static class StageHandler {
        private static StageManager instance = new StageManager();
    }

    private HashMap<String, BaseStage> stageHashMap;

    private StageManager() {
        stageHashMap = new HashMap<>();
    }

    /**
     * 获取StageManager实例，这里不需要做线程安全考虑。
     *
     * @return StageManager实例。
     */
    public static StageManager newInstance() {
        return StageHandler.instance;
    }

    /**
     * 向管理器中添加BaseStage。
     *
     * @param stage 添加的BaseStage
     * @return <p> 添加是否成功。  </p>
     * <p> true-添加成功。   </p>
     * <p> false-添加失败，表示已存在相同tag的BaseStage。   </p>
     */
    public boolean addStage(@NotNull BaseStage stage) {
        return addStage(stage.getTag(), stage);
    }

    /**
     * 向管理器中添加BaseStage。
     *
     * @param tag   表示该BaseStage的唯一tag
     * @param stage 添加的BaseStage
     * @return <p> 添加是否成功。  </p>
     * <p> true-添加成功。   </p>
     * <p> false-添加失败，表示已存在相同tag的BaseStage。   </p>
     */
    public boolean addStage(String tag, BaseStage stage) {
        if (stageHashMap.containsKey(tag)) {
            return false;
        } else {
            stageHashMap.put(tag, stage);
            return true;
        }
    }

    /**
     * 获取BaseStage。
     *
     * @param tag 存在于管理器中的tag
     * @return <P> tag对应的BaseStage，若不存在则返回null。   </P>
     */
    public <T> T getStage(String tag) {
        return (T) stageHashMap.get(tag);
    }

    /**
     * <p>优先判定管理器中是否存在相同tag的BaseStage。</p>
     * <p>当管理器中没有时，会将传值的BaseStage存入，并初始化传出。</p>
     *
     * @param baseStage 判定的BaseStage
     * @param <T>       获取的BaseStage类
     * @return 需要的BaseStage对象
     */
    public <T extends BaseStage> T getStage(BaseStage baseStage) {
        return getStage(baseStage, false);
    }

    /**
     * <p>优先判定管理器中是否存在相同tag的BaseStage。</p>
     * <p>当管理器中没有时，会将传值的BaseStage存入，并初始化传出。</p>
     *
     * @param baseStage 判定的BaseStage
     * @param ifThis    <p>    是否采用当前的窗口。  </p>
     *                  <p>    true - 当管理器中有同类型窗口时，会将其关闭，并用传入的窗口取代。</p>
     *                  <p>    false - 当管理器中有同类型的窗口时，以存在的窗口作为返回值。  </p>
     * @param <T>       获取的BaseStage类
     * @return 需要的BaseStage对象
     */
    public <T> T getStage(BaseStage baseStage, boolean ifThis) {
        if (ifThis) {
            stageHashMap.remove(baseStage.getTag());
        } else if (stageHashMap.containsKey(baseStage.getTag())) {
            BaseStage bs = stageHashMap.get(baseStage.getTag());
            return (T) bs;
        }
        baseStage.init();
        addStage(baseStage);
        return (T) baseStage;
    }

    /**
     * 移除管理器中的BaseStage
     *
     * @param tag 被移除的tag
     */
    public void removeStage(String tag) {
        stageHashMap.remove(tag);
    }
}
