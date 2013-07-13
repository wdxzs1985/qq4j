package org.qq4j.core.handler.command;

import java.util.Map;

import org.apache.commons.lang.SystemUtils;
import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQCommandHandler;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQUser;

public class QQViewDataCommandHandler implements QQCommandHandler {

    private long admin;

    @Override
    public void handle(final QQContext context,
                       final QQUser user,
                       final String message) {
        context.getSender().sendToUser(user, this.getAnswer(context, user));
    }

    @Override
    public void handleGroup(final QQContext context,
                            final QQGroup group,
                            final QQUser user,
                            final String message) {
        context.getSender().sendToGroup(group, this.getAnswer(context, null));
    }

    private String getAnswer(final QQContext context, final QQUser user) {
        final Map<Long, QQUser> friendList = context.getFriendManager()
                                                    .getUsers();
        final Map<Long, QQGroup> groupList = context.getGroupManager()
                                                    .getGroups();
        final boolean isAdmin = user != null && this.getAdmin() == user.getAccount();
        //
        final StringBuilder answer = new StringBuilder();
        answer.append("☆已有").append(friendList.size()).append("个好友。☆");
        if (isAdmin) {
            for (final QQUser next : friendList.values()) {
                answer.append(SystemUtils.LINE_SEPARATOR).append(next);
            }
        }
        answer.append(SystemUtils.LINE_SEPARATOR);
        answer.append("☆已加入").append(groupList.size()).append("个群。☆");
        if (isAdmin) {
            for (final QQGroup group : groupList.values()) {
                answer.append(SystemUtils.LINE_SEPARATOR).append(group);
            }
        }
        return answer.toString();
    }

    public long getAdmin() {
        return this.admin;
    }

    public void setAdmin(final long admin) {
        this.admin = admin;
    }
}
