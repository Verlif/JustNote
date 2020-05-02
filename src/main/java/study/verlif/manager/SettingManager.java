package study.verlif.manager;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import study.verlif.util.FileUtil;

import java.io.File;
import java.io.Serializable;

public class SettingManager {
    private static class SettingHandler {
        private static SettingManager instance = new SettingManager();
    }

    private static final String FILE_NAME = "setting.bak";

    @Getter
    private Setting setting;

    private SettingManager() {
        setting = loadSetting();
    }

    public static SettingManager newInstance() {
        return SettingHandler.instance;
    }

    public void saveSetting() {
        new FileUtil().saveObjectToFile(setting, getSettingFile());
    }

    private Setting loadSetting() {
        File file = getSettingFile();
        if (file.exists()) {
            return (Setting) new FileUtil().getObjectFromFile(file);
        } else return new Setting();
    }

    private File getSettingFile() {
        return new File(FILE_NAME);
    }

    @Data
    public static class Setting implements Serializable {
        public String httpType = "http://";
        public String ip = "";
    }
}
