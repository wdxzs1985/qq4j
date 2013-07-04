package org.qq4j.net;

import java.util.TimeZone;

public interface SystemConstants {

    public String ENCODING = "UTF-8";

    public TimeZone DEFAULT_LOCALE = TimeZone.getTimeZone("GMT+8:00");

    public String DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    public long REPLY_TIME_LIMIT = 1000 * 30;
}
