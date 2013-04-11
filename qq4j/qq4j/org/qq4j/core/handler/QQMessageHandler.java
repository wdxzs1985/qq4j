package org.qq4j.core.handler;

import java.io.UnsupportedEncodingException;

import org.qq4j.core.QQContext;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

public interface QQMessageHandler {

    public void handle(QQContext context, final JSONObject json)
            throws UnsupportedEncodingException, JSONException;

    public String getHandleType();
}
