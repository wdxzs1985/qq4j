package org.qq4j.mapper;

import java.util.List;
import java.util.Map;

import org.qq4j.domain.QQIndex;
import org.qq4j.domain.QQMessage;

public interface QQMessagesMapper {

    void insertMessage(QQMessage message);

    void insertIndex(QQIndex index);

    List<String> fetchAnswersByMessage(Map<String, Object> params);

    List<String> fetchAnswersByIndex(Map<String, Object> params);

}
