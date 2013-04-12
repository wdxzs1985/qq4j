package org.qq4j.core.handler;

import org.qq4j.core.QQContext;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQUser;

public interface QQCommandHandler {

    public void handle(QQContext context, QQUser user, final String message);

    public void handleGroup(QQContext context,
                            QQGroup group,
                            QQUser user,
                            final String message);

}
