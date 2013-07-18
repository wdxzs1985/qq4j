package org.qq4j.core.handler.command;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;
import org.qq4j.core.QQAiManager;
import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQCommandHandler;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQUser;
import org.qq4j.net.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;

public class QQFaithCommandHandler implements QQCommandHandler {

    @Autowired
    private QQAiManager aiManager = null;

    private String answer1 = null;
    private String answer2 = null;

    @Override
    public void handle(final QQContext context,
                       final QQUser user,
                       final String message) {
        final String messageToSend = this.getAnswer(context, user);
        context.getSender().sendToUser(user, messageToSend);
    }

    @Override
    public void handleGroup(final QQContext context,
                            final QQGroup group,
                            final QQUser user,
                            final String message) {
        final String messageToSend = this.getAnswer(context, user);
        context.getSender().sendToGroup(group, messageToSend);
    }

    private String getAnswer(final QQContext context, final QQUser user) {
        final QQUser result = this.aiManager.queryRank(user);
        final Map<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put("nick", user.getNick());
        if (result == null) {
            return StrSubstitutor.replace(this.getAnswer2(),
                                          valueMap,
                                          SystemConstants.REPLACE_PREFIX,
                                          SystemConstants.REPLACE_SUFFIX);
        } else {
            valueMap.put("faith", result.getFaith());
            valueMap.put("rank", result.getRank());
            return StrSubstitutor.replace(this.getAnswer1(),
                                          valueMap,
                                          SystemConstants.REPLACE_PREFIX,
                                          SystemConstants.REPLACE_SUFFIX);
        }

    }

    public String getAnswer1() {
        return this.answer1;
    }

    public void setAnswer1(final String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return this.answer2;
    }

    public void setAnswer2(final String answer2) {
        this.answer2 = answer2;
    }
}
