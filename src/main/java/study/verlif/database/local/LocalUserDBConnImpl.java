package study.verlif.database.local;

import study.verlif.model.User;
import study.verlif.database.local.intfac.LocalUserDBConnector;
import study.verlif.util.FileUtil;

import java.io.File;
import java.util.Date;

public class LocalUserDBConnImpl implements LocalUserDBConnector {

    private FileUtil fileUtil;

    public LocalUserDBConnImpl() {
        fileUtil = new FileUtil();
    }

    @Override
    public User getUser() {
        return (User) fileUtil.getObjectFromFile(buildDataFile());
    }

    @Override
    public boolean saveUser(User user) {
        user.setCreateTime(new Date().getTime());
        return fileUtil.saveObjectToFile(user, buildDataFile());
    }

    @Override
    public void delUser() {
        saveUser(new User());
    }

    /**
     * 建立用户文件
     * @return  用户文件
     */
    private File buildDataFile() {
        return new File("user.data");
    }
}
