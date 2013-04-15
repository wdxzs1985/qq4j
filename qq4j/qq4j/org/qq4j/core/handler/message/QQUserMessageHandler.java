package org.qq4j.core.handler.message;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQConstants;
import org.qq4j.core.QQContext;
import org.qq4j.core.QQUserManager;
import org.qq4j.core.handler.QQCommandHandlerMapping;
import org.qq4j.core.handler.QQMessageHandler;
import org.qq4j.domain.QQUser;
import org.qq4j.helper.QQMessageParser;
import org.qq4j.net.SystemConstants;


public class QQUserMessageHandler implements QQMessageHandler {

    // private final QQMessageSender sender;
    private final Log log = LogFactory.getLog(QQUserMessageHandler.class);

    private QQCommandHandlerMapping handlers = null;

    private int replyTimeLimit = 1000;

    private String repeatAnswer1 = null;

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
                final boolean isTooOld = System.currentTimeMillis() - time < this.getReplyTimeLimit();
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

    protected boolean isRepeat(final QQUser user, final String message) {
        if (StringUtils.equals(user.getLastMsg(), message)) {
            user.setRepeatTimes(user.getRepeatTimes() + 1);
            return true;
        }
        user.setRepeatTimes(0);
        return false;
    }

    protected void handleRepeat(final QQContext context, final QQUser user) {
        String answer = null;

        switch (user.getRepeatTimes()) {
        case 1:
            answer = this.repeatAnswer1;
            break;
        default:
            answer = null;
            break;
        }
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

    public QQCommandHandlerMapping getHandlers() {
        return this.handlers;
    }

    public void setHandlers(final QQCommandHandlerMapping handlers) {
        this.handlers = handlers;
    }

    public int getReplyTimeLimit() {
        return this.replyTimeLimit;
    }

    public void setReplyTimeLimit(final int replyTimeLimit) {
        this.replyTimeLimit = replyTimeLimit;
    }

    public String getRepeatAnswer1() {
        return this.repeatAnswer1;
    }

    public void setRepeatAnswer1(final String repeatAnswer1) {
        this.repeatAnswer1 = repeatAnswer1;
    }

}
