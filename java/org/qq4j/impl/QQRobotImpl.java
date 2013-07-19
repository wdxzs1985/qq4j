package org.qq4j.impl;

import net.sf.json.JSONException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQContext;
import org.qq4j.core.QQGroupManager;
import org.qq4j.core.QQLogin;
import org.qq4j.core.QQMessagePoller;
import org.qq4j.core.QQRobot;
import org.qq4j.core.QQSender;
import org.qq4j.core.QQUserManager;
import org.qq4j.core.exception.NeedVerifyCodeException;
import org.qq4j.domain.QQUser;

/**
 * 
 */
public class QQRobotImpl implements QQRobot {

    private static final Log LOG = LogFactory.getLog(QQRobotImpl.class);

    private QQLogin login = null;

    private QQContext context = null;

    private QQMessagePoller messagePoller = null;

    @Override
    public void login(final long account, final String password)
                                                                throws NeedVerifyCodeException {
        QQUser self = null;
        try {
            self = this.login.login(account, password);
            this.postLogin(self);
        } catch (final JSONException e) {
            QQRobotImpl.LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void login(final long account,
                      final String password,
                      final String verifyCode) {
        QQUser self = null;
        try {
            self = this.login.login(account, password, verifyCode);
            this.postLogin(self);
        } catch (final JSONException e) {
            QQRobotImpl.LOG.error(e.getMessage(), e);
        }
    }

    private void postLogin(final QQUser self) {
        if (self != null) {
            this.context.setRun(true);
            this.context.setSelf(self);
            this.login.online(this.context);
        } else {
            this.context.setRun(false);
            QQRobotImpl.LOG.error("QQ登录失败！");
        }
    }

    @Override
    public void startup() {
        if (this.context.isRun()) {
            // sender
            final QQSender sender = this.context.getSender();
            sender.initSender();
            // friend manager
            final QQUserManager friendManager = this.context.getFriendManager();
            friendManager.initFriendsInfo();
            // friendManager.initBlackList();
            // group manager
            final QQGroupManager groupManager = this.context.getGroupManager();
            groupManager.initGroupInfo();
            //
            this.startPoller(this.context);
        }
    }

    private void startPoller(final QQContext context) {
        final QQUser self = context.getSelf();
        QQRobotImpl.LOG.info(String.format("QQ开始运行！QQ:%s", self));
        new Thread(this.messagePoller).start();
    }

    @Override
    public void shutdown() {
        if (this.isRun()) {
            this.login.offline(this.context);
            this.context.setRun(false);
            final QQUser self = this.context.getSelf();
            QQRobotImpl.LOG.info(String.format("QQ停止运行！QQ:%s", self));
        }
    }

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

    public QQLogin getLogin() {
        return this.login;
    }

    public void setLogin(final QQLogin login) {
        this.login = login;
    }

    @Override
    public boolean isRun() {
        return this.context.isRun();
    }

    @Override
    public String getName() {
        return this.getContext().getSelf().getNick();
    }

    @Override
    public byte[] downloadVerifyImage(final long account) {
        return this.login.downloadVerifyImage(account);
    }

    @Override
    public boolean isNeedVerify() {
        return this.login.isNeedVerify();
    }

}
