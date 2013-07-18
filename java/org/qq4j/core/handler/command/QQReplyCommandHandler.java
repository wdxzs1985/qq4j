package org.qq4j.core.handler.command;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.qq4j.core.QQAiManager;
import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQCommandHandler;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQUser;
import org.springframework.beans.factory.annotation.Autowired;

public class QQReplyCommandHandler implements QQCommandHandler {

    @Autowired
    private QQAiManager aiManager = null;

    private int echoProbability = 0;

    private int maxEcho = 0;

    @Override
    public void handle(final QQContext context,
                       final QQUser user,
                       final String message) {
        final String answer = this.getReplyAnswer(message, user);
        context.getSender().sendToUser(user, answer);
    }

    @Override
    public void handleGroup(final QQContext context,
                            final QQGroup group,
                            final QQUser user,
                            final String message) {
        final String answer = this.getReplyAnswer(message, user);
        context.getSender().sendToGroup(group, answer);
    }

    private String getReplyAnswer(final String message, final QQUser user) {
        String answer = this.aiManager.getReplyAnswer(message, user);
        if (StringUtils.isBlank(answer)
            && StringUtils.length(message) < this.maxEcho) {
            final int random = RandomUtils.nextInt(100);
            if (random < this.echoProbability) {
                answer = this.buildEcho(message);
            } else {
                return null;
            }
        } else if (StringUtils.equals(answer, "[屏蔽]")) {
            return null;
        }
        return answer;
    }

    public String buildEcho(final String message) {
        final StringBuilder source = new StringBuilder();
        final StringBuilder echo = new StringBuilder();
        if (StringUtils.isNotBlank(message)) {
            source.append(message);
            while (source.length() > 1) {
                echo.append(source).append(SystemUtils.LINE_SEPARATOR);
                source.deleteCharAt(0);
            }
            echo.append(source);
        }
        return echo.toString();
    }

    public int getEchoProbability() {
        return this.echoProbability;
    }

    public void setEchoProbability(final int echoProbability) {
        this.echoProbability = echoProbability;
    }

    public int getMaxEcho() {
        return this.maxEcho;
    }

    public void setMaxEcho(final int maxEcho) {
        this.maxEcho = maxEcho;
    }
}
