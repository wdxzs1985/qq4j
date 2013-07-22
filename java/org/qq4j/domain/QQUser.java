package org.qq4j.domain;

import java.io.Serializable;

import org.qq4j.core.QQAccount;
import org.qq4j.core.QQConstants;

public class QQUser implements Serializable, QQAccount {
    /**
     * 
     */
    private static final long serialVersionUID = -4977525002155724113L;

    private String hexUin = null;

    private long uin = 0;

    private long account = 0L;

    private String password = null;

    private String nick = null;

    private int faith = 0;

    private long qq = 0L;

    private int black = 0;

    private long lastMsgId = 0L;

    private String lastMsg = null;

    private int repeatTimes = 0;

    private int rank = 0;

    private String status = QQConstants.STATUS_ONLINE;

    @Override
    public long getUin() {
        return this.uin;
    }

    public void setUin(final long uin) {
        this.uin = uin;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(final String nick) {
        this.nick = nick;
    }

    @Override
    public String toString() {
        return "@"
               + this.nick
               + "("
               + this.account
               + ")";
    }

    public long getAccount() {
        return this.account;
    }

    @Override
    public void setAccount(final long account) {
        this.account = account;
    }

    @Override
    public int hashCode() {
        return (int) this.uin;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final QQUser other = (QQUser) obj;

        return this.account == other.account;
    }

    public long getLastMsgId() {
        return this.lastMsgId;
    }

    public void setLastMsgId(final long lastMsgId) {
        this.lastMsgId = lastMsgId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getLastMsg() {
        return this.lastMsg;
    }

    public void setLastMsg(final String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public int getRepeatTimes() {
        return this.repeatTimes;
    }

    public void setRepeatTimes(final int repeatTimes) {
        this.repeatTimes = repeatTimes;
    }

    public int getFaith() {
        return this.faith;
    }

    public void setFaith(final int faith) {
        this.faith = faith;
    }

    public long getQq() {
        return this.qq;
    }

    public void setQq(final long qq) {
        this.qq = qq;
    }

    public int getBlack() {
        return this.black;
    }

    public void setBlack(final int black) {
        this.black = black;
    }

    public int getRank() {
        return this.rank;
    }

    public void setRank(final int rank) {
        this.rank = rank;
    }

    public String getHexUin() {
        return this.hexUin;
    }

    public void setHexUin(final String hexUin) {
        this.hexUin = hexUin;
    }

}
