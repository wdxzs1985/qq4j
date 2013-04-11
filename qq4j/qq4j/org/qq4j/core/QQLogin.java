package org.qq4j.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.domain.QQUser;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

public class QQLogin {

    protected Log log = LogFactory.getLog(QQLogin.class);

    public static final int APPID = 1003903;

    public boolean login(final QQContext context) {
        final String[] verifyCode = this.getVerifyCode(context);
        if (verifyCode != null) {
            this.log.info(String.format("获得验证码：%s", verifyCode[0]));
            this.log.info(String.format("获得UIN：%s", verifyCode[1]));
            return this.doLogin(context, verifyCode[0], verifyCode[1]);
        }
        return false;
    }

    private String[] getVerifyCode(final QQContext context) {
        final long account = context.getSelf().getAccount();
        // http://check.ptlogin2.qq.com/check?uin=398940444&appid=1003903&r=0.9252068820172412
        final String checkQQUrl = "http://check.ptlogin2.qq.com/check?appid="
                                  + QQLogin.APPID
                                  + "&uin="
                                  + account;
        final String result = context.getHttpClient().getData(checkQQUrl);
        String[] verifyCode = null;
        if (StringUtils.isNotBlank(result)) {
            verifyCode = this.findString("\\'([!\\\\0-9a-zA-Z]{2,})\\'", result);
            // TODO 生成图片验证码
            if (!verifyCode[0].startsWith("!")) {
                // 生成图片验证码
                return null;
            }
        }
        return verifyCode;
    }

    private boolean doLogin(final QQContext context,
                            final String verifyCode,
                            final String uin) {
        final long account = context.getSelf().getAccount();
        final String password = context.getSelf().getPassword();

        final String loginUrl = "http://ptlogin2.qq.com/login?u="
                                + account
                                + "&p="
                                + this.encodePass(password, verifyCode, uin)
                                + "&verifycode="

                                + verifyCode
                                + "&webqq_type=10&remember_uin=1&login2qq=1&aid="
                                + QQLogin.APPID
                                + "&u1=http%3A%2F%2Fweb.qq.com%2Floginproxy.html%3Flogin2qq%3D1%26webqq_type%3D10"
                                + "&h=1&ptredirect=0&ptlang=2052&from_ui=1&pttype=1&dumy=&fp=loginerroralert"
                                + "&action=5-13-9792&mibao_css=m_webqq&t=1&g=1";
        final String result = context.getHttpClient().getData(loginUrl);
        final String nick = this.findString("'登录成功！', '(.+)'\\);", result)[0];
        if (StringUtils.isNotBlank(nick)) {
            final QQUser self = context.getSelf();
            self.setNick(nick);
            this.log.info(String.format("QQ登录成功！QQ:%s", self));
            return true;
        }
        return false;
    }

    public void online(final QQContext context)
            throws UnsupportedEncodingException, JSONException {
        this.getDataFromCookie(context);
        this.fetchChannelInfo(context);
    }

    private void getDataFromCookie(final QQContext context) {
        final QQHttpClient httpClient = context.getHttpClient();
        context.setPtwebqq(httpClient.findCookie("ptwebqq"));
        context.setSkey(httpClient.findCookie("skey"));
        this.log.info(String.format("获得ptwebqq：%s", context.getPtwebqq()));
        this.log.info(String.format("获得skey：%s", context.getSkey()));
    }

    private void fetchChannelInfo(final QQContext context)
            throws UnsupportedEncodingException, JSONException {
        final String result = this.getChannelInfo(context);
        final JSONObject retJson = new JSONObject(result);
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
        // http://d.web2.qq.com/channel/change_status2?newstatus=offline&clientid=31594005&psessionid=8368046764001e636f6e6e7365727665725f77656271714031302e3132382e36362e313132000063e800001b2b016e0400e82c7f976d0000000a407a32336a394a49445a6d00000028681c0860608ca67acfb4f2fdf7a2b707e0e9ed2fe48dcd3b945580965f8070fd3519674e61284311&t=1330534346665
        final String statusUrl = "http://d.web2.qq.com/channel/change_status2?newstatus=offline&clientid="
                                 + context.getClientid()
                                 + "&psessionid="
                                 + context.getPsessionid()
                                 + "&t="
                                 + System.currentTimeMillis();
        context.getHttpClient().getData(statusUrl);
        context.setRun(false);
    }

    private String[] findString(final String pattern, final String search) {
        final Pattern p = Pattern.compile(pattern);
        final Matcher m = p.matcher(search);
        String[] targets = {};
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
        try {
            se.eval(new FileReader(new File(this.getClass()
                                                .getClassLoader()
                                                .getResource("org/qq4j/impl/encode.js")
                                                .getPath())));
            final Object t = se.eval("md5(md5(hexchar2bin(md5('"
                                     + pass
                                     + "'))+'"
                                     + uin
                                     + "')+'"
                                     + code
                                     + "')");
            return t.toString();
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (final ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    private String getChannelInfo(final QQContext context)
            throws JSONException, UnsupportedEncodingException {
        final String url = "http://d.web2.qq.com/channel/login2";

        final JSONObject content = new JSONObject();
        content.put("status", context.getSelf().getStatus());
        content.put("ptwebqq", context.getPtwebqq());
        content.put("passwd_sig", "");
        content.put("clientid", context.getClientid());
        return context.getHttpClient().postJsonData(url, content);
    }
}
