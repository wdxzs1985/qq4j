package org.qq4j.core.handler.message;

import java.util.Map;

import net.sf.json.JSONObject;

import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQMessageHandler;

public class QQSystemMessageHandlerMapping implements QQMessageHandler {

    private Map<String, QQMessageHandler> handlers = null;
    private QQMessageHandler defaultHandler = null;;

    public void registerHandler(final QQMessageHandler handler) {
        this.getHandlers().put(handler.getHandleType(), handler);
    }

    @Override
    public void handle(final QQContext context, final JSONObject json) {
        final JSONObject value = json.getJSONObject("value");
        final String type = value.getString("type");
        if (this.handlers.containsKey(type)) {
            this.handlers.get(type).handle(context, json);
        } else if (this.defaultHandler != null) {
            this.defaultHandler.handle(context, json);
        }
    }

    @Override
    public String getHandleType() {
        return null;
    }

    public Map<String, QQMessageHandler> getHandlers() {
        return this.handlers;
    }

    public void setHandlers(final Map<String, QQMessageHandler> handlers) {
        this.handlers = handlers;
    }

    public QQMessageHandler getDefaultHandler() {
        return this.defaultHandler;
    }

    public void setDefaultHandler(final QQMessageHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
    }
}
