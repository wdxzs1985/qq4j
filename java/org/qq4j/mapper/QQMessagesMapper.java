package org.qq4j.mapper;

import java.util.List;
import java.util.Map;

import org.qq4j.domain.QQIndex;
import org.qq4j.domain.QQMessage;

public interface QQMessagesMapper {

    void insertMessage(QQMessage message);

    void insertIndex(QQIndex index);

    List<QQMessage> fetchAnswersByMessage(Map<String, Object> params);

    List<QQMessage> fetchAnswersByIndex(Map<String, Object> params);

    String getNewMessageId();

    int countMessages(Map<String, Object> params);

    List<QQMessage> fetchMessages(Map<String, Object> params);

    QQMessage fetchMessage(Map<String, Object> params);

    void updateMessage(QQMessage messageBean);

    List<String> fetchIndexes(Map<String, Object> params);

    void removeIndex(QQMessage messageBean);
}
