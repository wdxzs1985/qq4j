package org.qq4j.core.handler.command;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQContext;
import org.qq4j.core.QQSender;
import org.qq4j.core.QQSession;
import org.qq4j.core.handler.QQCommandHandler;
import org.qq4j.core.handler.QQSessionHandler;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQUser;
import org.sina4j.impl.SinaRobot;

import atg.taglib.json.util.JSONException;

public class QQSinaweiboCommandHandler implements QQCommandHandler {

    private Log log = LogFactory.getLog(QQSinaweiboCommandHandler.class);

    private SinaRobot sinaRobot = null;

    private String answer1 = null;// 发微博
    private String answer2 = null;// 微博发送成功，查看微博请关注@天才_琪露诺
    private String answer3 = null;// 微博发送成功，查看微博请关注@天才_琪露诺
    private String answer4 = null;// 微博发送成功，查看微博请关注@天才_琪露诺
    private String answer5 = null;// 微博发送成功，查看微博请关注@天才_琪露诺
    private String command1 = null; // 放弃

    @Override
    public void handle(final QQContext context,
                       final QQUser user,
                       final String message)
            throws UnsupportedEncodingException, JSONException {

        if (this.log.isDebugEnabled()) {
            this.log.debug(String.format("%s进入微博模式", user));
        }
        final QQSession session = context.getSessionManager().getSession(user);
        new StringBuilder();
        final QQSender sender = context.getSender();
        if (StringUtils.isNotBlank(message)) {
            if (StringUtils.equals(message, this.command1)) {
                sender.sendToUser(user, this.answer2);
                session.remove(QQSessionHandler.SESSION_HANDLER);
            } else if (StringUtils.length(message) < 140) {

                if (this.sinaRobot.sendWeibo(message)) {
                    sender.sendToUser(user, this.answer3);
                } else {
                    sender.sendToUser(user, this.answer4);
                }
                session.remove(QQSessionHandler.SESSION_HANDLER);
            } else {
                sender.sendToUser(user, this.answer5);
            }
        } else {
            sender.sendToUser(user, this.answer1);
            session.put(QQSessionHandler.SESSION_HANDLER, this);
        }
    }

    @Override
    public void handleGroup(final QQContext context,
                            final QQGroup group,
                            final QQUser user,
                            final String message)
            throws UnsupportedEncodingException, JSONException {
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

    public String getAnswer4() {
        return this.answer4;
    }

    public void setAnswer4(final String answer4) {
        this.answer4 = answer4;
    }

    public String getCommand1() {
        return this.command1;
    }

    public void setCommand1(final String command1) {
        this.command1 = command1;
    }

    public String getAnswer5() {
        return this.answer5;
    }

    public void setAnswer5(final String answer5) {
        this.answer5 = answer5;
    }

    public SinaRobot getSinaRobot() {
        return this.sinaRobot;
    }

    public void setSinaRobot(final SinaRobot sinaRobot) {
        this.sinaRobot = sinaRobot;
    }

}
