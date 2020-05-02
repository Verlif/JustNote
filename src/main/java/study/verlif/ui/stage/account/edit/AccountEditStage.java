package study.verlif.ui.stage.account.edit;

import study.verlif.ui.stage.base.BaseStage;

public class AccountEditStage extends BaseStage {

    @Override
    public String loadResource() {
        return "/fxml/account/accountEdit.fxml";
    }

    @Override
    public void setAttr() {
        setTitle("编辑信息");
    }

}
