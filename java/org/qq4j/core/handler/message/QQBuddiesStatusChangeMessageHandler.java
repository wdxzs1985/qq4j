package org.qq4j.core.handler.message;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQConstants;
import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQMessageHandler;
import org.qq4j.domain.QQUser;

public class QQBuddiesStatusChangeMessageHandler implements QQMessageHandler {

    private final static Log LOG = LogFactory.getLog(QQBuddiesStatusChangeMessageHandler.class);

    @Override
    public void handle(final QQContext context, final JSONObject json) {
        if (QQBuddiesStatusChangeMessageHandler.LOG.isDebugEnabled()) {
            final JSONObject value = json.getJSONObject("value");
            final long uin = value.getLong("uin");
            final String status = value.getString("status");
            final QQUser user = context.getFriendManager().getQQUser(uin);

            if (user != null) {
                user.setStatus(status);
                if (QQConstants.STATUS_ONLINE.equals(status)) {
                    QQBuddiesStatusChangeMessageHandler.LOG.debug(String.format("%s >> %s上线了。",
                                                                                context.getSelf(),
                                                                                user));
                } else if (QQConstants.STATUS_AWAY.equals(status)) {
                    QQBuddiesStatusChangeMessageHandler.LOG.debug(String.format("%s >> %s离开一会。",
                                                                                context.getSelf(),
                                                                                user));
                } else if (QQConstants.STATUS_OFFLINE.equals(status)) {
                    QQBuddiesStatusChangeMessageHandler.LOG.debug(String.format("%s >> %s下线了。",
                                                                                context.getSelf(),
                                                                                user));
                } else if (QQConstants.STATUS_CALLME.equals(status)) {
                    QQBuddiesStatusChangeMessageHandler.LOG.debug(String.format("%s >> %sQ我吧。",
                                                                                context.getSelf(),
                                                                                user));
                } else if (QQConstants.STATUS_BUSY.equals(status)) {
                    QQBuddiesStatusChangeMessageHandler.LOG.debug(String.format("%s >> %s忙碌中。",
                                                                                context.getSelf(),
                                                                                user));
                } else if (QQConstants.STATUS_SILENT.equals(status)) {
                    QQBuddiesStatusChangeMessageHandler.LOG.debug(String.format("%s >> %s请勿打扰。",
                                                                                context.getSelf(),
                                                                                user));
                } else {
                    QQBuddiesStatusChangeMessageHandler.LOG.debug(String.format("%s >> %s未知状态：%s",
                                                                                context.getSelf(),
                                                                                user,
                                                                                status));
                }
            }
        }
    }

    @Override
    public String getHandleType() {
        return "buddies_status_change";
    }

}
