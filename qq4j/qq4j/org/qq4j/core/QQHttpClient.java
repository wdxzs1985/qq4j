package org.qq4j.core;

import java.util.Collections;
import java.util.List;

import org.apache.http.HttpMessage;
import org.apache.http.message.BasicNameValuePair;

import atg.taglib.json.util.JSONObject;
import framework.CommonHttpClient;

public class QQHttpClient extends CommonHttpClient {

    private static final String REFERER = "http://d.web2.qq.com/proxy.html?v=20110331002&callback=2";

    public String postJsonData(final String url, final JSONObject content) {
        final String contents = content.toString();
        final List<BasicNameValuePair> nvps = Collections.singletonList(new BasicNameValuePair("r",
                                                                                               contents));
        return this.postData(url, nvps);
    }

    @Override
    protected void initHttpHeader(final HttpMessage httpMessage) {
        super.initHttpHeader(httpMessage);
        httpMessage.addHeader("Referer", QQHttpClient.REFERER);
    }
}
