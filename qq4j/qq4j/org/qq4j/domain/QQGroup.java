package org.qq4j.domain;

import java.io.Serializable;
import java.util.Map;

import org.qq4j.core.QQAccount;

public class QQGroup implements Serializable, QQAccount {

    /**
     * 
     */
    private static final long serialVersionUID = -7472175651304183761L;

    /**
     * gid
     */
    private long uin;
    /**
     * gcode
     */
    private long code;

    /**
     * 群名
     */
    private String name;
    /**
     * 群号
     */
    private long account;

    private Map<Long, QQUser> members = null;

    public long getCode() {
        return this.code;
    }

    public void setCode(final long code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "#" + this.name + "(" + this.account + ")";
    }

    @Override
    public long getUin() {
        return this.uin;
    }

    public void setUin(final long uin) {
        this.uin = uin;
    }

    public long getAccount() {
        return this.account;
    }

    @Override
    public void setAccount(final long account) {
        this.account = account;
    }

    public Map<Long, QQUser> getMembers() {
        return this.members;
    }

    public void setMembers(final Map<Long, QQUser> members) {
        this.members = members;
    }

}
