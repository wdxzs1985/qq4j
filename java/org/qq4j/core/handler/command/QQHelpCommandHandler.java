package org.qq4j.core.handler.command;

import java.util.Map;
import java.util.Map.Entry;

import org.qq4j.common.SystemConstants;
import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQCommandHandler;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQGroupMember;
import org.qq4j.domain.QQUser;

public class QQHelpCommandHandler implements QQCommandHandler {

    private Map<String, String> mapping = null;

    @Override
    public void handle(final QQContext context,
                       final QQUser user,
                       final String message) {
        final String answer = this.getAnswer(context.getUserManager().getSelf());
        context.getSender().sendToUser(user, answer);
    }

    @Override
    public void handleGroup(final QQContext context,
                            final QQGroup group,
                            final QQGroupMember member,
                            final String message) {
        final String answer = this.getAnswer(context.getUserManager().getSelf());
        context.getSender().sendToGroup(group, answer);
    }

    private String getAnswer(final QQUser self) {
        final StringBuilder answer = new StringBuilder();
        answer.append(self.getNick()).append("现在会做以下几件事：");
        for (final Entry<String, String> next : this.getMapping().entrySet()) {
            answer.append(SystemConstants.LINE_SEPARATOR)
                  .append(next.getKey())
                  .append("  :  ")
                  .append(next.getValue());
        }
        return answer.toString();
    }

    public Map<String, String> getMapping() {
        return this.mapping;
    }

    public void setMapping(final Map<String, String> mapping) {
        this.mapping = mapping;
    }
}
