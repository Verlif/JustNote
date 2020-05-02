package study.verlif.ui.progress;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import study.verlif.ui.stage.base.BaseController;

public class ProgressController extends BaseController {

    public ProgressBar progressBar;
    public Label progressText;
    public Label titleView;

    @Override
    protected void init() {
    }

    public void setProgress(double progress) {
        progressBar.setProgress(progress);
        progressText.setText((int) (progress * 100) + "%");
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }
}
