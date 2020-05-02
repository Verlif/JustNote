package study.verlif.database.online;

import study.verlif.database.online.intfac.OnlineUserDBC;
import study.verlif.manager.CSMsgManager;
import study.verlif.model.Result;
import study.verlif.model.User;

public class OnlineUserDBCImpl implements OnlineUserDBC {

    private final CSMsgManager csMsgManager;

    public OnlineUserDBCImpl() {
        csMsgManager = CSMsgManager.newInstance();
    }

    @Override
    public User getUser(int userId) {
        Result result = csMsgManager.doGetUser(userId);
        if (result.ok()) {
            return result.getDataObject(User.class);
        } else return null;
    }

    @Override
    public boolean updateUser(User user) {
        Result result = csMsgManager.doUpdateUser(user);
        return result.ok();
    }
}
