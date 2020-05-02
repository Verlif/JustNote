package study.verlif.ui.stage.search;

import study.verlif.ui.stage.base.BaseStage;

public class SearchStage extends BaseStage {

    @Override
    public String loadResource() {
        return "/fxml/search.fxml";
    }

    @Override
    public void setAttr() {
        setTitle("搜索");
        setMinWidth(800);
    }
}
