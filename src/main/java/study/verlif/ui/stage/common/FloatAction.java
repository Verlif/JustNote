package study.verlif.ui.stage.common;

public interface FloatAction {
    /**
     * 获取动作名
     * @return  动作名
     */
    String tag();

    /**
     * 点击事件
     *
     * @return 是否继续显示窗口
     */
    default boolean action() {
        return true;
    }

    /**
     * 设置Label样式
     * @return  样式数据
     */
    default String style() { return ""; }
}
