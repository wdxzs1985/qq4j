package org.qq4j.core.handler.message;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.common.QQMessageParser;
import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQCommandHandler;
import org.qq4j.core.handler.QQMessageHandler;
import org.qq4j.domain.QQUser;

public class QQStrangerMessageHandler implements QQMessageHandler {

    private final Log log = LogFactory.getLog(QQStrangerMessageHandler.class);

    private QQCommandHandler replyHandler = null;

    @Override
    public void handle(final QQContext context, final JSONObject json) {
        final JSONObject value = json.getJSONObject("value");
        final long uin = value.getLong("from_uin");
        final long msgId = value.getLong("msg_id");
        final QQUser user = context.getFriendManager().getQQUser(uin);
        final QQUser self = context.getUserManager().getSelf();
        if (user != null
            && msgId != user.getLastMsgId()) {
            // 内容
            final JSONArray content = value.getJSONArray("content");
            final String message = QQMessageParser.parseMessage(content);
            this.log.info(String.format("%s >> %s(陌生人)发送消息：%s",
                                        self,
                                        user,
                                        message));
            this.replyHandler.handle(context, user, message);
            user.setLastMsgId(msgId);
        }
    }

    @Override
    public String getHandleType() {
        return "sess_message";
    }

    public QQCommandHandler getReplyHandler() {
        return this.replyHandler;
    }

    public void setReplyHandler(final QQCommandHandler replyHandler) {
        this.replyHandler = replyHandler;
    }

}
