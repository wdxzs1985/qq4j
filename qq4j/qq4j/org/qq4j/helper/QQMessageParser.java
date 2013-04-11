package org.qq4j.helper;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;

public class QQMessageParser {

    protected static Log log = LogFactory.getLog(QQMessageParser.class);

    private static final Pattern fontPattern = Pattern.compile("\\[\\\"font\\\",.+\\]");
    private static final Pattern facePattern = Pattern.compile("\\[\\\"face\\\",(\\d+{1,3})\\]");
    private static final Pattern cfacePattern = Pattern.compile("\\[\\\"cface\\\",.*\\]");
    private static final Pattern offpicPattern = Pattern.compile("\\[\\\"offpic\\\",.*\\]");

    // ["offpic",{"success":1,"file_path":"/2fb8625d-79b8-44b7-8d65-7e1870b0cfbf"}]

    public static String parseMessage(final JSONArray content)
            throws JSONException, UnsupportedEncodingException {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < content.length(); i++) {
            final String fragment = content.getString(i);
            if (StringUtils.isBlank(fragment)) {
                // do nothing
            } else if (QQMessageParser.fontPattern.matcher(fragment).find()) {
                // do nothing
            } else if (QQMessageParser.facePattern.matcher(fragment).find()) {
                builder.append(fragment);
            } else if (QQMessageParser.cfacePattern.matcher(fragment).find()) {
                // builder.append("[图片]");
            } else if (QQMessageParser.offpicPattern.matcher(fragment).find()) {
                // builder.append(fragment);
            } else {
                String message = QQMessageParser.removeMobileInfo(fragment);
                message = QQMessageParser.removeQplusInfo(message);
                builder.append(message);
            }
        }
        String message = builder.toString();
        message = StringUtils.trim(message);
        return message;
    }

    public static String replaceFace(final String message) {
        return message.replaceAll("(\\[\\\\\"face\\\\\",(\\d+)\\])",
                                  "\",[\"face\",$2],\" ");
    }

    public static String replaceCFace(final String message) {
        return message.replaceAll("(\\[图片\\])",
                                  "\",[\"cface\",\"37C10EB76500BDF330CB1A0F62C406F0.JPG\"],\" ");
    }

    public static String replaceGroupCFace(final String message) {
        return message.replaceAll("(\\[图片\\])",
                                  "\",[\"cface\",\"group\",\"37C10EB76500BDF330CB1A0F62C406F0.JPG\"],\" ");
    }

    private static String removeMobileInfo(final String message) {
        return message.replaceAll("\\(.*[Qq]{2}.+\\)", "");
    }

    private static String removeQplusInfo(final String message) {
        return message.replaceAll("【提示：.+】", "");
    }
}
