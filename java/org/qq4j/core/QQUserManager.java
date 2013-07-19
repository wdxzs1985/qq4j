package org.qq4j.core;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.common.SystemConstants;
import org.qq4j.domain.QQUser;
import org.qq4j.mapper.QQUserMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class QQUserManager extends QQAccountManager {

    private final Log log = LogFactory.getLog(QQUserManager.class);
    private Map<Long, QQUser> users = null;

    @Autowired
    private QQUserMapper userMapper = null;

    public boolean isBlackList(final QQUser user) {
        return user.getBlack() != 0;
    }

    public void initFriendsInfo() {
        this.users = Collections.synchronizedMap(new HashMap<Long, QQUser>());
    }

    public QQUser getQQUser(final long uin) {
        // get from cache
        QQUser user = null;
        // synchronized (this.users) {
        user = this.users.get(uin);
        if (user == null) {
            // get from friend api
            user = this.fetchFriendInfo(uin);
        }
        // get user qq
        if (user != null
            && user.getAccount() == 0) {
            user = this.fetchUserAccount(user);
            if (user != null) {
                this.registerUser(user);
                this.users.put(uin, user);
            }
        }
        // }
        return user;
    }

    private void registerUser(final QQUser user) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("qq", user.getQq());
        params.put("account", user.getAccount());
        final QQUser userInfo = this.userMapper.fetch(params);
        if (userInfo == null) {
            this.userMapper.insert(user);
        } else {
            user.setFaith(userInfo.getFaith());
            user.setBlack(userInfo.getBlack());
        }
    }

    protected QQUser fetchFriendInfo(final long uin) {
        final String url = "http://s.web2.qq.com/api/get_friend_info2?"
                           + "tuin="
                           + uin
                           + "&verifysession=&gid=0&code="
                           + "&vfwebqq="
                           + this.getContext().getVfwebqq()
                           + "&t="
                           + System.currentTimeMillis();
        final String result = this.getContext().getHttpClient().getJSON(url);
        QQUser user = this.parseUserInfo(result);
        if (user == null) {
            // get from stranger api
            user = this.fetchStrangerInfo(uin);
        }
        return user;
    }

    protected QQUser fetchStrangerInfo(final long uin) {
        final String url = "http://s.web2.qq.com/api/get_stranger_info2?"
                           + "tuin="
                           + uin
                           + "&verifysession=&gid=0&code="
                           + "&vfwebqq="
                           + this.getContext().getVfwebqq()
                           + "&t="
                           + System.currentTimeMillis();
        final String result = this.getContext().getHttpClient().getJSON(url);
        return this.parseUserInfo(result);
    }

    private QQUser parseUserInfo(final String result) {
        QQUser user = null;
        if (StringUtils.isNotBlank(result)) {
            try {
                final JSONObject retJson = JSONObject.fromObject(result);
                final int retcode = retJson.getInt("retcode");
                if (retcode == 0) {
                    final JSONObject value = retJson.getJSONObject("result");
                    user = new QQUser();
                    user.setUin(value.getLong("uin"));
                    user.setNick(value.getString("nick"));
                } else {
                    final String message = String.format("[%s]%s >> 用户信息获得失败。error=%d",
                                                         DateFormatUtils.format(System.currentTimeMillis(),
                                                                                SystemConstants.DATETIME_FORMAT),
                                                         this.getContext()
                                                             .getSelf(),
                                                         retcode);
                    this.log.warn(message);
                }
            } catch (final JSONException e) {
                this.log.error("用户信息解析失败："
                               + result, e);
            }
        }
        return user;
    }

    public QQUser searchFriendInfo(final long qq) {
        final String url = "http://s.web2.qq.com/api/search_qq_by_uin2?"
                           + "tuin="
                           + qq
                           + "&verifysession=&code="
                           + "&vfwebqq="
                           + this.getContext().getVfwebqq()
                           + "&t="
                           + System.currentTimeMillis();
        final String result = this.getContext().getHttpClient().getJSON(url);
        return this.parseUserInfo(result);
    }

    public void allowAddFriend(final long account) {
        //
        final QQContext context = this.getContext();
        final String url = "http://s.web2.qq.com/api/allow_and_add2";
        final JSONObject content = new JSONObject();
        content.put("account", account);
        content.put("gid", 0);
        content.put("mname", "");
        content.put("vfwebqq", context.getVfwebqq());
        context.getHttpClient().postJsonData(url, content);
    }

    public void setLongNick(final String nlk) throws JSONException,
                                             UnsupportedEncodingException {
        final QQContext context = this.getContext();
        final String url = "http://s.web2.qq.com/api/set_long_nick2";
        final JSONObject content = new JSONObject();
        content.put("nlk", nlk);
        content.put("vfwebqq", context.getVfwebqq());
        context.getHttpClient().postJsonData(url, content);
    }

    public Map<Long, QQUser> getUsers() {
        return this.users;
    }
}
