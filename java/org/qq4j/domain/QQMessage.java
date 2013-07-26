package org.qq4j.domain;

public class QQMessage {

    private String messageId = null;

    private String message = null;

    private String answer = null;

    private long qq = 0L;

    private long owner = 0L;

    private int privatable = 0;

    private int resultCount = 0;

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

    public int getPrivatable() {
        return this.privatable;
    }

    public void setPrivatable(final int privatable) {
        this.privatable = privatable;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(final String messageId) {
        this.messageId = messageId;
    }

    public int getResultCount() {
        return this.resultCount;
    }

    public void setResultCount(final int resultCount) {
        this.resultCount = resultCount;
    }

}
