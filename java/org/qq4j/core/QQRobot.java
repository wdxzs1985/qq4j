package org.qq4j.core;

import org.qq4j.domain.QQUser;

public interface QQRobot {

    QQLogin getLogin();

    void startup(QQUser self);

    void shutdown();

    boolean isRun();

    String getName();

}
