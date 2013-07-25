package org.qq4j.domain;

import org.qq4j.core.QQConstants;

public class QQFont {

    private String name = QQConstants.DEFAULT_FONT_NAME;

    private String color = QQConstants.DEFAULT_FONT_COLOR;

    private int size = QQConstants.DEFAULT_FONT_SIZE;

    private boolean bold = false;

    private boolean underline = false;

    private boolean italic = false;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public boolean isBold() {
        return this.bold;
    }

    public void setBold(final boolean bold) {
        this.bold = bold;
    }

    public boolean isUnderline() {
        return this.underline;
    }

    public void setUnderline(final boolean underline) {
        this.underline = underline;
    }

    public boolean isItalic() {
        return this.italic;
    }

    public void setItalic(final boolean italic) {
        this.italic = italic;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

}
