package org.qq4j.core.handler.command;

import java.util.Calendar;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.qq4j.common.SystemConstants;
import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQCommandHandler;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQGroupMember;
import org.qq4j.domain.QQUser;

public class QQTimeCommandHandler implements QQCommandHandler {

    private String format;
    private String hello1;
    private String hello2;
    private String hello3;
    private String hello4;
    private String hello5;

    @Override
    public void handle(final QQContext context,
                       final QQUser user,
                       final String message) {
        context.getSender().sendToUser(user, this.getAnswer());
    }

    @Override
    public void handleGroup(final QQContext context,
                            final QQGroup group,
                            final QQGroupMember member,
                            final String message) {
        context.getSender().sendToGroup(group, this.getAnswer());
    }

    private String getAnswer() {
        final Calendar now = Calendar.getInstance(SystemConstants.DEFAULT_LOCALE);
        final int hours = (int) DateUtils.getFragmentInHours(now, Calendar.DATE);
        String hello = null;
        switch (hours) {
        case 5:
        case 6:
        case 7:
        case 8:
            hello = this.hello1;
            break;
        case 9:
        case 10:
        case 11:
            hello = this.hello2;
            break;
        case 12:
            hello = this.hello3;
            break;
        case 13:
        case 14:
        case 15:
        case 16:
        case 17:
            hello = this.hello4;
            break;
        case 18:
        case 19:
        case 20:
        case 21:
        case 22:
        case 23:
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
            hello = this.hello5;
            break;
        }

        return hello
               + DateFormatUtils.format(System.currentTimeMillis(),
                                        this.format,
                                        SystemConstants.DEFAULT_LOCALE);
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(final String format) {
        this.format = format;
    }

    public String getHello1() {
        return this.hello1;
    }

    public void setHello1(final String hello1) {
        this.hello1 = hello1;
    }

    public String getHello2() {
        return this.hello2;
    }

    public void setHello2(final String hello2) {
        this.hello2 = hello2;
    }

    public String getHello3() {
        return this.hello3;
    }

    public void setHello3(final String hello3) {
        this.hello3 = hello3;
    }

    public String getHello4() {
        return this.hello4;
    }

    public void setHello4(final String hello4) {
        this.hello4 = hello4;
    }

    public String getHello5() {
        return this.hello5;
    }

    public void setHello5(final String hello5) {
        this.hello5 = hello5;
    }
}
