package org.qq4j.core;

import org.qq4j.domain.QQUser;

public interface QQRobot {

    QQContext getContext();

    void startup(QQUser self);

    void shutdown();
}
