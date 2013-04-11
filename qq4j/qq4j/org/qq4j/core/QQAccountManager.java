package org.qq4j.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQUser;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

public abstract class QQAccountManager {

    private static final Log LOG = LogFactory.getLog(QQAccountManager.class);

    private static final long ERROR_USER = -1;

    private QQContext context;

    protected QQUser fetchUserAccount(final QQUser user) {
        final QQContext context = this.getContext();
        final String url = "http://s.web2.qq.com/api/get_friend_uin2?"
                           + "tuin="
                           + user.getUin()
                           + "&type=1&verifysession=&code="
                           + "&vfwebqq="
                           + context.getVfwebqq()
                           + "&t="
                           + System.currentTimeMillis();
        final String result = context.getHttpClient().getData(url);
        final long account = this.parseUserAccount(result);
        if (account != QQAccountManager.ERROR_USER) {
            user.setAccount(account);
            return user;
        }
        return null;
    }

    protected QQGroup fetchGroupAccount(final QQGroup group) {
        final QQContext context = this.getContext();
        final String url = "http://s.web2.qq.com/api/get_friend_uin2?"
                           + "tuin="
                           + group.getCode()
                           + "&type=4&verifysession=&code="
                           + "&vfwebqq="
                           + context.getVfwebqq()
                           + "&t="
                           + System.currentTimeMillis();
        final String result = context.getHttpClient().getData(url);
        final long account = this.parseUserAccount(result);
        if (account != QQAccountManager.ERROR_USER) {
            group.setAccount(account);
            return group;
        }
        return null;

    }

    private long parseUserAccount(final String result) {
        if (StringUtils.isNotBlank(result)) {
            try {
                final JSONObject retJson = new JSONObject(result);
                if (retJson.getInt("retcode") == 0) {
                    final JSONObject value = retJson.getJSONObject("result");
                    return value.getLong("account");
                } else {
                    QQAccountManager.LOG.error("QQ号码获得失败：" + result);
                }
            } catch (final JSONException e) {
                QQAccountManager.LOG.error("QQ号码解析失败：" + result, e);
            }
        }
        return -1;
    }

    public QQContext getContext() {
        return this.context;
    }

    public void setContext(final QQContext context) {
        this.context = context;
    }
}
