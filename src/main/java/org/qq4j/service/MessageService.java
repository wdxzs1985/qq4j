package org.qq4j.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.qq4j.domain.QQIndex;
import org.qq4j.domain.QQMessage;
import org.qq4j.mapper.QQMessagesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {

    public static final int PAGE_SIZE = 10;

    @Autowired
    private QQMessagesMapper messagesMapper = null;

    public List<QQMessage> getMessages(final Long qq, Integer page) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("qq", qq);
        final int count = this.messagesMapper.countMessages(params);
        if (count == 0) {
            return Collections.emptyList();
        }
        if (page == null
            || page < 1) {
            page = 1;
        }
        final int start = (page - 1)
                          * PAGE_SIZE;
        final int end = page
                        * PAGE_SIZE;
        params.put("start", start);
        params.put("end", end);
        return this.messagesMapper.fetchMessages(params);
    }

    public QQMessage getMessage(final String messageId) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("messageId", messageId);
        final QQMessage qqMessage = this.messagesMapper.fetchMessage(params);
        return qqMessage;
    }

    public List<QQMessage> searchAnswer(final String message, final Long qq) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("message", message);
        params.put("qq", qq);
        return this.messagesMapper.fetchAnswersByMessage(params);
    }

    public List<QQMessage> searchAnswersByIndex(final List<String> wordList,
                                                final Long qq) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("words", wordList);
        params.put("qq", qq);
        return this.messagesMapper.fetchAnswersByIndex(params);
    }

    @Transactional
    public void addAnswer(final long qq,
                          final String message,
                          final String answer,
                          final List<String> wordList) {
        final String messageId = this.messagesMapper.getNewMessageId();
        final String source = StringUtils.lowerCase(message);
        final QQMessage messageBean = new QQMessage();
        messageBean.setMessageId(messageId);
        messageBean.setMessage(source);
        messageBean.setAnswer(answer);
        messageBean.setQq(qq);
        messageBean.setPrivatable(1);
        messageBean.setUnknown(0);
        this.messagesMapper.insertMessage(messageBean);
        for (final String word : wordList) {
            final QQIndex index = new QQIndex();
            index.setWord(word);
            index.setMessage(messageBean);
            this.messagesMapper.insertIndex(index);
        }
    }

    @Transactional
    public void updateAnswer(final QQMessage messageBean) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("messageId", messageBean.getMessageId());
        params.put("answer", messageBean.getAnswer());
        this.messagesMapper.updateMessage(params);
    }

    @Transactional
    public void updatePrivatable(final QQMessage messageBean) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("messageId", messageBean.getMessageId());
        params.put("privatable", messageBean.getPrivatable());
        this.messagesMapper.updateMessage(params);
    }

    @Transactional
    public void deleteAnswer(final QQMessage messageBean) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("messageId", messageBean.getMessageId());
        this.messagesMapper.deleteMessage(params);

    }

    public List<String> getIndexes(final String messageId) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("messageId", messageId);
        final List<String> indexes = this.messagesMapper.fetchIndexes(params);
        return indexes;
    }

}
