package org.sina4j.core;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.sina4j.domain.SinaUserDTO;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import framework.CommonHttpClient;
import framework.SystemConstants;

public class SinaLogin {
    public static final String LOGIN_URL = "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.3.19)";
    public static final int NONCE_LENGTH = 6;

    public static final Pattern LOCATION_PATTERN = Pattern.compile("location.replace\\('(.*)'\\)");
    public static final Pattern USERINFO_PATTERN = Pattern.compile("parent.sinaSSOController.feedBackUrlCallBack\\((.*)\\)");

    private Log log = LogFactory.getLog(SinaLogin.class);
    private SinaHttpClient sinaClient = null;

    public SinaUserDTO doLogin(final SinaUserDTO user) {
        final String ajaxLoginUrl = this.getAjaxLoginUrl(user);
        if (StringUtils.isBlank(ajaxLoginUrl)) {
            this.log.error("can not find login url!");
            return null;
        }
        //
        final String resultJsonString = this.getResultJsonString(ajaxLoginUrl);
        System.out.println(resultJsonString);
        if (StringUtils.isBlank(resultJsonString)) {
            this.log.error("can not find login result!");
            return null;
        }
        //
        try {
            final JSONObject result = new JSONObject(resultJsonString);
            if (result.getBoolean("result")) {
                final JSONObject userinfo = result.getJSONObject("userinfo");

                user.setUniqueid(userinfo.getString("uniqueid"));
                user.setUserid(userinfo.getString("userid"));
                user.setDisplayname(userinfo.getString("displayname"));
                user.setUserdomain(userinfo.getString("userdomain"));

                this.sinaClient.setReferer("http://www.weibo.com/u/"
                                           + user.getUniqueid());
                this.log.info(user.getDisplayname() + " 登录成功！");
            } else {
                this.log.error(result);
            }
        } catch (final JSONException e) {
            this.log.error(e.getMessage());
            return null;
        }
        return user;
    }

    public String encodeAccount(final String email) {
        String account = null;
        ;
        try {
            account = URLEncoder.encode(email, SystemConstants.ENCODING);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return Base64.encodeBase64String(account.getBytes());
    }

    public String encodePassword(final String password,
                                 final String servertime,
                                 final String nonce) {
        String encoded = DigestUtils.shaHex(password);
        encoded = DigestUtils.shaHex(encoded);
        encoded = DigestUtils.shaHex(encoded + servertime + nonce);
        return encoded;
    }

    public String getServerTime() {
        final long servertime = System.currentTimeMillis() / 1000;
        return String.valueOf(servertime);
    }

    public String makeNonce(final int len) {
        final String x = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String str = "";
        for (int i = 0; i < len; i++) {
            str += x.charAt((int) (Math.ceil(Math.random() * 1000000) % x.length()));
        }
        return str;
    }

    private String getAjaxLoginUrl(final SinaUserDTO user) {
        final String servertime = this.getServerTime();
        final String nonce = this.makeNonce(SinaLogin.NONCE_LENGTH);

        final List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("entry", "weibo"));
        nvps.add(new BasicNameValuePair("gateway", "1"));
        nvps.add(new BasicNameValuePair("from", ""));
        nvps.add(new BasicNameValuePair("savestate", "7"));
        nvps.add(new BasicNameValuePair("useticket", "1"));
        nvps.add(new BasicNameValuePair("ssosimplelogin", "1"));
        nvps.add(new BasicNameValuePair("su",
                                        this.encodeAccount(user.getEmail())));
        nvps.add(new BasicNameValuePair("service", "miniblog"));
        nvps.add(new BasicNameValuePair("servertime", servertime));
        nvps.add(new BasicNameValuePair("nonce", nonce));
        nvps.add(new BasicNameValuePair("pwencode", "wsse"));
        nvps.add(new BasicNameValuePair("sp",
                                        this.encodePassword(user.getPassword(),
                                                            servertime,
                                                            nonce)));
        nvps.add(new BasicNameValuePair("url",
                                        "http://www.weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack"));
        nvps.add(new BasicNameValuePair("returntype", "META"));
        nvps.add(new BasicNameValuePair("encoding", "UTF-8"));
        nvps.add(new BasicNameValuePair("vsnval", ""));

        final CommonHttpClient client = new CommonHttpClient();
        final String entity = client.postData(SinaLogin.LOGIN_URL, nvps);

        final Matcher m = SinaLogin.LOCATION_PATTERN.matcher(entity);
        String url = null;
        if (m.find()) {
            url = m.group(1);
        }
        return url;
    }

    private String getResultJsonString(final String ajaxLoginUrl) {
        final String entity = this.sinaClient.getData(ajaxLoginUrl);
        final Matcher m = SinaLogin.USERINFO_PATTERN.matcher(entity);
        String result = null;
        if (m.find()) {
            result = m.group(1);
        }
        return result;
    }

    public SinaHttpClient getSinaClient() {
        return this.sinaClient;
    }

    public void setSinaClient(final SinaHttpClient sinaClient) {
        this.sinaClient = sinaClient;
    }
}
