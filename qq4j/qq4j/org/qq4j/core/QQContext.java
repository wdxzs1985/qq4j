package org.qq4j.core;

import java.util.Random;

import org.qq4j.domain.QQFont;
import org.qq4j.domain.QQUser;

public class QQContext {

    private long clientid = new Random().nextInt(10000000);

    private QQHttpClient httpClient = null;

    private QQUser self = null;

    private QQUserManager friendManager = null;

    private QQGroupManager groupManager = null;

    private QQSessionManager sessionManager = null;

    private QQSender sender = null;

    private QQFont font = null;

    private String psessionid = null;

    private String ptwebqq = null;

    private String vfwebqq = null;

    private String skey = null;

    private boolean run = false;

    public Long getClientid() {
        return this.clientid;
    }

    public String getPsessionid() {
        return this.psessionid;
    }

    public void setPsessionid(final String psessionid) {
        this.psessionid = psessionid;
    }

    public String getPtwebqq() {
        return this.ptwebqq;
    }

    public void setPtwebqq(final String ptwebqq) {
        this.ptwebqq = ptwebqq;
    }

    public String getVfwebqq() {
        return this.vfwebqq;
    }

    public void setVfwebqq(final String vfwebqq) {
        this.vfwebqq = vfwebqq;
    }

    public String getSkey() {
        return this.skey;
    }

    public void setSkey(final String skey) {
        this.skey = skey;
    }

    public boolean isRun() {
        return this.run;
    }

    public void setRun(final boolean run) {
        this.run = run;
    }

    public QQUserManager getFriendManager() {
        return this.friendManager;
    }

    public QQGroupManager getGroupManager() {
        return this.groupManager;
    }

    public void setFriendManager(final QQUserManager friendManager) {
        this.friendManager = friendManager;
    }

    public void setGroupManager(final QQGroupManager groupManager) {
        this.groupManager = groupManager;
    }

    public QQSender getSender() {
        return this.sender;
    }

    public void setSender(final QQSender sender) {
        this.sender = sender;
    }

    public QQHttpClient getHttpClient() {
        return this.httpClient;
    }

    public QQUser getSelf() {
        return this.self;
    }

    public QQFont getFont() {
        return this.font;
    }

    public void setFont(final QQFont font) {
        this.font = font;
    }

    public void setSelf(final QQUser user) {
        this.self = user;
    }

    public QQSessionManager getSessionManager() {
        return this.sessionManager;
    }

    public void setSessionManager(final QQSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setHttpClient(final QQHttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
