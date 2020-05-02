package study.verlif.database.online.intfac;

import study.verlif.model.User;

public interface OnlineUserDBC {

    User getUser(int userId);
    boolean updateUser(User user);

}
