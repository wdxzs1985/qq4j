package org.qq4j.core.handler.message;

import net.sf.json.JSONObject;

import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQMessageHandler;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQUser;

public class QQGroupJoinMessageHandler implements QQMessageHandler {

    @Override
    public void handle(final QQContext context, final JSONObject json) {
        final JSONObject value = json.getJSONObject("value");
        final long gcode = value.getLong("gcode");
        final long newMember = value.getLong("new_member");
        final QQGroup group = context.getGroupManager().getQQGroup(gcode);
        final QQUser user = context.getFriendManager().getQQUser(newMember);
        String answer = null;
        if (user != null) {
            if (!user.equals(context.getSelf())) {
                answer = String.format("欢迎%s入裙！", user.getNick());
            } else {
                answer = "大家好，我是新人！请多多关照～～";
            }
            context.getSender().sendToGroup(group, answer);
        }
    }

    @Override
    public String getHandleType() {
        return "group_join";
    }

}
