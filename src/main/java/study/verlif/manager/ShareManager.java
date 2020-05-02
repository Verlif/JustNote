package study.verlif.manager;

import study.verlif.model.Result;
import study.verlif.model.Share;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShareManager {
    private static class ShareHandler {
        private static final ShareManager instance = new ShareManager();
    }

    private final CSMsgManager csMsgManager;

    private ShareManager() {
        csMsgManager = CSMsgManager.newInstance();
    }

    public static ShareManager newInstance() {
        return ShareHandler.instance;
    }

    /**
     * 获取笔记本的共享列表
     * @param noteIdOL    笔记本的onlineId
     * @return  共享列表
     */
    public List<Share> getNoteShareList(int noteIdOL) {
        Result result = csMsgManager.doGetNoteShare(noteIdOL);
        if (result.ok()) {
            return result.getDataList("shares", Share.class);
        } else return new ArrayList<>();
    }

    public boolean addOrModifyShare(Share share) {
        if (share.getShareId() == 0) {
            return csMsgManager.doSaveShare(share).ok();
        } else return csMsgManager.doModifyShare(share).ok();
    }

    public boolean deleteShare(int shareId) {
        return csMsgManager.doDeleteShare(shareId).ok();
    }
}
