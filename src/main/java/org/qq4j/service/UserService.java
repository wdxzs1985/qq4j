package org.qq4j.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.qq4j.domain.QQUser;
import org.qq4j.mapper.QQMessagesMapper;
import org.qq4j.mapper.QQUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private QQUserMapper userMapper = null;
    @Autowired
    private QQMessagesMapper messagesMapper = null;

    public List<QQUser> getUsers(final long qq) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("qq", qq);
        return this.userMapper.fetchRanking(params);
    }

    public QQUser getUser(final long qq, final long account) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("qq", qq);
        params.put("account", account);
        return this.userMapper.fetch(params);
    }

    @Transactional
    public void black(final long qq, final long account) {
        final QQUser user = this.getUser(qq, account);
        if (user != null) {
            final Map<String, Object> params = new HashMap<String, Object>();
            params.put("qq", qq);
            params.put("account", account);
            if (user.getBlack() == 0) {
                params.put("faith", 0);
                params.put("black", 1);
                this.messagesMapper.deleteUserMessage(params);
            } else {
                params.put("black", 0);
            }
            this.userMapper.update(params);
        }
    }
}
