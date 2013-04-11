package org.qq4j.core.handler.command;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.SystemUtils;
import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQCommandHandler;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQUser;

import atg.taglib.json.util.JSONException;

public class QQHelpCommandHandler implements QQCommandHandler {

    private Map<String, String> mapping = null;

    @Override
    public void handle(final QQContext context,
                       final QQUser user,
                       final String message)
            throws UnsupportedEncodingException, JSONException {
        final String answer = this.getAnswer(context.getSelf());
        context.getSender().sendToUser(user, answer);
    }

    @Override
    public void handleGroup(final QQContext context,
                            final QQGroup group,
                            final QQUser user,
                            final String message)
            throws UnsupportedEncodingException, JSONException {
        final String answer = this.getAnswer(context.getSelf());
        context.getSender().sendToGroup(group, answer);
    }

    private String getAnswer(final QQUser self) {
        final StringBuilder answer = new StringBuilder();
        answer.append(self.getNick()).append("现在会做以下几件事：");
        for (final Entry<String, String> next : this.getMapping().entrySet()) {
            answer.append(SystemUtils.LINE_SEPARATOR)
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
