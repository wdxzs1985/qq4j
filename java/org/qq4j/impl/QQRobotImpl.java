package org.qq4j.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQContext;
import org.qq4j.core.QQFriendManager;
import org.qq4j.core.QQGroupManager;
import org.qq4j.core.QQMessagePoller;
import org.qq4j.core.QQRobot;
import org.qq4j.core.QQSender;
import org.qq4j.domain.QQUser;

/**
 * 
 */
public class QQRobotImpl implements QQRobot {

    private static final Log LOG = LogFactory.getLog(QQRobotImpl.class);

    private QQContext context = null;

    private QQMessagePoller messagePoller = null;

    @Override
    public void startup(final QQUser self) {
        this.context.setRun(true);
        this.context.getUserManager().online();
        // sender
        final QQSender sender = this.context.getSender();
        sender.initSender();
        // friend manager
        final QQFriendManager friendManager = this.context.getFriendManager();
        friendManager.initFriendsInfo();
        // friendManager.initBlackList();
        // group manager
        final QQGroupManager groupManager = this.context.getGroupManager();
        groupManager.initGroupInfo();
        //
        this.startPoller(this.context);
    }

    private void startPoller(final QQContext context) {
        final QQUser self = context.getUserManager().getSelf();
        QQRobotImpl.LOG.info(String.format("QQ开始运行！QQ:%s", self));
        new Thread(this.messagePoller).start();
    }

    @Override
    public void shutdown() {
        if (this.context.isRun()) {
            this.context.setRun(false);
            this.context.getUserManager().offline();
            final QQUser self = this.context.getUserManager().getSelf();
            QQRobotImpl.LOG.info(String.format("QQ停止运行！QQ:%s", self));
        }
    }

    @Override
    public QQContext getContext() {
        return this.context;
    }

    public void setContext(final QQContext context) {
        this.context = context;
    }

    public QQMessagePoller getMessagePoller() {
        return this.messagePoller;
    }

    public void setMessagePoller(final QQMessagePoller messagePoller) {
        this.messagePoller = messagePoller;
    }

}
