package org.qq4j.core.handler.message;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQMessageHandler;
import org.qq4j.domain.QQUser;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

public class QQShakeMessageHandler implements QQMessageHandler {

    private final static Log LOG = LogFactory.getLog(QQShakeMessageHandler.class);

    @Override
    public void handle(final QQContext context, final JSONObject json)
            throws UnsupportedEncodingException, JSONException {
        final JSONObject value = json.getJSONObject("value");
        // 发送人
        final long uin = value.getLong("from_uin");
        final QQUser user = context.getFriendManager().getQQUser(uin);
        if (user != null) {
            if (QQShakeMessageHandler.LOG.isDebugEnabled()) {
                final String message = String.format("%s >> %s发了一个窗口抖动。",
                                                     context.getSelf(),
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
