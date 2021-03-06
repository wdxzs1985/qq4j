package org.qq4j.core.handler;

import org.qq4j.core.QQContext;
import org.qq4j.core.QQSession;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQGroupMember;
import org.qq4j.domain.QQUser;

public class QQSessionHandler implements QQCommandHandler {

    public static final String SESSION_HANDLER = QQSessionHandler.class.getName();

    private QQCommandHandler defaultHandler = null;

    @Override
    public void handle(final QQContext context,
                       final QQUser user,
                       final String message) {
        final QQSession session = context.getSessionManager().getSession(user);
        QQCommandHandler handler = null;
        if (session != null) {
            handler = (QQCommandHandler) session.get(QQSessionHandler.SESSION_HANDLER);
        }
        handler = handler == null ? this.getDefaultHandler() : handler;
        handler.handle(context, user, message);
    }

    @Override
    public void handleGroup(final QQContext context,
                            final QQGroup group,
                            final QQGroupMember member,
                            final String message) {
        final QQSession session = context.getSessionManager()
                                         .getSession(member.getUser());
        QQCommandHandler handler = null;
        if (session != null) {
            handler = (QQCommandHandler) session.get(QQSessionHandler.SESSION_HANDLER);
        }
        handler = handler == null ? this.getDefaultHandler() : handler;
        handler.handleGroup(context, group, member, message);
    }

    public QQCommandHandler getDefaultHandler() {
        return this.defaultHandler;
    }

    public void setDefaultHandler(final QQCommandHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

}
