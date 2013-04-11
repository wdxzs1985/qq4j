package org.qq4j.core.handler;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.qq4j.core.QQContext;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

public class QQSystemMessageHandlerMapping implements QQMessageHandler {

    private Map<String, QQMessageHandler> handlers = null;
    private QQMessageHandler defaultHandler = null;

    @Override
    public void handle(final QQContext context, final JSONObject json)
            throws JSONException, UnsupportedEncodingException {
        final String type = json.getString("type");
        if (this.getHandlers().containsKey(type)) {
            this.getHandlers().get(type).handle(context, json);
        } else if (this.getDefaultHandler() != null) {
            this.getDefaultHandler().handle(context, json);
        }
    }

    @Override
    public String getHandleType() {
        return null;
    }

    public QQMessageHandler getDefaultHandler() {
        return this.defaultHandler;
    }

    public void setDefaultHandler(final QQMessageHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    public Map<String, QQMessageHandler> getHandlers() {
        return this.handlers;
    }

    public void setHandlers(final Map<String, QQMessageHandler> handlers) {
        this.handlers = handlers;
    }
}
