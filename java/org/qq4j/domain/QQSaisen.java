package org.qq4j.domain;

public class QQSaisen {

    private String date = null;

    private int times = 0;

    public String getDate() {
        return this.date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public int getTimes() {
        return this.times;
    }

    public void increaseTimes() {
        this.times++;
    }

}
