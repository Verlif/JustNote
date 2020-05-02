package study.verlif.ui.stage.common;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import study.verlif.manager.StageManager;
import study.verlif.ui.stage.base.BaseController;
import study.verlif.util.TextUtil;

import java.util.ArrayList;

public class FloatController extends BaseController {

    public VBox vBox;

    private StageManager stageManager;

    public FloatController() {
        stageManager = StageManager.newInstance();
    }

    @Override
    protected void init() {

    }

    public void setActionList(ArrayList<FloatAction> actionList) {
        buildMenu(actionList);
    }

    private void buildMenu(ArrayList<FloatAction> actionList) {
        if (actionList != null && actionList.size() > 0) {
            for (FloatAction action : actionList) {
                Label label = new Label();
                // 缩减标题长度
                String tag = TextUtil.cutString(action.tag(), 8);
                label.setText(tag);
                // 设置点击事件
                label.setOnMouseClicked(event -> {
                    if (!action.action()) {
                        close();
                    }
                });
                // 设置内边距
                label.setPadding(new Insets(5));
                // 设置样式
                label.setStyle(action.style());
                vBox.getChildren().add(label);
            }
        }
    }

}
