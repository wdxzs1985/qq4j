package org.qq4j.domain;

public class QQIndex {

    private long id = 0L;

    private long messageId = 0L;

    private String word = null;

    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public long getMessageId() {
        return this.messageId;
    }

    public void setMessageId(final long messageId) {
        this.messageId = messageId;
    }

    public String getWord() {
        return this.word;
    }

    public void setWord(final String word) {
        this.word = word;
    }

}
