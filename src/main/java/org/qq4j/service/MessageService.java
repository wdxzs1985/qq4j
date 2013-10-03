package org.qq4j.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.qq4j.domain.QQMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public static final int PAGE_SIZE = 10;

    private Log log = LogFactory.getLog(MessageService.class);

    @Autowired
    private SolrServer solrServer = null;

    public List<QQMessage> getMessages(final Long qq, final Integer page) {
        final List<QQMessage> messages = new ArrayList<QQMessage>();
        final SolrQuery query = new SolrQuery("qq:"
                                              + qq);
        try {
            final QueryResponse response = this.solrServer.query(query);
            final SolrDocumentList list = response.getResults();
            for (final SolrDocument doc : list) {
                final String messageId = (String) doc.getFieldValue("messageId");
                final String message = (String) doc.getFieldValue("message");
                final String answer = (String) doc.getFieldValue("answer");
                final Long owner = (Long) doc.getFieldValue("owner");
                final Integer privatable = (Integer) doc.getFieldValue("privatable");

                final QQMessage messageBean = new QQMessage();
                messageBean.setMessageId(messageId);
                messageBean.setMessage(message);
                messageBean.setAnswer(answer);
                messageBean.setOwner(owner);
                messageBean.setQq(qq);
                messageBean.setPrivatable(privatable);

                messages.add(messageBean);
            }
        } catch (final SolrServerException e) {
            this.log.error(e.getMessage(), e);
        }
        return messages;
    }

    public QQMessage getMessage(final String messageId) {
        final SolrQuery query = new SolrQuery("messageId:"
                                              + messageId);
        try {
            final QueryResponse response = this.solrServer.query(query);
            final SolrDocumentList list = response.getResults();
            for (final SolrDocument doc : list) {
                final String message = (String) doc.getFieldValue("message");
                final String answer = (String) doc.getFieldValue("answer");
                final Long owner = (Long) doc.getFieldValue("owner");
                final Long qq = (Long) doc.getFieldValue("qq");
                final Integer privatable = (Integer) doc.getFieldValue("privatable");

                final QQMessage messageBean = new QQMessage();
                messageBean.setMessageId(messageId);
                messageBean.setMessage(message);
                messageBean.setAnswer(answer);
                messageBean.setOwner(owner);
                messageBean.setQq(qq);
                messageBean.setPrivatable(privatable);

                return messageBean;
            }
        } catch (final SolrServerException e) {
            this.log.error(e.getMessage(), e);
        }
        return null;
    }

    public List<QQMessage> searchAnswer(final String message) {
        final List<QQMessage> answerList = new ArrayList<QQMessage>();
        final SolrQuery query = new SolrQuery("message:"
                                              + message);
        try {
            final QueryResponse response = this.solrServer.query(query);
            final SolrDocumentList list = response.getResults();
            for (final SolrDocument doc : list) {
                final Collection<Object> answeList = doc.getFieldValues("answer");
                if (CollectionUtils.isNotEmpty(answeList)) {
                    for (final Object next : answeList) {
                        final QQMessage messageBean = new QQMessage();
                        messageBean.setMessage(message);
                        messageBean.setAnswer((String) next);
                    }
                }
            }
        } catch (final SolrServerException e) {
            this.log.error(e.getMessage(), e);
        }
        return answerList;
    }

    public void addAnswer(final String message,
                          final String answer,
                          final long account) {
        try {
            final SolrInputDocument inputDocument = new SolrInputDocument();
            inputDocument.addField("messageId", System.nanoTime());
            inputDocument.addField("message", message);
            inputDocument.addField("answer", answer);
            inputDocument.addField("owner", 0);
            inputDocument.addField("qq", account);
            inputDocument.addField("privatable", 0);
            // 登録処理
            this.solrServer.add(inputDocument);
            // 登録した文書はcommitしないと反映されない
            this.solrServer.commit();
        } catch (final SolrServerException e) {
            this.log.error(e.getMessage(), e);
        } catch (final IOException e) {
            this.log.error(e.getMessage(), e);
        }
        return;
    }

    public void updateMessage(final QQMessage messageBean) {
        final SolrInputDocument inputDocument = new SolrInputDocument();
        inputDocument.setField("messageId", messageBean.getMessageId());
        inputDocument.setField("message", messageBean.getMessage());
        inputDocument.setField("answer", messageBean.getAnswer());
        inputDocument.setField("owner", messageBean.getOwner());
        inputDocument.setField("qq", messageBean.getQq());
        inputDocument.setField("privatable", messageBean.getPrivatable());

        // 登録処理
        try {
            this.solrServer.add(inputDocument);
            this.solrServer.commit();
        } catch (final SolrServerException e) {
            this.log.error(e.getMessage(), e);
        } catch (final IOException e) {
            this.log.error(e.getMessage(), e);
        }
        return;
    }

    public void deleteAnswer(final QQMessage messageBean) {
        try {
            this.solrServer.deleteById(messageBean.getMessageId());
            this.solrServer.commit();
        } catch (final SolrServerException e) {
            this.log.error(e.getMessage(), e);
        } catch (final IOException e) {
            this.log.error(e.getMessage(), e);
        }
        return;
    }

}
