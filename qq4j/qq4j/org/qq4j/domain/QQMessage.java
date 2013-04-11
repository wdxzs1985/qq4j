package org.qq4j.domain;

import java.util.Date;

public class QQMessage {

    private String message = null;

    private String answer = null;

    private Long qq = null;

    private Date addDate = null;

    private Long owner = null;

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

    public Date getAddDate() {
        return this.addDate;
    }

    public void setAddDate(final Date addDate) {
        this.addDate = addDate;
    }

    public Long getOwner() {
        return this.owner;
    }

    public void setOwner(final Long owner) {
        this.owner = owner;
    }
}
