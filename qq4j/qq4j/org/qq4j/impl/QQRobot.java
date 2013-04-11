package org.qq4j.impl;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQContext;
import org.qq4j.core.QQGroupManager;
import org.qq4j.core.QQHttpClient;
import org.qq4j.core.QQLogin;
import org.qq4j.core.QQMessagePoller;
import org.qq4j.core.QQSender;
import org.qq4j.core.QQUserManager;
import org.qq4j.domain.QQUser;
import org.qq4j.factory.QQThreadFactory;

import atg.taglib.json.util.JSONException;

/**
 * 
 */
public class QQRobot {

    private static final Log LOG = LogFactory.getLog(QQRobot.class);

    private static final Executor threadExecutor = Executors.newFixedThreadPool(10,
                                                                                new QQThreadFactory("robot"));

    public static final void shutdown() {
        final ThreadPoolExecutor executor = (ThreadPoolExecutor) QQRobot.threadExecutor;
        executor.shutdownNow();
    }

    private QQContext context = null;

    private QQMessagePoller messagePoller = null;

    private QQLogin login = null;

    public void restart() {
        if (this.context.isRun()) {
            this.reLogin();
        } else {
            this.startup();
        }
    }

    public void startup() {
        this.context.setClientid(new Random().nextInt(10000000));
        this.context.setHttpClient(new QQHttpClient());

        if (this.doLogin(this.context)) {
            //
            final QQSender sender = this.context.getSender();
            // sender.setContext(this.context);
            sender.initSender();
            // friend manager
            final QQUserManager friendManager = this.context.getFriendManager();
            // friendManager.setContext(this.context);
            friendManager.initFriendsInfo();
            friendManager.initBlackList();
            // group manager
            final QQGroupManager groupManager = this.context.getGroupManager();
            // groupManager.setContext(this.context);
            groupManager.initGroupInfo();
            //
            this.context.setRun(true);
            //
            this.startPoller(this.context);
        }
    }

    public void reLogin() {
        this.context.setHttpClient(new QQHttpClient());
        if (this.doLogin(this.context)) {
            final QQUserManager friendManager = this.context.getFriendManager();
            friendManager.initFriendsInfo();
            friendManager.initBlackList();
            final QQGroupManager groupManager = this.context.getGroupManager();
            groupManager.initGroupInfo();
        }
    }

    private boolean doLogin(final QQContext context) {
        try {
            if (this.login.login(context)) {
                this.login.online(context);
                return true;
            }
        } catch (final UnsupportedEncodingException e) {
            QQRobot.LOG.error(e.getMessage(), e);
        } catch (final JSONException e) {
            QQRobot.LOG.error(e.getMessage(), e);
        }

        this.context.setRun(false);
        QQRobot.LOG.error(String.format("QQ登录失败！QQ:%d,密码:%s",
                                        this.context.getSelf().getAccount(),
                                        this.context.getSelf().getPassword()));
        return false;
    }

    private void startPoller(final QQContext context) {
        final QQUser self = context.getSelf();
        QQRobot.LOG.info(String.format("QQ开始运行！QQ:%s", self));
        // this.messagePoller = new QQMessagePoller();
        // this.messagePoller.setContext(context);
        // this.messagePoller.initHandler();
        // new Thread(messagePoller).run();
        QQRobot.threadExecutor.execute(this.messagePoller);
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

}
