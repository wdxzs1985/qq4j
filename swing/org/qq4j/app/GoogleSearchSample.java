package org.qq4j.app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.CommonHttpClient;

public class GoogleSearchSample {

    private static final Log log = LogFactory.getLog(GoogleSearchSample.class);

    public static void main(final String[] args)
                                                throws UnsupportedEncodingException {
        final CommonHttpClient httpClient = new CommonHttpClient();
        final StringBuilder url = new StringBuilder("http://ajax.googleapis.com/ajax/services/search/web");
        url.append("?").append("v=1.0");
        url.append("&")
           .append("q=")
           .append(URLEncoder.encode("Paris Hilton", "utf-8"));
        final String response = httpClient.getJSON(url.toString());
        final JSONObject responseJsonObject = JSONObject.fromObject(response);
        if (responseJsonObject.getInt("responseStatus") == 200) {
            // success
            final JSONObject responseData = responseJsonObject.getJSONObject("responseData");
            final JSONArray results = responseData.getJSONArray("results");
            for (int i = 0; i < results.size(); i++) {
                final JSONObject resultItem = results.getJSONObject(i);
                log.debug(StringUtils.rightPad("GsearchResultClass", 20)
                          + ":"
                          + resultItem.getString("GsearchResultClass"));
                log.debug(StringUtils.rightPad("unescapedUrl", 20)
                          + ":"
                          + resultItem.getString("unescapedUrl"));
                log.debug(StringUtils.rightPad("url", 20)
                          + ":"
                          + resultItem.getString("url"));
                log.debug(StringUtils.rightPad("visibleUrl", 20)
                          + ":"
                          + resultItem.getString("visibleUrl"));
                log.debug(StringUtils.rightPad("cacheUrl", 20)
                          + ":"
                          + resultItem.getString("cacheUrl"));
                log.debug(StringUtils.rightPad("title", 20)
                          + ":"
                          + resultItem.getString("title"));
                log.debug(StringUtils.rightPad("titleNoFormatting", 20)
                          + ":"
                          + resultItem.getString("titleNoFormatting"));
                log.debug(StringUtils.rightPad("content", 20)
                          + ":"
                          + resultItem.getString("content"));
                log.debug(StringUtils.repeat("=", 20));
            }

        } else {
            log.error(responseJsonObject.getString("responseDetails"));
        }
    }

}
