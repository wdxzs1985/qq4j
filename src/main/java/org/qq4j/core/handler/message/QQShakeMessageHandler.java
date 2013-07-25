package org.qq4j.core.handler.message;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQMessageHandler;
import org.qq4j.domain.QQUser;

public class QQShakeMessageHandler implements QQMessageHandler {

    private final static Log LOG = LogFactory.getLog(QQShakeMessageHandler.class);

    @Override
    public void handle(final QQContext context, final JSONObject json) {
        final JSONObject value = json.getJSONObject("value");
        // 发送人
        final long uin = value.getLong("from_uin");
        final QQUser user = context.getFriendManager().getQQUser(uin);
        final QQUser self = context.getUserManager().getSelf();
        if (user != null) {
            if (QQShakeMessageHandler.LOG.isDebugEnabled()) {
                final String message = String.format("%s >> %s发了一个窗口抖动。",
                                                     self,
                                                     user);
                QQShakeMessageHandler.LOG.debug(message);
            }
            context.getSender().sendShakeMessage(user);
        }
    }

    @Override
    public String getHandleType() {
        return "shake_message";
    }

}
