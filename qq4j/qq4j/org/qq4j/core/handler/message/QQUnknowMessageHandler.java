package org.qq4j.core.handler.message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQMessageHandler;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

public class QQUnknowMessageHandler implements QQMessageHandler {

    private final Log log = LogFactory.getLog(QQUnknowMessageHandler.class);

    @Override
    public void handle(final QQContext context, final JSONObject json)
            throws JSONException {
        this.log.info("未知消息：" + json.toString());
    }

    @Override
    public String getHandleType() {
        return null;
    }

}
