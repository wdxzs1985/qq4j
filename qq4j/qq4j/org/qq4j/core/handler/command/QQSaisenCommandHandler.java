package org.qq4j.core.handler.command;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQCommandHandler;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQSaisen;
import org.qq4j.domain.QQUser;

import atg.taglib.json.util.JSONException;
import framework.SystemConstants;

public class QQSaisenCommandHandler implements QQCommandHandler {

    private String answer1 = null;
    private String answer2 = null;
    private String answer3 = null;

    private int timesLimit = 0;
    private String timeLimitFormat = null;
    private Map<Long, QQSaisen> timesMapping = new HashMap<Long, QQSaisen>();

    @Override
    public void handle(final QQContext context,
                       final QQUser user,
                       final String message)
            throws UnsupportedEncodingException, JSONException {
        final QQSaisen saisen = this.increaseTimes(user);
        final String answer = this.getAnswer(context, user, saisen);
        context.getSender().sendToUser(user, answer);
    }

    @Override
    public void handleGroup(final QQContext context,
                            final QQGroup group,
                            final QQUser user,
                            final String message)
            throws UnsupportedEncodingException, JSONException {
        final QQSaisen saisen = this.increaseTimes(user);
        final String answer = this.getAnswer(context, user, saisen);
        context.getSender().sendToGroup(group, answer);
    }

    private QQSaisen increaseTimes(final QQUser user) {
        QQSaisen saisen = this.timesMapping.get(user.getAccount());
        //
        final String time = DateFormatUtils.format(System.currentTimeMillis(),
                                                   "yyyyMMddHH",
                                                   SystemConstants.DEFAULT_LOCALE);
        //
        if (saisen == null || !saisen.getDate().equals(time)) {
            saisen = new QQSaisen();
            saisen.setDate(time);

            this.timesMapping.put(user.getAccount(), saisen);
        }
        saisen.increaseTimes();

        return saisen;
    }

    private String getAnswer(final QQContext context,
                             final QQUser user,
                             final QQSaisen saisen) {

        String answer = null;
        if (saisen.getTimes() <= this.timesLimit) {
            final int random = new Random().nextInt(100);
            if (random % 2 == 0) {
                context.getAiManager().increaseFaith(user, 1);
                if (StringUtils.isNotBlank(this.answer1)) {
                    final Map<String, String> valueMap = new HashMap<String, String>();
                    valueMap.put("nick", user.getNick());
                    answer = StrSubstitutor.replace(this.answer1, valueMap);
                }

            } else {
                if (StringUtils.isNotBlank(this.answer2)) {
                    final Map<String, String> valueMap = new HashMap<String, String>();
                    valueMap.put("nick", user.getNick());
                    answer = StrSubstitutor.replace(this.answer2, valueMap);
                }
            }
        } else {
            if (StringUtils.isNotBlank(this.answer3)) {
                final Map<String, String> valueMap = new HashMap<String, String>();
                valueMap.put("nick", user.getNick());
                answer = StrSubstitutor.replace(this.answer3, valueMap);
            }
        }
        return answer;
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

    public String getAnswer3() {
        return this.answer3;
    }

    public void setAnswer3(final String answer3) {
        this.answer3 = answer3;
    }

    public int getTimesLimit() {
        return this.timesLimit;
    }

    public void setTimesLimit(final int timesLimit) {
        this.timesLimit = timesLimit;
    }

    public String getTimeLimitFormat() {
        return this.timeLimitFormat;
    }

    public void setTimeLimitFormat(final String timeLimitFormat) {
        this.timeLimitFormat = timeLimitFormat;
    }
}
