package org.qq4j.common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class QQStringAnalyst {
    static final int BLANK = 0;
    static final int ALPHABET = 1;
    static final int NUMBER = 2;
    static final int HANZI = 5;
    static final int OTHER = 99;

    public static final int MAX_LENGTH = 20;

    static Pattern PATTERN_ALPHABET = Pattern.compile("[a-zA-Z]");
    static Pattern PATTERN_NUMBER = Pattern.compile("[0-9]");
    static Pattern PATTERN_HANZI = Pattern.compile("[\u4E00-\u9FA5]");
    //
    static Pattern PATTERN_FACE = Pattern.compile("(\\[\"face\",[\\d]+\\])");

    public static List<String> analystString(final String source,
                                             final int splitLength) {
        final QQStringAnalyst analyst = new QQStringAnalyst();
        analyst.setSplitLength(splitLength);
        analyst.analyst(source);
        return analyst.wordList;
    }

    private List<String> wordList = null;

    private int status = QQStringAnalyst.BLANK;

    private StringBuilder buffer = null;

    private int splitLength = 2;

    protected void analyst(final String source) {
        this.status = QQStringAnalyst.BLANK;
        this.wordList = new ArrayList<String>();
        this.buffer = new StringBuilder();

        final String noface = this.replaceFace(source);

        for (final char c : noface.toCharArray()) {
            final String cs = new String(new char[] { c });
            if (StringUtils.isBlank(cs)) {
                this.status = QQStringAnalyst.BLANK;
                continue;
                // } else if
                // (QQStringAnalyst.PATTERN_ALPHABET.matcher(cs).matches()) {
                // this.flushIfStatusChanged(QQStringAnalyst.OTHER);
                // this.flushIfTooLong(QQStringAnalyst.MAX_LENGTH);
                // this.appendIfNoRepeat(c);
                // continue;
                // } else if
                // (QQStringAnalyst.PATTERN_NUMBER.matcher(cs).matches()) {
                // this.flushIfStatusChanged(QQStringAnalyst.OTHER);
                // this.flushIfTooLong(QQStringAnalyst.MAX_LENGTH);
                // this.buffer.append(cs);
                // continue;
            } else if (QQStringAnalyst.PATTERN_HANZI.matcher(cs).matches()) {
                this.flushIfStatusChanged(QQStringAnalyst.HANZI);
                if (this.buffer.length() == this.splitLength) {
                    final String lastChar = this.buffer.toString();
                    this.flush(this.buffer);
                    this.buffer.append(lastChar).deleteCharAt(0);
                }
                this.appendIfNoRepeat(c);
                this.status = QQStringAnalyst.HANZI;
                continue;
            }
            this.flushIfStatusChanged(QQStringAnalyst.OTHER);
            this.flushIfTooLong(QQStringAnalyst.MAX_LENGTH);
            if (CollectionUtils.isEmpty(this.wordList)) {
                this.appendIfNoRepeat(c);
            }
        }
        this.flush(this.buffer);
    }

    private String replaceFace(final String source) {
        final Matcher m = QQStringAnalyst.PATTERN_FACE.matcher(source);
        while (m.find()) {
            final String group = m.group();
            this.wordList.add(group);
        }
        return m.replaceAll("");
    }

    void appendIfNoRepeat(final char c) {
        if (this.buffer.length() == 0 || this.buffer.charAt(this.buffer.length() - 1) != c) {
            this.buffer.append(c);
        }
    }

    void flushIfTooLong(final int length) {
        if (this.buffer.length() == length) {
            this.flush(this.buffer);
        }
    }

    void flushIfStatusChanged(final int newStatus) {
        if (this.status != newStatus) {
            this.flush(this.buffer);
            this.status = newStatus;
        }
    }

    void flush(final StringBuilder buffer) {
        if (buffer.length() > 0) {
            this.wordList.add(buffer.toString());
            buffer.setLength(0);
        }
    }

    public int getSplitLength() {
        return this.splitLength;
    }

    public void setSplitLength(final int splitLength) {
        this.splitLength = splitLength;
    }
}
