package org.qq4j.core.handler;

import java.io.UnsupportedEncodingException;

import org.qq4j.core.QQContext;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQUser;

import atg.taglib.json.util.JSONException;

public interface QQCommandHandler {

    public void handle(QQContext context, QQUser user, final String message)
            throws UnsupportedEncodingException, JSONException;

    public void handleGroup(QQContext context,
                            QQGroup group,
                            QQUser user,
                            final String message)
            throws UnsupportedEncodingException, JSONException;

}
