package org.qq4j.core.handler.message;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.common.QQMessageParser;
import org.qq4j.common.SystemConstants;
import org.qq4j.core.QQContext;
import org.qq4j.core.QQFriendManager;
import org.qq4j.core.QQGroupManager;
import org.qq4j.core.handler.QQMessageHandler;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQGroupMember;
import org.qq4j.domain.QQUser;

public class QQGroupMessageHandler extends BaseMessageHandler implements QQMessageHandler {

    private final Log log = LogFactory.getLog(QQGroupMessageHandler.class);

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
            final QQFriendManager friendManager = context.getFriendManager();
            final QQGroup group = groupManager.getQQGroup(gCode);
            final QQUser self = context.getUserManager().getSelf();
            if (group != null) {
                final QQGroupMember member = group.getMembers().get(uin);
                if (member != null) {
                    if (member.getUser() == null) {
                        final QQUser user = friendManager.getQQUser(uin);
                        if (user == null) {
                            return;
                        }
                        member.setUser(user);
                    }
                    this.log.info(String.format("[%s-%d]%s >> %s%s发送消息：%s",
                                                DateFormatUtils.format(time,
                                                                       SystemConstants.DATETIME_FORMAT),
                                                msgId,
                                                self,
                                                group,
                                                member,
                                                message));
                    synchronized (member) {
                        final QQUser user = member.getUser();
                        if (msgId != member.getUser().getLastMsgId()
                            && System.currentTimeMillis()
                               - time < this.getReplyTimeLimit()) {
                            if (this.isRepeat(user, message)) {
                                this.handleRepeat(context, group, user);
                            } else {
                                this.getHandlers().handleGroup(context,
                                                               group,
                                                               member,
                                                               message);
                            }
                        }
                        user.setLastMsgId(msgId);
                        user.setLastMsg(message);
                    }
                }
            }
        }
    }

    protected void handleRepeat(final QQContext context,
                                final QQGroup group,
                                final QQUser user) {
        final String answer = this.selectRepeatAnswer(user);
        context.getSender().sendToGroup(group, answer);
    }

    @Override
    public String getHandleType() {
        return "group_message";
    }
}
