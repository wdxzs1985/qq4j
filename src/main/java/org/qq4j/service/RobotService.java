package org.qq4j.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.qq4j.core.QQRobot;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class RobotService implements ApplicationContextAware {

    private Map<Long, QQRobot> pool = Collections.synchronizedMap(new HashMap<Long, QQRobot>());

    public Collection<QQRobot> getRobotList() {
        return this.pool.values();
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext)
                                                                                  throws BeansException {
        this.pool.clear();
        final Map<String, QQRobot> robotBeansMap = applicationContext.getBeansOfType(QQRobot.class);
        for (final QQRobot robot : robotBeansMap.values()) {
            final long account = robot.getContext()
                                      .getUserManager()
                                      .getSelf()
                                      .getAccount();
            this.pool.put(account, robot);
        }
    }

    public QQRobot getRobot(final long account) {
        final QQRobot robot = this.pool.get(account);
        return robot;
    }

}
