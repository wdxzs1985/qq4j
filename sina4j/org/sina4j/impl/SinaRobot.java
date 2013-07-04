package org.sina4j.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.sina4j.core.SinaHttpClient;
import org.sina4j.core.SinaLogin;
import org.sina4j.domain.SinaUserDTO;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

public class SinaRobot {

    private Log log = LogFactory.getLog(SinaRobot.class);

    private SinaHttpClient sinaClient = null;

    private SinaLogin sinaLogin = null;

    private SinaUserDTO sinaUser = null;

    public boolean doLogin() {
        return this.sinaLogin.doLogin(this.sinaUser) != null;
    }

    public boolean sendWeibo(final String text) {
        return this.doLogin() && this.sendAdd(text);
    }

    private boolean sendAdd(final String text) {
        final String url = "http://www.weibo.com/aj/mblog/add";

        final List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("_surl", ""));
        nvps.add(new BasicNameValuePair("_t", "0"));
        nvps.add(new BasicNameValuePair("location", "home"));
        nvps.add(new BasicNameValuePair("module", "stissue"));
        nvps.add(new BasicNameValuePair("pic_id", ""));
        nvps.add(new BasicNameValuePair("rank", ""));
        nvps.add(new BasicNameValuePair("text", text));

        final String result = this.sinaClient.postData(url, nvps);
        if (StringUtils.isNotBlank(result)) {
            int code = 0;
            try {
                final JSONObject resultJson = new JSONObject(result);
                code = resultJson.getInt("code");
            } catch (final JSONException e) {
                this.log.error(e.getMessage());
            }
            return code == 100000;
        }
        return false;
    }

    public SinaLogin getSinaLogin() {
        return this.sinaLogin;
    }

    public void setSinaLogin(final SinaLogin sinaLogin) {
        this.sinaLogin = sinaLogin;
    }

    public SinaUserDTO getSinaUser() {
        return this.sinaUser;
    }

    public void setSinaUser(final SinaUserDTO sinaUser) {
        this.sinaUser = sinaUser;
    }

    public SinaHttpClient getSinaClient() {
        return this.sinaClient;
    }

    public void setSinaClient(final SinaHttpClient sinaClient) {
        this.sinaClient = sinaClient;
    }

}
