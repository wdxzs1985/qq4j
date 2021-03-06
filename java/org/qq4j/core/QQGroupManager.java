package org.qq4j.core;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQGroupMember;
import org.qq4j.domain.QQUser;

public class QQGroupManager extends QQAccountManager {

    protected Log log = LogFactory.getLog(QQGroupManager.class);
    private Map<Long, QQGroup> groups = null;

    public void initGroupInfo() {
        this.groups = Collections.synchronizedMap(new HashMap<Long, QQGroup>());
    }

    public QQGroup getQQGroup(final long gcode) {
        QQGroup group = null;
        // synchronized (this.groups) {
        group = this.groups.get(gcode);
        if (group == null
            || group.getMembers() == null) {
            group = this.forceGetQQGroup(gcode);
        }
        // }
        return group;
    }

    public QQGroup forceGetQQGroup(final long gcode) {
        QQGroup group = this.fetchGroupInfo(gcode);
        if (group != null
            && group.getAccount() == 0) {
            group = this.fetchGroupAccount(group);
            this.groups.put(gcode, group);
        }
        return group;
    }

    public QQGroup fetchGroupInfo(final long gcode) {
        final String url = "http://s.web2.qq.com/api/get_group_info_ext2?gcode="
                           + gcode
                           + "&vfwebqq="
                           + this.getContext().getVfwebqq()
                           + "&t="
                           + System.currentTimeMillis();
        final String result = this.getContext().getHttpClient().getJSON(url);
        return this.parseGroupInfo(result);
    }

    private QQGroup parseGroupInfo(final String result) {
        QQGroup group = null;

        if (StringUtils.isNotBlank(result)) {
            try {
                final JSONObject retJson = JSONObject.fromObject(result);
                final int retcode = retJson.getInt("retcode");
                if (retcode == 0) {
                    final JSONObject groupJson = retJson.getJSONObject("result");
                    final Map<Long, QQGroupMember> members = new HashMap<Long, QQGroupMember>();
                    group = new QQGroup();
                    group.setMembers(members);
                    // ginfo
                    final JSONObject ginfo = groupJson.getJSONObject("ginfo");
                    group.setUin(ginfo.getLong("gid"));
                    group.setCode(ginfo.getLong("code"));
                    group.setName(ginfo.getString("name"));
                    // minfo
                    final JSONArray minfo = groupJson.getJSONArray("minfo");

                    for (int i = 0; i < minfo.size(); i++) {
                        final JSONObject userJson = minfo.getJSONObject(i);
                        final long uin = userJson.getLong("uin");
                        final String nick = userJson.getString("nick");

                        final QQGroupMember member = new QQGroupMember();
                        member.setNick(nick);
                        members.put(uin, member);
                    }
                    // cards
                    if (groupJson.has("cards")) {
                        final JSONArray cards = groupJson.getJSONArray("cards");
                        for (int i = 0; i < cards.size(); i++) {
                            final JSONObject cardJson = cards.getJSONObject(i);
                            final long uin = cardJson.getLong("muin");
                            final String card = cardJson.getString("card");
                            final QQGroupMember member = members.get(uin);
                            member.setNick(card);
                        }
                    }
                } else {
                    this.log.warn("群信息获得失败。error="
                                  + retcode);
                }
            } catch (final JSONException e) {
                this.log.error("群信息解析失败："
                               + result, e);
            }
        }
        return group;
    }

    public void allowJoinGroup(final QQGroup group, final QQUser user) {
        final String pollUrl = "http://d.web2.qq.com/channel/op_group_join_req?"
                               + "group_uin="
                               + group.getUin()
                               + "&req_uin="
                               + user.getUin()
                               + "&msg=&op_type=2&clientid="
                               + this.getContext().getClientid()
                               + "&psessionid="
                               + this.getContext().getPsessionid();
        this.getContext().getHttpClient().getJSON(pollUrl);
    }

    public void quitGroup(final QQGroup group) throws JSONException,
                                              UnsupportedEncodingException {
        final QQContext context = this.getContext();
        final String url = "http://s.web2.qq.com/api/quit_group2";

        final JSONObject content = new JSONObject();
        content.put("gcode", group.getCode());
        content.put("vfwebqq", context.getVfwebqq());
        context.getHttpClient().postJsonData(url, content);
    }

    public Map<Long, QQGroup> getGroups() {
        return this.groups;
    }
}
