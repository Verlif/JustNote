package study.verlif.database.local.intfac;

import study.verlif.model.User;

public interface LocalUserDBConnector {

    /**
     * <p>  从缓存中获取用户数据。 </p>
     * <p>  本地仅允许保存一位用户数据。  </p>
     * @return  用户实例。
     */
    User getUser();

    /**
     * <p>  保存用户数据到缓存。  </p>
     * <p>  缓存仅会保存一位用户的数据，当本地已存在用户数据时，会将此用户进行替换。</p>
     * @param user  保存的用户实例
     * @return  是否保存成功。
     */
    boolean saveUser(User user);

    /**
     * 删除本地用户数据
     */
    void delUser();
}
