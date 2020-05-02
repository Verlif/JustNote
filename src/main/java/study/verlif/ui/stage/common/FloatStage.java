package study.verlif.ui.stage.common;

import javafx.stage.StageStyle;
import study.verlif.ui.stage.base.BaseStage;

import java.awt.*;
import java.util.ArrayList;

public class FloatStage extends BaseStage {

    private ArrayList<FloatAction> actionList;

    public FloatStage(ArrayList<FloatAction> actionList) {
        this.actionList = actionList;
    }

    @Override
    public String loadResource() {
        return "/fxml/float.fxml";
    }

    @Override
    public void setAttr() {
        initStyle(StageStyle.UNDECORATED);
        // 设置窗口位置
        PointerInfo pi = MouseInfo.getPointerInfo();
        Point p = pi.getLocation();
        setX(p.getX());
        setY(p.getY());
        // 当窗口未被聚焦时关闭
        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                close();
            }
        });
        FloatController controller = getController();
        controller.setActionList(actionList);
    }
}
