package org.qq4j.core.handler;

import net.sf.json.JSONObject;

import org.qq4j.core.QQContext;

public interface QQMessageHandler {

    public void handle(QQContext context, final JSONObject json);

    public String getHandleType();
}
