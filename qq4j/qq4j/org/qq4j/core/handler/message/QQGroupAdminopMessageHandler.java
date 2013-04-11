package org.qq4j.core.handler.message;

import java.io.UnsupportedEncodingException;

import org.qq4j.core.QQContext;
import org.qq4j.core.QQSender;
import org.qq4j.core.handler.QQMessageHandler;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQUser;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

public class QQGroupAdminopMessageHandler implements QQMessageHandler {

    @Override
    public void handle(final QQContext context, final JSONObject json)
            throws UnsupportedEncodingException, JSONException {
        final JSONObject value = json.getJSONObject("value");
        final long gcode = value.getLong("gcode");
        final long uin = value.getLong("uin");
        final int type = value.getInt("op_type");
        final QQGroup group = context.getGroupManager().getQQGroup(gcode);
        final QQUser user = context.getFriendManager().getQQUser(uin);
        if (group != null) {
            final QQSender sender = context.getSender();
            if (type == 1) {
                final String answer = String.format("恭喜%s成为管理员。",
                                                    user.getNick(),
                                                    group.getName());
                sender.sendToGroup(group, answer);
            } else if (type == 0) {
                final String answer = String.format("%s的管理员被撤销了。",
                                                    user.getNick(),
                                                    group.getName());
                sender.sendToGroup(group, answer);
            }
        }

    }

    @Override
    public String getHandleType() {
        return "group_admin_op";
    }

}
