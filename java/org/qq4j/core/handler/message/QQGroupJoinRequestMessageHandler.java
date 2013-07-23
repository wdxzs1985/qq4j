package org.qq4j.core.handler.message;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQMessageHandler;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQUser;

public class QQGroupJoinRequestMessageHandler implements QQMessageHandler {

    private final Log log = LogFactory.getLog(QQGroupJoinRequestMessageHandler.class);

    private String password = null;

    @Override
    public void handle(final QQContext context, final JSONObject json) {
        final JSONObject value = json.getJSONObject("value");
        final long gcode = value.getLong("gcode");
        final long uin = value.getLong("request_uin");
        final String msg = value.getString("msg");
        final QQGroup group = context.getGroupManager().getQQGroup(gcode);
        final QQUser user = context.getFriendManager().getQQUser(uin);
        final QQUser self = context.getUserManager().getSelf();
        if (group != null
            && user != null) {
            this.log.info(String.format("%s >> %s请求入群：%s", self, user, group));
            if (StringUtils.isBlank(this.getPassword())
                || StringUtils.equals(msg, this.getPassword())) {
                this.log.info(String.format("%s >> 同意%s入群%s", self, user, group));
                context.getGroupManager().allowJoinGroup(group, user);
            }
        }
    }

    @Override
    public String getHandleType() {
        return "group_request_join";
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

}
