package study.verlif.ui.stage.account.show;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import study.verlif.manager.StageManager;
import study.verlif.manager.UserManager;
import study.verlif.model.User;
import study.verlif.ui.stage.account.edit.AccountEditStage;
import study.verlif.ui.stage.base.BaseController;
import study.verlif.util.TimeFormatUtil;

public class AccountShowController extends BaseController {

    public Label nameView;
    public Label timeView;
    public Label contentView;
    public ToolBar buttonArea;

    private User user;
    private UserManager userManager;
    private StageManager stageManager;

    public AccountShowController() {
        userManager = UserManager.newInstance();
        stageManager = StageManager.newInstance();
    }

    @Override
    protected void init() {

    }

    public void setUser(User user) {
        this.user = user;
        if (userManager.getLocalUser().getUserId() == user.getUserId()) {
            buttonArea.setVisible(true);
        } else {
            buttonArea.setVisible(false);
        }
        nameView.setText(user.getUserName());
        if (user.getUserDesc() != null && user.getUserDesc().length() > 0) {
            contentView.setText(user.getUserDesc());
        } else contentView.setText("干净得像一张白纸。");
        timeView.setText(TimeFormatUtil.getDateString(user.getCreateTime()));
    }

    public void edit() {
        AccountEditStage stage = stageManager.getStage(new AccountEditStage());
        stage.show();
        close();
    }
}
