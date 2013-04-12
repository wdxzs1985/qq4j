package org.qq4j.core.handler.message;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQMessageHandler;
import org.qq4j.domain.QQUser;

public class QQInputNotifyMessageHandler implements QQMessageHandler {

    private final static Log LOG = LogFactory.getLog(QQInputNotifyMessageHandler.class);

    @Override
    public void handle(final QQContext context, final JSONObject json) {
        if (QQInputNotifyMessageHandler.LOG.isDebugEnabled()) {
            final JSONObject value = json.getJSONObject("value");
            final long uin = value.getLong("from_uin");
            final QQUser user = context.getFriendManager().getQQUser(uin);
            QQInputNotifyMessageHandler.LOG.debug(String.format("%s >> %s正在输入。。。",
                                                                context.getSelf(),
                                                                user));
        }
    }

    @Override
    public String getHandleType() {
        return "input_notify";
    }

}
