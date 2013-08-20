package org.qq4j.mapper;

import java.util.List;
import java.util.Map;

import org.qq4j.domain.QQUser;

public interface QQUserMapper {

    void insert(QQUser user);

    QQUser fetch(Map<String, Object> params);

    void update(Map<String, Object> params);

    List<QQUser> fetchRanking(Map<String, Object> params);

    int fetchUserCount(Map<String, Object> params);

}
