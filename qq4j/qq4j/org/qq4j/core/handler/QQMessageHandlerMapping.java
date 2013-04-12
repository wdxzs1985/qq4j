package org.qq4j.core.handler;

import java.util.Map;

import net.sf.json.JSONObject;

import org.qq4j.core.QQContext;

public class QQMessageHandlerMapping implements QQMessageHandler {

    private Map<String, QQMessageHandler> handlers = null;
    private QQMessageHandler defaultHandler = null;

    @Override
    public void handle(final QQContext context, final JSONObject json) {
        final String type = json.getString("poll_type");
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
