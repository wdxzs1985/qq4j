package org.qq4j.core;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.domain.QQFont;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQUser;
import org.qq4j.helper.QQMessageParser;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

public class QQSender {

    private final Log log = LogFactory.getLog(QQSender.class);

    private QQContext context;
    private String gfaceKey = null;
    private String gfaceSig = null;
    private boolean canSendGface = false;

    public void initSender() {
        try {
            this.initGfaceSig();
            this.canSendGface = true;
        } catch (final JSONException e) {
            this.log.warn(e.getMessage());
        }
    }

    public void sendToUser(final QQUser user, final String message)
            throws JSONException, UnsupportedEncodingException {
        if (StringUtils.isBlank(message)) {
            return;
        }

        String content = this.makeMessage(message);
        content = QQMessageParser.replaceFace(content);
        // content = QQMessageUtils.replaceCFace(content);

        if (this.log.isDebugEnabled()) {
            this.log.debug(String.format("%s >> 回复%s：%s",
                                         this.context.getSelf(),
                                         user,
                                         content.toString()));
        }

        final String sendMsgUrl = "http://d.web2.qq.com/channel/send_buddy_msg2";
        final JSONObject json = new JSONObject();
        json.put("to", user.getUin());// 要发送的人
        json.put("content", content);
        this.sendMessage(json, sendMsgUrl);
    }

    public void sendToGroup(final QQGroup group, final String message)
            throws JSONException, UnsupportedEncodingException {
        if (StringUtils.isBlank(message)) {
            return;
        }
        final String sendMsgUrl = "http://d.web2.qq.com/channel/send_qun_msg2";
        final JSONObject json = new JSONObject();
        json.put("group_uin", group.getUin());// 要发送的人
        json.put("gcode", group.getCode());

        String content = this.makeMessage(message);
        content = QQMessageParser.replaceFace(content);

        if (this.log.isDebugEnabled()) {
            this.log.debug(String.format("%s >> 回复%s：%s",
                                         this.context.getSelf(),
                                         group,
                                         content.toString()));
        }

        if (this.canSendGface) {
            content = QQMessageParser.replaceGroupCFace(content);
            json.put("key", this.gfaceKey);
            json.put("sig", this.gfaceSig);
        }

        json.put("content", content);
        this.sendMessage(json, sendMsgUrl);
    }

    private String makeMessage(final String message) throws JSONException {
        final QQFont font = this.getContext().getFont();

        final JSONArray msg = new JSONArray();
        msg.add(message);
        // msg.add("[图片]");

        final JSONArray fontJson = new JSONArray();
        fontJson.add("font");

        final JSONObject fontBase = new JSONObject();
        fontBase.put("name", font.getName());
        fontBase.put("size", font.getSize());

        final JSONArray style = new JSONArray();
        style.add(font.isBold() ? 1 : 0);
        style.add(font.isUnderline() ? 1 : 0);
        style.add(font.isItalic() ? 1 : 0);
        fontBase.put("style", style);

        fontBase.put("color", font.getColor());

        fontJson.add(fontBase);
        msg.add(fontJson);

        return msg.toString();
    }

    private void sendMessage(final JSONObject content, final String url)
            throws JSONException, UnsupportedEncodingException {
        final QQContext context = this.getContext();

        content.put("msg_id", new Random().nextInt(10000000));
        content.put("clientid", context.getClientid());
        content.put("psessionid", context.getPsessionid());// 需要这个才能发送

        context.getHttpClient().postJsonData(url, content);
    }

    public void sendShakeMessage(final QQUser user) {
        final QQContext context = this.getContext();
        final String url = "http://d.web2.qq.com/channel/shake2"
                           + "?to_uin="
                           + user.getUin()
                           + "&clientid="
                           + context.getClientid()
                           + "&psessionid="
                           + context.getPsessionid()
                           + "&t="
                           + System.currentTimeMillis();
        context.getHttpClient().getData(url);
    }

    private void initGfaceSig() throws JSONException {
        final QQContext context = this.getContext();
        final String url = "http://d.web2.qq.com/channel/get_gface_sig2"
                           + "?clientid="
                           + context.getClientid()
                           + "&psessionid="
                           + context.getPsessionid()
                           + "&t="
                           + System.currentTimeMillis();
        final String result = context.getHttpClient().getData(url);
        if (StringUtils.isNotBlank(result)) {
            final JSONObject json = new JSONObject(result);
            final int retcode = json.getInt("retcode");
            if (retcode == 0) {
                final JSONObject resultJson = json.getJSONObject("result");
                this.gfaceKey = resultJson.getString("gface_key");
                this.gfaceSig = resultJson.getString("gface_sig");
            }
        }
    }

    public QQContext getContext() {
        return this.context;
    }

    public void setContext(final QQContext context) {
        this.context = context;
    }
}
