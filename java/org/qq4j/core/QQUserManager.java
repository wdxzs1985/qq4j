package org.qq4j.core;

import java.io.UnsupportedEncodingException;
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

    public static final int APPID = 1003903;

    protected Log log = LogFactory.getLog(QQUserManager.class);

    private QQContext context;

    private QQUser self = null;

    private String hexUin = null;

    private String verifyCode = null;

    public String getVerifyCode() {
        this.hexUin = null;
        this.verifyCode = null;
        final String checkQQUrl = "http://check.ptlogin2.qq.com/check?appid="
                                  + QQUserManager.APPID
                                  + "&uin="
                                  + this.self.getAccount();
        final String result = this.context.getHttpClient().getJSON(checkQQUrl);
        Object[] group = null;
        if (StringUtils.isNotBlank(result)) {
            group = this.findString("'(.*?)'", result);
            if (group[0].equals("0")) {
                this.verifyCode = (String) group[1];
            }
            this.hexUin = (String) group[2];
        }
        return this.verifyCode;
    }

    public byte[] downloadVerifyImage() {
        final String url = "http://captcha.qq.com/getimage?aid="
                           + QQUserManager.APPID
                           + "&uin="
                           + this.self.getAccount();
        return this.context.getHttpClient().getByte(url);
    }

    public QQUser login(final String password, final String verifyCode)
                                                                       throws UnsupportedEncodingException {
        final String loginUrl = "http://ptlogin2.qq.com/login?u="
                                + this.self.getAccount()
                                + "&p="
                                + this.encodePass(password,
                                                  verifyCode,
                                                  this.hexUin)
                                + "&verifycode="

                                + verifyCode
                                + "&webqq_type=10&remember_uin=1&login2qq=1&aid="
                                + QQUserManager.APPID
                                + "&u1=http%3A%2F%2Fweb.qq.com%2Floginproxy.html%3Flogin2qq%3D1%26webqq_type%3D10"
                                + "&h=1&ptredirect=0&ptlang=2052&from_ui=1&pttype=1&dumy=&fp=loginerroralert&action=5-13-9792&mibao_css=m_webqq&t=1&g=1";
        final String result = this.context.getHttpClient().getJSON(loginUrl);
        final Object[] ptuiCB = this.findString("'(.*?)'", result);
        final String errorCode = (String) ptuiCB[0];
        final String errorMessage = (String) ptuiCB[4];
        if (StringUtils.equals("0", errorCode)) {
            final String nick = (String) ptuiCB[5];
            this.self.setNick(nick);
            this.log.info(String.format("%s%s", this.self, errorMessage));
        } else {
            this.log.error(errorMessage);
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

    public void offline() {
        final String statusUrl = "http://d.web2.qq.com/channel/change_status2?newstatus=offline&clientid="
                                 + this.context.getClientid()
                                 + "&psessionid="
                                 + this.context.getPsessionid()
                                 + "&t="
                                 + System.currentTimeMillis();
        this.context.getHttpClient().getJSON(statusUrl);
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

    private String getChannelInfo() {
        final String url = "http://d.web2.qq.com/channel/login2";

        final JSONObject content = new JSONObject();
        content.put("status", this.getSelf().getStatus());
        content.put("ptwebqq", this.context.getPtwebqq());
        content.put("passwd_sig", "");
        content.put("clientid", this.context.getClientid());
        return this.context.getHttpClient().postJsonData(url, content);
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

    public String getLongNick(final QQUser user) {
        String lnick = null;
        final QQContext context = this.getContext();
        final String url = "http://s.web2.qq.com/api/set_long_nick2";
        final JSONObject content = new JSONObject();
        content.put("tuin", user.getUin());
        content.put("vfwebqq", context.getVfwebqq());
        final String result = context.getHttpClient().getJSON(url);
        final JSONObject retJson = JSONObject.fromObject(result);
        final int retcode = retJson.getInt("retcode");
        if (retcode == 0) {
            final JSONObject resultJson = retJson.getJSONObject("result");
            lnick = resultJson.getString("lnick");
        }
        return lnick;
    }

    public void setLongNick(final String nlk) {
        final QQContext context = this.getContext();
        final String url = "http://s.web2.qq.com/api/set_long_nick2";
        final JSONObject content = new JSONObject();
        content.put("nlk", nlk);
        content.put("vfwebqq", context.getVfwebqq());
        context.getHttpClient().postJsonData(url, content);
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
}
