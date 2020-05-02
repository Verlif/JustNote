package study.verlif.ui.stage.base;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Data;
import study.verlif.manager.StageManager;
import study.verlif.util.ResFileUtil;

import java.io.IOException;

public abstract class BaseStage extends Stage {

    private String title;
    private boolean isInit = false;

    private BaseController baseController;

    public void init() {
        if (!isInit) {
            StageAttr stageAttr = new StageAttr();
            Parent root = null;
            try {
                // 设定图标
                getIcons().add(new Image(ResFileUtil.getFileUrl("/icon/icon.png")));
                // 获取根视图文件
                FXMLLoader loader = new FXMLLoader(getClass().getResource(loadResource()));
                root = loader.load();
                // 初始化窗口大小
                initStageAttr(stageAttr);
                // 获取控制器
                baseController = loader.getController();
                if (baseController != null) {
                    baseController.setTag(getTag());
                }
                // 设定窗口属性
                setAttr();
            } catch (Exception e) {
                e.printStackTrace();
                // 设定错误页面
                try {
                    root = FXMLLoader.load(getClass().getResource("/fxml/error.fxml"));
                    setTitle("Error!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            // 设置视图
            if (stageAttr.width == 0) {
                setScene(new Scene(root));
            } else setScene(new Scene(root, stageAttr.width, stageAttr.height));
            isInit = true;

            setOnHidden(event -> close());
        }
    }

    public String getTag() {
        return getClass().getSimpleName();
    }

    protected void initStageAttr(StageAttr stageAttr) {
    }

    public abstract String loadResource();
    public abstract void setAttr();

    @Override
    public void close() {
        Platform.runLater(super::close);
        StageManager.newInstance().removeStage(getTag());
    }

    @Data
    protected static class StageAttr {
        public int width;
        public int height;
    }

    /**
     * 设置加载标记。
     */
    protected void loading() {
        if (isInit) {
            title = getTitle();
            setTitle(title + "(Loading...)");
        }
    }

    /**
     * 结束加载标记
     */
    protected void finishLoad() {
        if (title != null) {
            setTitle(title);
        }
    }

    public <T extends BaseController> T getController() {
        return (T) baseController;
    }
}
