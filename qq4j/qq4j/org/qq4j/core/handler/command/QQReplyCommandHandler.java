package org.qq4j.core.handler.command;

import org.apache.commons.lang3.StringUtils;
import org.qq4j.core.QQAiManager;
import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQCommandHandler;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQUser;

public class QQReplyCommandHandler implements QQCommandHandler {

    private QQAiManager aiManager = null;

    @Override
    public void handle(final QQContext context,
                       final QQUser user,
                       final String message) {
        final long account = context.getSelf().getAccount();
        final long owner = user.getAccount();
        final String answer = this.getAiManager().getReplyAnswer(message,
                                                                 account,
                                                                 owner);
        if (StringUtils.isNotBlank(answer)) {
            context.getSender().sendToUser(user, answer);
        }
    }

    @Override
    public void handleGroup(final QQContext context,
                            final QQGroup group,
                            final QQUser user,
                            final String message) {
        final long account = context.getSelf().getAccount();
        final long owner = user.getAccount();
        final String answer = this.getAiManager().getReplyAnswer(message,
                                                                 account,
                                                                 owner);
        if (StringUtils.isNotBlank(answer) && !StringUtils.equals(answer,
                                                                  "[屏蔽]")) {
            context.getSender().sendToGroup(group, answer);
        }
    }

    public QQAiManager getAiManager() {
        return this.aiManager;
    }

    public void setAiManager(final QQAiManager aiManager) {
        this.aiManager = aiManager;
    }

}