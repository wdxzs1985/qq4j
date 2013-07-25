package org.qq4j.domain;

public class QQGroupMember {

    private String nick = null;

    private QQUser user = null;

    public String getNick() {
        return this.nick;
    }

    public void setNick(final String nick) {
        this.nick = nick;
    }

    public QQUser getUser() {
        return this.user;
    }

    public void setUser(final QQUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "@"
               + this.nick
               + "("
               + this.getUser().getAccount()
               + ")";
    }
}
