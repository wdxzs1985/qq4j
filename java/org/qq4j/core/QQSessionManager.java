package org.qq4j.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.qq4j.domain.QQUser;

public class QQSessionManager {

    private final Map<QQUser, QQSession> sessionContainer;

    public QQSessionManager() {
        final Map<QQUser, QQSession> m = new HashMap<QQUser, QQSession>();
        this.sessionContainer = Collections.synchronizedMap(m);
    }

    public QQSession getSession(final QQUser user) {
        QQSession session = this.sessionContainer.get(user);
        if (session == null) {
            session = new QQSession();
            this.sessionContainer.put(user, session);
        }
        return session;
    }

}
