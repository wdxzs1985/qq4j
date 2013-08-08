package org.qq4j.core;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.domain.QQUser;

/**
 * 
 * 
 */
public class QQUserManager {

    public static final int DAID = 164;
    public static final int APPID = 1003903;
    public static final String S_URL = "http://web2.qq.com/loginproxy.html";

    protected Log log = LogFactory.getLog(QQUserManager.class);

    private QQContext context;

    private QQUser self = null;

    private String hexUin = null;

    private String verifyCode = null;

    private String loginSig = null;

    public void initLogin() throws UnsupportedEncodingException {
        this.initLoginParams();
        this.checkVerifyCode();
    }

    private void initLoginParams() throws UnsupportedEncodingException {
        final String url = "https://ui.ptlogin2.qq.com/cgi-bin/login?daid="
                           + DAID
                           + "&target=self&style=5&mibao_css=m_webqq&appid="
                           + APPID
                           + "&enable_qlogin=0&"
                           + "no_verifyimg=1"
                           + "&s_url="
                           + URLEncoder.encode(S_URL, "utf-8")
                           + "&f_url=loginerroralert"
                           + "&strong_login=1"
                           + "&login_state=10";
        final String result = this.context.getHttpClient().get(url);
        Object[] group = null;
        if (StringUtils.isNotBlank(result)) {
            group = this.findString("var g_login_sig=encodeURIComponent\\(\"(.{64})\"\\);",
                                    result);
            this.loginSig = (String) group[0];
        }
    }

    private void checkVerifyCode() throws UnsupportedEncodingException {
        this.hexUin = null;
        this.verifyCode = null;
        final String url = "https://ssl.ptlogin2.qq.com/check?uin="
                           + this.self.getAccount()
                           + "&appid="
                           + APPID
                           + "&login_sig="
                           + this.loginSig
                           + "&u1="
                           + URLEncoder.encode(S_URL, "utf-8");
        final String result = this.context.getHttpClient().get(url);
        Object[] group = null;
        if (StringUtils.isNotBlank(result)) {
            group = this.findString("'(.*?)'", result);
            if (group[0].equals("0")) {
                this.verifyCode = (String) group[1];
            }
            this.hexUin = (String) group[2];
        }
    }

    public byte[] downloadVerifyImage() {
        final String url = "https://ssl.captcha.qq.com/getimage?uin="
                           + this.self.getAccount()
                           + "&appid="
                           + APPID;
        return this.context.getHttpClient().getByte(url);
    }

    public QQUser login(final String password, final String verifyCode)
                                                                       throws UnsupportedEncodingException {
        final String url = "https://ssl.ptlogin2.qq.com/login?u="
                           + this.self.getAccount()
                           + "&p="
                           + this.encodePass(password, verifyCode, this.hexUin)
                           + "&verifycode="
                           + verifyCode
                           + "&webqq_type=10"
                           + "&remember_uin=1"
                           + "&login2qq=1"
                           + "&aid="
                           + APPID
                           + "&u1="
                           + URLEncoder.encode(S_URL
                                                       + "?webqq_type=10&login2qq=1",
                                               "utf-8")
                           + "&h=1"
                           + "&ptredirect=0"
                           + "&ptlang=2052"
                           + "&daid="
                           + DAID
                           + "&from_ui=1"
                           + "&pttype=1"
                           + "&dumy="
                           + "&fp=loginerroralert"
                           + "&action=10-28-642199"
                           + "&mibao_css=m_webqq"
                           + "&t=1"
                           + "&g=1"
                           + "&js_type=0"
                           + "&js_ver=10038"
                           + "&login_sig="
                           + this.loginSig;
        final String result = this.context.getHttpClient().get(url);
        final Object[] ptuiCB = this.findString("'(.*?)'", result);
        final String resultCode = (String) ptuiCB[0];
        final String message = (String) ptuiCB[4];
        if (StringUtils.equals("0", resultCode)) {
            final String nick = (String) ptuiCB[5];
            this.self.setNick(nick);
            this.log.info(String.format("%s%s", this.self, message));

            final String checkSigUrl = (String) ptuiCB[2];
            this.context.getHttpClient().get(checkSigUrl);
            this.online();
        } else {
            this.log.error(message);
        }
        return this.self;
    }

    public void online() {
        this.getDataFromCookie();
        this.fetchChannelInfo();
    }

    private void getDataFromCookie() {
        this.context.setPtwebqq(this.context.getHttpClient()
                                            .findCookie("ptwebqq"));
        this.context.setSkey(this.context.getHttpClient().findCookie("skey"));
        this.log.info(String.format("获得ptwebqq：%s", this.context.getPtwebqq()));
        this.log.info(String.format("获得skey：%s", this.context.getSkey()));
    }

    private void fetchChannelInfo() {
        final String result = this.getChannelInfo();
        final JSONObject retJson = JSONObject.fromObject(result);
        final int retcode = retJson.getInt("retcode");
        if (retcode == 0) {
            final JSONObject resultJson = retJson.getJSONObject("result");
            // long uin = resultJson.getLong("uin");
            this.context.setVfwebqq(resultJson.getString("vfwebqq"));
            this.context.setPsessionid(resultJson.getString("psessionid"));
            this.log.info(String.format("获得vfwebqq：%s",
                                        this.context.getVfwebqq()));
            this.log.info(String.format("获得psessionid：%s",
                                        this.context.getPsessionid()));
        }
    }

    private String getChannelInfo() {
        final String url = "https://d.web2.qq.com/channel/login2";
        final JSONObject content = new JSONObject();
        content.put("status", this.getSelf().getStatus());
        content.put("ptwebqq", this.context.getPtwebqq());
        content.put("passwd_sig", "");
        content.put("clientid", this.context.getClientid());
        return this.context.getHttpClient().postJsonData(url, content);
    }

    public void offline() {
        this.changeStatus(QQConstants.STATUS_OFFLINE);
        this.context.setRun(false);
    }

    public Object[] findString(final String pattern, final String search) {
        final Pattern p = Pattern.compile(pattern);
        final Matcher m = p.matcher(search);
        Object[] targets = {};
        while (m.find()) {
            targets = ArrayUtils.add(targets, m.group(1));
        }
        return targets;
    }

    // 加密密码
    public String encodePass(final String pass,
                             final String code,
                             final String uin)
                                              throws UnsupportedEncodingException {
        String encode = this.md5Hex(pass);
        encode = this.hexCharToBin(encode);
        encode = encode
                 + this.evalString(uin);
        encode = this.md5Hex(encode);
        encode = encode
                 + code.toUpperCase();
        encode = this.md5Hex(encode);
        return encode;
    }

    private String md5Hex(final String str) throws UnsupportedEncodingException {
        return DigestUtils.md5Hex(str.getBytes("ISO-8859-1")).toUpperCase();
    }

    private String hexCharToBin(final String hex) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < hex.length() / 2; i++) {
            builder.append("\\x");
            builder.append(StringUtils.substring(hex, i * 2, i * 2 + 2));
        }
        return this.evalString(builder.toString());
    }

    private String evalString(final String hex) {
        final String[] codeStrs = hex.split("\\\\x");
        final StringBuilder builder = new StringBuilder();
        for (final String code : codeStrs) {
            if (StringUtils.isNotBlank(code)) {
                builder.append((char) Integer.parseInt(code, 16));
            }
        }
        return builder.toString();
    }

    public void allowAddFriend(final long account) {
        //
        final QQContext context = this.getContext();
        final String url = "http://s.web2.qq.com/api/allow_and_add2";
        final JSONObject content = new JSONObject();
        content.put("account", account);
        content.put("gid", 0);
        content.put("mname", "");
        content.put("vfwebqq", context.getVfwebqq());
        context.getHttpClient().postJsonData(url, content);
    }

    public String getSelfInfo() {
        final String lnick = null;
        final QQContext context = this.getContext();
        final String url = "http://s.web2.qq.com/api/get_self_info2";
        final String result = context.getHttpClient().get(url);
        final JSONObject retJson = JSONObject.fromObject(result);
        final int retcode = retJson.getInt("retcode");
        if (retcode == 0) {
            final JSONObject resultJson = retJson.getJSONObject("result");
            this.self.setAccount(resultJson.getLong("account"));
            this.self.setNick(resultJson.getString("nick"));
            this.self.setLnick(resultJson.getString("lnick"));
            this.self.setFace(resultJson.getInt("face"));
            this.self.setGender(resultJson.getString("gender"));
        }
        return lnick;
    }

    public void setLongNick(final String nlk) {
        final QQContext context = this.getContext();
        final String url = "http://s.web2.qq.com/api/set_long_nick2";
        final JSONObject content = new JSONObject();
        content.put("nlk", this.self.getLnick());
        content.put("vfwebqq", context.getVfwebqq());
        final String result = context.getHttpClient()
                                     .postJsonData(url, content);
        final JSONObject retJson = JSONObject.fromObject(result);
        final int retcode = retJson.getInt("retcode");
        if (retcode == 0) {
            this.self.setLnick(nlk);
        }
    }

    public void changeStatus(final String newStatus) {
        final String url = "http://d.web2.qq.com/channel/change_status2?newstatus="
                           + newStatus
                           + "&clientid="
                           + this.context.getClientid()
                           + "&psessionid="
                           + this.context.getPsessionid();
        final String result = this.context.getHttpClient().get(url);
        final JSONObject retJson = JSONObject.fromObject(result);
        final int retcode = retJson.getInt("retcode");
        if (retcode == 0) {
            this.self.setStatus(newStatus);
        }
    }

    public QQContext getContext() {
        return this.context;
    }

    public void setContext(final QQContext context) {
        this.context = context;
    }

    public QQUser getSelf() {
        return this.self;
    }

    public void setSelf(final QQUser self) {
        this.self = self;
    }

    public String getVerifyCode() {
        return this.verifyCode;
    }

    public String getLoginSig() {
        return this.loginSig;
    }
}
