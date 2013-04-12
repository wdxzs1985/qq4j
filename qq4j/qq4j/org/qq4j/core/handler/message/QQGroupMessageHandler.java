package org.qq4j.core.handler.message;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQContext;
import org.qq4j.core.QQGroupManager;
import org.qq4j.core.QQUserManager;
import org.qq4j.core.handler.QQCommandHandlerMapping;
import org.qq4j.core.handler.QQMessageHandler;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQUser;
import org.qq4j.helper.QQMessageParser;

import framework.SystemConstants;

public class QQGroupMessageHandler implements QQMessageHandler {

    private QQCommandHandlerMapping handlers = null;

    private final Log log = LogFactory.getLog(QQGroupMessageHandler.class);

    private int replyTimeLimit = 1000;

    private String repeatAnswer1 = null;

    @Override
    public void handle(final QQContext context, final JSONObject json) {
        final JSONObject value = json.getJSONObject("value");
        // 内容
        final JSONArray content = value.getJSONArray("content");
        final String message = QQMessageParser.parseMessage(content);
        if (StringUtils.isNotBlank(message)) {
            // 获取群信息用
            final long gCode = value.getLong("group_code");
            // 发送人
            final long uin = value.getLong("send_uin");
            final long msgId = value.getLong("msg_id");
            final long time = value.getLong("time") * 1000;

            final QQGroupManager groupManager = context.getGroupManager();
            final QQUserManager friendManager = context.getFriendManager();
            final QQGroup group = groupManager.getQQGroup(gCode);
            if (group != null) {
                final QQUser member = group.getMembers().get(uin);
                // final QQUser user = friendManager.getQQUser(uin);
                if (member != null) {
                    if (member.getAccount() == 0) {
                        final QQUser user = friendManager.getQQUser(uin);
                        if (user == null) {
                            return;
                        }
                        member.setAccount(user.getAccount());
                    }
                    this.log.info(String.format("[%s-%d]%s >> %s%s发送消息：%s",
                                                DateFormatUtils.format(time,
                                                                       SystemConstants.DATETIME_FORMAT),
                                                msgId,
                                                context.getSelf(),
                                                group,
                                                member,
                                                message));
                    // synchronized (member) {
                    // if (msgId != member.getLastMsgId() &&
                    // System.currentTimeMillis() - time <
                    // this.getReplyTimeLimit()) {
                    // if (this.isRepeat(member, message)) {
                    // this.handleRepeat(context, group, member);
                    // } else {
                    // this.getHandlers().handleGroup(context,
                    // group,
                    // member,
                    // message);
                    // }
                    // }
                    // member.setLastMsgId(msgId);
                    // member.setLastMsg(message);
                    // }
                }
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

    protected void handleRepeat(final QQContext context,
                                final QQGroup group,
                                final QQUser member) {
        String answer = null;

        switch (member.getRepeatTimes()) {
        case 1:
            answer = this.repeatAnswer1;
            break;
        default:
            answer = null;
            break;
        }
        context.getSender().sendToGroup(group, answer);
    }

    @Override
    public String getHandleType() {
        return "group_message";
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
