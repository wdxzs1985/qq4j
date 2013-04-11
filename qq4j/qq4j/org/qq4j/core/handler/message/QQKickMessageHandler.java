package org.qq4j.core.handler.message;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQMessageHandler;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

public class QQKickMessageHandler implements QQMessageHandler {

    private final Log log = LogFactory.getLog(QQKickMessageHandler.class);

    @Override
    public void handle(final QQContext context, final JSONObject json)
            throws UnsupportedEncodingException, JSONException {
        final JSONObject value = json.getJSONObject("value");
        // reason
        final String reason = value.getString("reason");
        this.log.warn(reason);
        // 下线
        context.setRun(false);
    }

    @Override
    public String getHandleType() {
        return "kick_message";
    }

}
