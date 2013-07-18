package org.qq4j.domain;


public class QQMessage {

    private long id = 0L;

    private String message = null;

    private String answer = null;

    private long qq = 0L;

    private long owner = 0L;

    private int publicFlg = 0;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(final String answer) {
        this.answer = answer;
    }

    public Long getQq() {
        return this.qq;
    }

    public void setQq(final Long qq) {
        this.qq = qq;
    }

    public Long getOwner() {
        return this.owner;
    }

    public void setOwner(final Long owner) {
        this.owner = owner;
    }

    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public int getPublicFlg() {
        return this.publicFlg;
    }

    public void setPublicFlg(final int publicFlg) {
        this.publicFlg = publicFlg;
    }
}
