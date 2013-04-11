package org.qq4j.core.handler.command;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQContext;
import org.qq4j.core.QQSender;
import org.qq4j.core.QQSession;
import org.qq4j.core.handler.QQCommandHandler;
import org.qq4j.core.handler.QQSessionHandler;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQUser;

import atg.taglib.json.util.JSONException;

public class QQEchoCommandHandler implements QQCommandHandler {

    private static final Log LOG = LogFactory.getLog(QQEchoCommandHandler.class);

    private String answer;

    @Override
    public void handle(final QQContext context,
                       final QQUser user,
                       final String message)
            throws UnsupportedEncodingException, JSONException {
        if (QQEchoCommandHandler.LOG.isDebugEnabled()) {
            QQEchoCommandHandler.LOG.debug(String.format("%s进入回音模式", user));
        }
        final QQSession session = context.getSessionManager().getSession(user);

        final QQSender sender = context.getSender();
        if (StringUtils.isNotBlank(message)) {
            final String echo = this.buildEcho(message);
            sender.sendToUser(user, echo);
            session.remove(QQSessionHandler.SESSION_HANDLER);
        } else {
            if (StringUtils.isNotBlank(this.answer)) {
                sender.sendToUser(user, this.answer);
                session.put(QQSessionHandler.SESSION_HANDLER, this);
            }
        }
    }

    @Override
    public void handleGroup(final QQContext context,
                            final QQGroup group,
                            final QQUser user,
                            final String message)
            throws UnsupportedEncodingException, JSONException {
        if (QQEchoCommandHandler.LOG.isDebugEnabled()) {
            QQEchoCommandHandler.LOG.debug(String.format("%s进入回音模式", user));
        }
        final QQSession session = context.getSessionManager().getSession(user);

        final QQSender sender = context.getSender();
        if (StringUtils.isNotBlank(message)) {
            final String echo = this.buildEcho(message);
            sender.sendToGroup(group, echo);
            session.remove(QQSessionHandler.SESSION_HANDLER);
        } else {
            if (StringUtils.isNotBlank(this.answer)) {
                sender.sendToGroup(group, this.answer);
                session.put(QQSessionHandler.SESSION_HANDLER, this);
            }
        }
    }

    public String buildEcho(final CharSequence message) {
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

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(final String answer) {
        this.answer = answer;
    }

}
