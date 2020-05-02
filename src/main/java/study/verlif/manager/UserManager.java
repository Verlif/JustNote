package study.verlif.manager;

import lombok.Getter;
import study.verlif.database.online.OnlineUserDBCImpl;
import study.verlif.database.online.intfac.OnlineUserDBC;
import study.verlif.model.Result;
import study.verlif.model.User;
import study.verlif.database.local.LocalUserDBConnImpl;
import study.verlif.database.local.intfac.LocalUserDBConnector;
import study.verlif.util.ConsoleUtil;

public class UserManager {

    private static class UserHandler {
        private static final UserManager instance = new UserManager();
    }

    /**
     * 本地已缓存用户信息
     */
    private User localUser;
    /**
     * 是否经过在线校验
     */
    @Getter
    private boolean checkOnline;

    private final CSMsgManager csMsgManager;

    private final LocalUserDBConnector sudbc;
    private final OnlineUserDBC oludbc;

    private UserManager() {
        sudbc = new LocalUserDBConnImpl();
        oludbc = new OnlineUserDBCImpl();
        csMsgManager = CSMsgManager.newInstance();
        localUser = loadUser();
    }

    public static UserManager newInstance() {
        return UserHandler.instance;
    }

    /**
     * 加载存储本地的用户信息
     * @return  <p> 用户实例。   </p>
     *          <p> null-不存在合法用户信息时，返回null。  </p>
     */
    private User loadUser() {
        /* 从本地缓存加载用户信息 */
        return sudbc.getUser();
    }

    /**
     * 在线校验用户信息
     * @param user  校验目标
     * @return  <p> true-校验通过，用户合法。  </p>
     *          <p> false-校验未通过，用户不合法。  </p>
     */
    public boolean login(User user) {
        Result result = csMsgManager.doLogin(user);
        if (result.ok()) {
            User resultUser = result.getDataObject(User.class);
            resultUser.setUserKey(user.getUserKey());
            localUser = resultUser;
            ConsoleUtil.println(localUser);
            checkOnline = true;
            saveUser(localUser);
            return true;
        } else return false;
    }

    /**
     * 保存用户到本地缓存
     * @param user  目标用户实例
     */
    public boolean saveUser(User user) {
        if (sudbc.saveUser(user)) {
            localUser = user;
            return true;
        } else return false;
    }

    public boolean registerUser(User user) {
        Result result = csMsgManager.doRegister(user);
        if (result.ok()) {
            User resultUser = result.getDataObject(User.class);
            resultUser.setUserKey(user.getUserKey());
            localUser = resultUser;
            saveUser(localUser);
            return true;
        } else return false;
    }

    public User getLocalUser() {
        // 设置默认的用户信息
        if (localUser == null) {
            localUser = new User();
            localUser.setUserName("本地用户");
            saveUser(localUser);
        }
        return localUser;
    }

    public boolean updateUser(User user) {
        Result result = csMsgManager.doUpdateUser(user);
        if (result.ok()) {
            User resultUser = result.getDataObject(User.class);
            resultUser.setUserKey(localUser.getUserKey());
            localUser = resultUser;
            saveUser(localUser);
            return true;
        } else return false;
    }

    /**
     * 通过用户id获取用户实例。优先查询本地用户，当不匹配时，查询在线数据。
     * @param userId    用户id
     * @return  用户实例。
     */
    public User getUserById(int userId) {
        if (userId == 0) {
            return buildLocalUser();
        } else if (localUser.getUserId() == userId) {
            return localUser;
        } else {
            return oludbc.getUser(userId);
        }
    }

    private User buildLocalUser() {
        User user = new User();
        user.setUserId(0);
        user.setUserName("本地用户");
        user.setUserDesc("本地用户，默认的非登陆用户");
        return user;
    }
}
