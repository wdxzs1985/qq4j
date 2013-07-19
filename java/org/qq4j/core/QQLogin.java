package org.qq4j.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.exception.NeedVerifyCodeException;
import org.qq4j.domain.QQUser;

public class QQLogin {

    public static final int APPID = 1003903;

    protected Log log = LogFactory.getLog(QQLogin.class);

    private QQHttpClient httpClient = null;

    private String uin = null;

    private boolean needVerify = false;

    private void reset() {
        this.uin = null;
        this.needVerify = false;
    }

    public QQUser login(final long account, final String pasword)
                                                                 throws NeedVerifyCodeException {
        final String verifyCode = this.getVerifyCode(account);
        if (verifyCode != null) {
            this.log.info(String.format("获得验证码：%s", verifyCode));
            this.log.info(String.format("获得UIN：%s", this.uin));
            return this.login(account, pasword, verifyCode);
        }
        return null;
    }

    private String getVerifyCode(final long account)
                                                    throws NeedVerifyCodeException {
        final String checkQQUrl = "http://check.ptlogin2.qq.com/check?appid="
                                  + QQLogin.APPID
                                  + "&uin="
                                  + account;
        final String result = this.httpClient.getJSON(checkQQUrl);
        Object[] group = null;
        if (StringUtils.isNotBlank(result)) {
            group = this.findString("'(.*?)'", result);
            this.uin = (String) group[2];
            if (group[0].equals("0")) {
                return (String) group[1];
            } else {
                this.needVerify = true;
                // TODO 生成图片验证码
                throw new NeedVerifyCodeException();
            }
        }
        return null;
    }

    public byte[] downloadVerifyImage(final long account) {
        final String url = "http://captcha.qq.com/getimage?aid="
                           + QQLogin.APPID
                           + "&uin="
                           + account;
        return this.httpClient.getByte(url);
    }

    public QQUser login(final long account,
                        final String password,
                        final String verifyCode) {
        final String loginUrl = "http://ptlogin2.qq.com/login?u="
                                + account
                                + "&p="
                                + this.encodePass(password,
                                                  verifyCode,
                                                  this.uin)
                                + "&verifycode="

                                + verifyCode
                                + "&webqq_type=10&remember_uin=1&login2qq=1&aid="
                                + QQLogin.APPID
                                + "&u1=http%3A%2F%2Fweb.qq.com%2Floginproxy.html%3Flogin2qq%3D1%26webqq_type%3D10"
                                + "&h=1&ptredirect=0&ptlang=2052&from_ui=1&pttype=1&dumy=&fp=loginerroralert&action=5-13-9792&mibao_css=m_webqq&t=1&g=1";
        final String result = this.httpClient.getJSON(loginUrl);
        final Object[] ptuiCB = this.findString("'(.*?)'", result);
        final String errorCode = (String) ptuiCB[0];
        final String errorMessage = (String) ptuiCB[4];
        QQUser self = null;
        if (StringUtils.equals("0", errorCode)) {
            final String nick = (String) ptuiCB[5];
            self = new QQUser();
            self.setAccount(account);
            self.setNick(nick);
            this.log.info(String.format("%s%s", self, errorMessage));
        } else {
            this.log.error(errorMessage);
        }
        return self;
    }

    public void online(final QQContext context) {
        this.reset();
        this.getDataFromCookie(context);
        this.fetchChannelInfo(context);
    }

    private void getDataFromCookie(final QQContext context) {
        context.setPtwebqq(this.httpClient.findCookie("ptwebqq"));
        context.setSkey(this.httpClient.findCookie("skey"));
        this.log.info(String.format("获得ptwebqq：%s", context.getPtwebqq()));
        this.log.info(String.format("获得skey：%s", context.getSkey()));
    }

    private void fetchChannelInfo(final QQContext context) {
        final String result = this.getChannelInfo(context);
        final JSONObject retJson = JSONObject.fromObject(result);
        final int retcode = retJson.getInt("retcode");
        if (retcode == 0) {
            final JSONObject resultJson = retJson.getJSONObject("result");
            // long uin = resultJson.getLong("uin");
            context.setVfwebqq(resultJson.getString("vfwebqq"));
            context.setPsessionid(resultJson.getString("psessionid"));
            this.log.info(String.format("获得vfwebqq：%s", context.getVfwebqq()));
            this.log.info(String.format("获得psessionid：%s",
                                        context.getPsessionid()));
        }
    }

    public void offline(final QQContext context) {
        final String statusUrl = "http://d.web2.qq.com/channel/change_status2?newstatus=offline&clientid="
                                 + context.getClientid()
                                 + "&psessionid="
                                 + context.getPsessionid()
                                 + "&t="
                                 + System.currentTimeMillis();
        this.httpClient.getJSON(statusUrl);
        context.setRun(false);
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
                             final String uin) {
        // byte[] bin = DigestUtils.md5(pass);
        // bin = DigestUtils.md5(bin);
        // final String md5Hex = DigestUtils.md5Hex(bin).toUpperCase();
        // return DigestUtils.md5Hex(md5Hex + code).toUpperCase();
        // final String uin = "\\x00\\x00\\x00\\x00\\x99\\xcc\\x39\\x2e";

        final ScriptEngineManager m = new ScriptEngineManager();
        final ScriptEngine se = m.getEngineByName("javascript");
        InputStream input = null;
        Reader reader = null;
        try {
            input = this.getClass()
                        .getClassLoader()
                        .getResource("org/qq4j/core/encode.js")
                        .openStream();
            reader = new InputStreamReader(input);
            se.eval(reader);
            final Object t = se.eval("md5(md5(hexchar2bin(md5('"
                                     + pass
                                     + "'))+'"
                                     + uin
                                     + "')+'"
                                     + code.toUpperCase()
                                     + "')");
            return t.toString();
        } catch (final ScriptException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(reader);
        }
    }

    private String getChannelInfo(final QQContext context) {
        final String url = "http://d.web2.qq.com/channel/login2";

        final JSONObject content = new JSONObject();
        content.put("status", context.getSelf().getStatus());
        content.put("ptwebqq", context.getPtwebqq());
        content.put("passwd_sig", "");
        content.put("clientid", context.getClientid());
        return this.httpClient.postJsonData(url, content);
    }

    public void setHttpClient(final QQHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public boolean isNeedVerify() {
        return this.needVerify;
    }

    public void setNeedVerify(final boolean needVerify) {
        this.needVerify = needVerify;
    }
}
