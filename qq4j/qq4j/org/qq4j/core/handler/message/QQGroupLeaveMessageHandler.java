package org.qq4j.core.handler.message;

import java.io.UnsupportedEncodingException;

import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQMessageHandler;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQUser;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

public class QQGroupLeaveMessageHandler implements QQMessageHandler {

    @Override
    public void handle(final QQContext context, final JSONObject json)
            throws UnsupportedEncodingException, JSONException {
        final JSONObject value = json.getJSONObject("value");
        final long gcode = value.getLong("gcode");
        final long oldMember = value.getLong("old_member");
        final QQGroup group = context.getGroupManager().getQQGroup(gcode);
        final QQUser user = context.getFriendManager().getQQUser(oldMember);
        if (group != null && user != null) {
            final String answer = String.format("%s被UFO抓走了！", user.getNick());
            context.getSender().sendToGroup(group, answer);
        }
    }

    @Override
    public String getHandleType() {
        return "group_leave";
    }

}
