package org.qq4j.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.qq4j.domain.QQUser;
import org.qq4j.mapper.QQUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class QQAiManager {

    public static final String MESSAGE = "message";
    public static final String ANSWER = "answer";

    private Log log = LogFactory.getLog(QQAiManager.class);

    @Autowired
    private QQUserMapper userMapper = null;
    @Autowired
    private SolrServer solrServer = null;

    public String getReplyAnswer(final String message, final QQUser user) {
        String answer = null;
        final SolrQuery params = new SolrQuery("message:"
                                               + message);
        try {
            final QueryResponse response = this.solrServer.query(params);
            final SolrDocumentList list = response.getResults();
            if (CollectionUtils.isNotEmpty(list)) {
                Collections.shuffle(list);
                final SolrDocument doc = list.get(0);
                answer = (String) doc.getFieldValue(ANSWER);
            }
        } catch (final SolrServerException e) {
            this.log.error(e.getMessage());
        }
        return answer;
    }

    public String getReplyAnswerSmart(final String message, final QQUser user) {
        return this.getReplyAnswer(message, user);
    }

    @Transactional
    public void addAnswer(final String source,
                          final String answer,
                          final long qq,
                          final long owner) {
    }

    @Transactional
    public void increaseFaith(final QQUser user, final int faith) {
        user.setFaith(faith
                      + user.getFaith());
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("account", user.getAccount());
        params.put("qq", user.getQq());
        params.put("faith", user.getFaith());
        this.userMapper.update(params);
    }

    public QQUser queryRank(final QQUser user) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("account", user.getAccount());
        params.put("qq", user.getQq());
        final QQUser userRank = this.userMapper.fetchRanking(params).get(0);
        return userRank;
    }

    public int countUser(final QQUser user) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("qq", user.getQq());
        return this.userMapper.fetchUserCount(params);
    }
}
