package org.qq4j.core.handler;

import java.util.Map;

import org.qq4j.core.QQContext;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQGroupMember;
import org.qq4j.domain.QQUser;

public class QQCommandHandlerMapping implements QQCommandHandler {

    private Map<String, QQCommandHandler> handlers;

    private QQCommandHandler defaultHandler;

    @Override
    public void handle(final QQContext context,
                       final QQUser user,
                       final String message) {
        final String command = message;
        if (this.getHandlers().containsKey(command)) {
            this.getHandlers().get(command).handle(context, user, null);
        } else if (this.defaultHandler != null) {
            this.defaultHandler.handle(context, user, message);
        }
    }

    @Override
    public void handleGroup(final QQContext context,
                            final QQGroup group,
                            final QQGroupMember member,
                            final String message) {
        final String command = message;
        if (this.getHandlers().containsKey(command)) {
            this.getHandlers()
                .get(command)
                .handleGroup(context, group, member, null);
        } else if (this.defaultHandler != null) {
            this.defaultHandler.handleGroup(context, group, member, message);
        }
    }

    public QQCommandHandler getDefaultHandler() {
        return this.defaultHandler;
    }

    public void setDefaultHandler(final QQCommandHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    public Map<String, QQCommandHandler> getHandlers() {
        return this.handlers;
    }

    public void setHandlers(final Map<String, QQCommandHandler> handlers) {
        this.handlers = handlers;
    }
}
