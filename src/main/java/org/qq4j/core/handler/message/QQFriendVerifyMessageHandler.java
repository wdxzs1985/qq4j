package org.qq4j.core.handler.message;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQContext;
import org.qq4j.core.handler.QQMessageHandler;
import org.qq4j.domain.QQUser;

public class QQFriendVerifyMessageHandler implements QQMessageHandler {

    private final Log log = LogFactory.getLog(QQFriendVerifyMessageHandler.class);

    private String password = null;

    @Override
    public void handle(final QQContext context, final JSONObject json) {
        final JSONObject value = json.getJSONObject("value");
        final long account = value.getLong("account");
        final String msg = value.getString("msg");
        final QQUser self = context.getUserManager().getSelf();
        this.log.info(String.format("%s >> %d请求加好友:%s", self, account, msg));
        if (StringUtils.isBlank(this.password)
            || StringUtils.equals(msg, this.password)) {
            this.log.info(String.format("%s >> 同意并加好友：%s", self, account));
            context.getUserManager().allowAddFriend(account);
        }
    }

    @Override
    public String getHandleType() {
        return "verify_required";
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
