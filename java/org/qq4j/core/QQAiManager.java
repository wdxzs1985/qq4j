package org.qq4j.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.qq4j.domain.QQIndex;
import org.qq4j.domain.QQMessage;
import org.qq4j.domain.QQUser;
import org.qq4j.helper.QQStringAnalyst;
import org.qq4j.mapper.QQMessagesMapper;
import org.qq4j.mapper.QQUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public class QQAiManager {

    public static final String MESSAGE = "message";
    public static final String ANSWER = "answer";

    @Autowired
    private JdbcTemplate jdbcTemplate = null;
    @Autowired
    private QQUserMapper userMapper = null;
    @Autowired
    private QQMessagesMapper messagesMapper = null;

    public String getReplyAnswer(final String message, final QQUser user) {
        final String question = message.toLowerCase();
        final List<String> aiList = this.queryAnswer(question, user);
        return this.getAnswer(aiList);
    }

    public String getReplyAnswerSmart(final String message, final QQUser user) {
        final String question = message.toLowerCase();
        List<String> aiList = this.queryAnswer(question, user);
        if (CollectionUtils.isEmpty(aiList)) {
            final List<String> wordList = this.analystString(question);
            aiList = this.searchAnswersByIndex(wordList, user);
        }
        return this.getAnswer(aiList);
    }

    private List<String> searchAnswersByIndex(final List<String> wordList,
                                              final QQUser user) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("words", wordList);
        params.put("owner", user.getAccount());
        params.put("qq", user.getQq());

        return this.messagesMapper.fetchAnswersByIndex(params);
    }

    private List<String> queryAnswer(final String message, final QQUser user) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("message", message);
        params.put("qq", user.getQq());
        params.put("owner", user.getAccount());
        return this.messagesMapper.fetchAnswersByMessage(params);
    }

    private String getAnswer(final List<String> aiList) {
        if (CollectionUtils.isNotEmpty(aiList)) {
            Collections.shuffle(aiList);
            return aiList.get(0);
        }
        return null;
    }

    @Transactional
    public void addAnswer(final String source,
                          final String answer,
                          final long qq,
                          final long owner) {
        final QQMessage message = new QQMessage();
        message.setMessage(source.toLowerCase());
        message.setAnswer(answer);
        message.setOwner(owner);
        message.setQq(qq);
        this.messagesMapper.insertMessage(message);
        final List<String> wordList = this.analystString(source);
        for (final String word : wordList) {
            final long messageId = message.getId();
            final QQIndex index = new QQIndex();
            index.setWord(word);
            index.setMessageId(messageId);
            this.messagesMapper.insertIndex(index);
        }
    }

    private List<String> analystString(final String source) {
        final List<String> indexs = new ArrayList<String>();
        int splitLength = source.length();
        splitLength = splitLength > QQStringAnalyst.MAX_LENGTH ? QQStringAnalyst.MAX_LENGTH : splitLength;
        while (splitLength > 1) {
            final List<String> analyst = QQStringAnalyst.analystString(source,
                                                                       splitLength);
            for (final String index : analyst) {
                if (!indexs.contains(index)) {
                    indexs.add(index);
                }
            }
            splitLength--;
        }
        return indexs;
    }

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
        final QQUser userRank = this.userMapper.fetchRanking(params);
        return userRank;
    }
}
