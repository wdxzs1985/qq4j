package org.qq4j.core.handler.command;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.qq4j.core.QQAiManager;
import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQCommandHandler;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQUser;

import atg.taglib.json.util.JSONException;

public class QQFaithCommandHandler implements QQCommandHandler {

    private String answer1 = null;
    private String answer2 = null;

    @Override
    public void handle(final QQContext context,
                       final QQUser user,
                       final String message)
            throws UnsupportedEncodingException, JSONException {
        final String messageToSend = this.getAnswer(context, user);
        context.getSender().sendToUser(user, messageToSend);
    }

    @Override
    public void handleGroup(final QQContext context,
                            final QQGroup group,
                            final QQUser user,
                            final String message)
            throws UnsupportedEncodingException, JSONException {
        final String messageToSend = this.getAnswer(context, user);
        context.getSender().sendToGroup(group, messageToSend);
    }

    private String getAnswer(final QQContext context, final QQUser user) {
        final QQAiManager aiManager = context.getAiManager();
        if (aiManager.hasFaith(user)) {
            final Map<String, Object> result = aiManager.queryRank(user);
            final BigDecimal faith = (BigDecimal) result.get("faith");
            final BigDecimal rank = (BigDecimal) result.get("rank");

            final Map<String, Object> valueMap = new HashMap<String, Object>();
            valueMap.put("nick", user.getNick());
            valueMap.put("faith", faith.intValue());
            valueMap.put("rank", rank.intValue());
            return StrSubstitutor.replace(this.getAnswer1(), valueMap);
        } else {
            final Map<String, Object> valueMap = new HashMap<String, Object>();
            valueMap.put("nick", user.getNick());
            return StrSubstitutor.replace(this.getAnswer2(), valueMap);
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
