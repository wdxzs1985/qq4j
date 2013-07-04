package org.sina4j.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpMessage;

import framework.CommonHttpClient;

public class SinaHttpClient extends CommonHttpClient {

    private String referer = null;

    public String getReferer() {
        return this.referer;
    }

    public void setReferer(final String referer) {
        this.referer = referer;
    }

    @Override
    protected void initHttpHeader(final HttpMessage httpMessage) {
        super.initHttpHeader(httpMessage);
        if (StringUtils.isNotBlank(this.referer)) {
            httpMessage.addHeader("Referer", this.referer);
        }
    }

}
