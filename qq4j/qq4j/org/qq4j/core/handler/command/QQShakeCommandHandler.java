package org.qq4j.core.handler.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQCommandHandler;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQUser;

public class QQShakeCommandHandler implements QQCommandHandler {

    private final static Log LOG = LogFactory.getLog(QQShakeCommandHandler.class);

    @Override
    public void handle(final QQContext context,
                       final QQUser user,
                       final String message) {
        if (QQShakeCommandHandler.LOG.isDebugEnabled()) {
            QQShakeCommandHandler.LOG.debug(String.format("%s >> 给%s发送一个窗口抖动。",
                                                          context.getSelf(),
                                                          user));
        }
        context.getSender().sendShakeMessage(user);
    }

    @Override
    public void handleGroup(final QQContext context,
                            final QQGroup group,
                            final QQUser user,
                            final String message) {
    }

}
