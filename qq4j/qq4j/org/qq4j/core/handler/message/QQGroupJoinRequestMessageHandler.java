package org.qq4j.core.handler.message;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQMessageHandler;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQUser;

public class QQGroupJoinRequestMessageHandler implements QQMessageHandler {

    private final Log log = LogFactory.getLog(QQGroupJoinRequestMessageHandler.class);

    private final String password = null;

    @Override
    public void handle(final QQContext context, final JSONObject json) {
        final JSONObject value = json.getJSONObject("value");
        final long gcode = value.getLong("gcode");
        final long uin = value.getLong("request_uin");
        final String msg = value.getString("msg");
        final QQGroup group = context.getGroupManager().getQQGroup(gcode);
        final QQUser user = context.getFriendManager().getQQUser(uin);
        if (group != null && user != null) {
            this.log.info(String.format("%s >> %s请求入群：%s",
                                        context.getSelf(),
                                        user,
                                        group));
            if (StringUtils.isBlank(this.password) || StringUtils.equals(msg,
                                                                         this.password)) {
                this.log.info(String.format("%s >> 同意%s入群%s",
                                            context.getSelf(),
                                            user,
                                            group));
                context.getGroupManager().allowJoinGroup(group, user);
            }
        }
    }

    @Override
    public String getHandleType() {
        return "group_request_join";
    }

}
