package org.qq4j.core.handler.message;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQContext;
import org.qq4j.core.QQUserManager;
import org.qq4j.core.handler.QQMessageHandler;
import org.qq4j.domain.QQUser;

public class QQBuddyListChangeMessageHandler implements QQMessageHandler {

    private final static Log LOG = LogFactory.getLog(QQBuddyListChangeMessageHandler.class);

    @Override
    public void handle(final QQContext context, final JSONObject json) {
        if (QQBuddyListChangeMessageHandler.LOG.isDebugEnabled()) {
            final JSONObject value = json.getJSONObject("value");
            final JSONArray addedFriends = value.getJSONArray("added_friends");

            final QQUserManager friendManager = context.getFriendManager();

            for (int i = 0; i < addedFriends.size(); i++) {
                final JSONObject jUser = addedFriends.getJSONObject(i);
                final long uin = jUser.getLong("uin");
                final QQUser user = friendManager.getQQUser(uin);
                QQBuddyListChangeMessageHandler.LOG.info(String.format("已添加好友:%s",
                                                                       user));
            }
            final JSONArray removedFriends = value.getJSONArray("removed_friends");
            for (int i = 0; i < removedFriends.size(); i++) {
                final JSONObject jUser = removedFriends.getJSONObject(i);
                final long uin = jUser.getLong("uin");
                final QQUser user = friendManager.getQQUser(uin);
                QQBuddyListChangeMessageHandler.LOG.info(String.format("已移除好友:%s",
                                                                       user));
            }
        }
    }

    @Override
    public String getHandleType() {
        return "buddylist_change";
    }

}
