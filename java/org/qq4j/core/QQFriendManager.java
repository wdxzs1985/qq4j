package org.qq4j.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.common.SystemConstants;
import org.qq4j.domain.QQUser;
import org.qq4j.mapper.QQUserMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class QQFriendManager extends QQAccountManager {

    private final Log log = LogFactory.getLog(QQFriendManager.class);
    private Map<Long, QQUser> users = null;

    @Autowired
    private QQUserMapper userMapper = null;

    public boolean isBlackList(final QQUser user) {
        return user.getBlack() != 0;
    }

    public void initFriendsInfo() {
        if (this.users == null) {
            this.users = Collections.synchronizedMap(new HashMap<Long, QQUser>());
        } else {
            this.users.clear();
        }
    }

    public String getFriends() {
        final String url = "http://s.web2.qq.com/api/get_user_friends2";
        final String hash = this.hashNative(String.valueOf(this.getContext()
                                                               .getUserManager()
                                                               .getSelf()
                                                               .getAccount()),
                                            this.getContext().getPtwebqq());
        final JSONObject content = new JSONObject();
        content.put("h", "hello");
        content.put("hash", hash);
        content.put("vfwebqq", this.getContext().getVfwebqq());
        final String result = this.getContext()
                                  .getHttpClient()
                                  .postJsonData(url, content);
        return result;
    }

    public String hashNative(final String uin, final String ptwebqq) {
        final int[] a = new int[uin.length()];
        for (int i = 0; i < uin.length(); i++) {
            a[i] = NumberUtils.toInt(String.valueOf(uin.charAt(i)));
        }

        int j = 0;
        int d = -1;
        for (final int element : a) {
            j += element;
            j %= ptwebqq.length();
            int c = 0;
            if (j + 4 > ptwebqq.length()) {
                final int l = 4
                              + j
                              - ptwebqq.length();
                for (int i = 0; i < 4; i++) {
                    c |= i < l ? (ptwebqq.codePointAt(j
                                                      + i) & 255) << (3 - i) * 8 : (ptwebqq.codePointAt(i
                                                                                                        - l) & 255) << (3 - i) * 8;
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    c |= (ptwebqq.codePointAt(j
                                              + i) & 255) << (3 - i) * 8;
                }
            }
            d ^= c;
        }

        final int[] matrix = new int[4];
        matrix[0] = d >> 24 & 255;
        matrix[1] = d >> 16 & 255;
        matrix[2] = d >> 8 & 255;
        matrix[3] = d & 255;

        final String[] code = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

        final StringBuilder hash = new StringBuilder();
        for (final int element : matrix) {
            hash.append(code[element >> 4 & 15]);
            hash.append(code[element & 15]);
        }

        return hash.toString();
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
        final String result = this.getContext().getHttpClient().get(url);
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
        final String result = this.getContext().getHttpClient().get(url);
        return this.parseUserInfo(result);
    }

    private QQUser parseUserInfo(final String result) {
        QQUser user = null;
        if (StringUtils.isNotBlank(result)) {
            final JSONObject retJson = JSONObject.fromObject(result);
            final int retcode = retJson.getInt("retcode");
            if (retcode == 0) {
                final JSONObject value = retJson.getJSONObject("result");
                user = new QQUser();
                user.setUin(value.getLong("uin"));
                user.setNick(value.getString("nick"));
                user.setFace(value.getInt("face"));
                user.setBlood(value.getInt("blood"));
                user.setGender(value.getString("gender"));
                user.setShengxiao(value.getInt("shengxiao"));
                user.setCountry(value.getString("country"));
                user.setProvince(value.getString("province"));
                user.setCity(value.getString("city"));
            } else {
                final String message = String.format("[%s]%s >> 用户信息获得失败。error=%d",
                                                     DateFormatUtils.format(System.currentTimeMillis(),
                                                                            SystemConstants.DATETIME_FORMAT),
                                                     this.getContext()
                                                         .getUserManager()
                                                         .getSelf(),
                                                     retcode);
                this.log.warn(message);
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
        final String result = this.getContext().getHttpClient().get(url);
        return this.parseUserInfo(result);
    }

    public Map<Long, QQUser> getUsers() {
        return this.users;
    }
}
