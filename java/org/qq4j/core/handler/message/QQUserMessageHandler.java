package org.qq4j.core.handler.message;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQConstants;
import org.qq4j.core.QQContext;
import org.qq4j.core.QQUserManager;
import org.qq4j.core.handler.QQMessageHandler;
import org.qq4j.domain.QQUser;
import org.qq4j.helper.QQMessageParser;
import org.qq4j.net.SystemConstants;

public class QQUserMessageHandler extends BaseMessageHandler implements
        QQMessageHandler {

    private final Log log = LogFactory.getLog(QQUserMessageHandler.class);

    @Override
    public void handle(final QQContext context, final JSONObject json) {
        final JSONObject value = json.getJSONObject("value");
        // 内容
        final JSONArray content = value.getJSONArray("content");
        final String message = QQMessageParser.parseMessage(content);
        // 发送人
        final long uin = value.getLong("from_uin");
        final long msgId = value.getLong("msg_id");
        final long time = value.getLong("time") * 1000;

        final QQUserManager friendManager = context.getFriendManager();
        final QQUser user = friendManager.getQQUser(uin);
        if (user != null) {
            synchronized (user) {
                this.log.info(String.format("[%s-%d]%s >> %s发送消息：%s",
                                            DateFormatUtils.format(time,
                                                                   SystemConstants.DATETIME_FORMAT),
                                            msgId,
                                            context.getSelf(),
                                            user,
                                            message));
                final boolean isNew = msgId > user.getLastMsgId();
                final boolean isTooOld = System.currentTimeMillis() - time > this.getReplyTimeLimit();
                final boolean isBlack = friendManager.isBlackList(user);
                final boolean isBusy = this.isBuzy(user);

                if (isNew && !isTooOld && !isBlack && !isBusy) {
                    if (this.isRepeat(user, message)) {
                        this.handleRepeat(context, user);
                    } else {
                        this.getHandlers().handle(context, user, message);
                    }
                }
                user.setLastMsgId(msgId);
                user.setLastMsg(message);
            }
        }
    }

    protected void handleRepeat(final QQContext context, final QQUser user) {
        final String answer = this.selectRepeatAnswer(user);
        context.getSender().sendToUser(user, answer);
    }

    private boolean isBuzy(final QQUser user) {
        final String status = user.getStatus();
        final boolean isSilent = StringUtils.equals(QQConstants.STATUS_SILENT,
                                                    status);
        final boolean isBusy = StringUtils.equals(QQConstants.STATUS_BUSY,
                                                  status);
        final boolean isAway = StringUtils.equals(QQConstants.STATUS_AWAY,
                                                  status);
        if (isSilent || isBusy || isAway) {
            return true;
        }
        return false;
    }

    @Override
    public String getHandleType() {
        return "message";
    }

}
