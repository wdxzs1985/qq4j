package org.qq4j.core.handler.message;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.qq4j.core.handler.QQCommandHandlerMapping;
import org.qq4j.domain.QQUser;

public abstract class BaseMessageHandler {

    protected QQCommandHandlerMapping handlers = null;
    private int replyTimeLimit = 1000;
    protected List<String> repeatAnswer = null;

    protected boolean isRepeat(final QQUser user, final String message) {
        if (StringUtils.equals(user.getLastMsg(), message)) {
            user.setRepeatTimes(user.getRepeatTimes() + 1);
            return true;
        } else {
            user.setRepeatTimes(0);
        }
        return false;
    }

    protected String selectRepeatAnswer(final QQUser user) {
        String answer = null;
        if (user.getRepeatTimes() < this.repeatAnswer.size()) {
            Collections.shuffle(this.repeatAnswer);
            answer = this.repeatAnswer.get(0);
        } else {
            answer = null;
        }
        return answer;
    }

    public int getReplyTimeLimit() {
        return this.replyTimeLimit;
    }

    public void setReplyTimeLimit(final int replyTimeLimit) {
        this.replyTimeLimit = replyTimeLimit;
    }

    public List<String> getRepeatAnswer() {
        return this.repeatAnswer;
    }

    public void setRepeatAnswer(final List<String> repeatAnswer) {
        this.repeatAnswer = repeatAnswer;
    }

    public QQCommandHandlerMapping getHandlers() {
        return this.handlers;
    }

    public void setHandlers(final QQCommandHandlerMapping handlers) {
        this.handlers = handlers;
    }

}