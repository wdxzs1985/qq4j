package org.qq4j.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.domain.QQUser;

public class QQAiManager {

    private final Log log = LogFactory.getLog(QQAiManager.class);

    public static final String MESSAGE = "message";
    public static final String ANSWER = "answer";

    // private JdbcTemplate jdbcTemplate = null;

    private QQContext context = null;

    public String getReplyAnswer(final String message,
                                 final long account,
                                 final long owner) {
        // final String question = message.toLowerCase();
        // final List<Map<String, Object>> aiList = this.queryAnswer(question,
        // account,
        // owner);
        // return this.getAnswer(aiList);
        return "baka";
    }

    public String getReplyAnswerSmart(final String message,
                                      final long account,
                                      final long owner) {
        String question = message.toLowerCase();
        List<Map<String, Object>> aiList = this.queryAnswer(question,
                                                            account,
                                                            owner);
        if (CollectionUtils.isEmpty(aiList)) {
            question = this.searchQuestion(question, account, owner);
            if (StringUtils.isNotBlank(question)) {
                aiList = this.queryAnswer(question, account, owner);
            }
        }
        return this.getAnswer(aiList);
    }

    private String searchQuestion(final String question,
                                  final long account,
                                  final long owner) {
        // final List<String> indexs = this.analystString(question);
        // final String search = this.searchQuestionByIndex(indexs, account,
        // owner);
        return null;
    }

    private String searchQuestionByIndex(final List<String> wordList,
                                         final long account,
                                         final long owner) {
        // final int size = wordList.size();
        // final StringBuilder sql = new StringBuilder();
        // sql.append("select MESSAGE, MESSAGE_COUNT FROM (")
        // .append("select MESSAGE, COUNT(A.MESSAGE_ID) as MESSAGE_COUNT FROM (")
        //
        // .append("select MESSAGE_ID FROM QQ_INDEX WHERE ")
        // .append("MESSAGE_INDEX IN ( ");
        //
        // for (int i = 0; i < size; i++) {
        // sql.append(" ? ,");
        // }
        // sql.deleteCharAt(sql.length() - 1).append(")");
        //
        // sql.append(") A, QQ_MESSAGES B WHERE A.MESSAGE_ID = B.MESSAGE_ID(+) and qq = ? and ((owner = ? and privatable = '1') or privatable = '0') GROUP BY MESSAGE")
        // .append(") ORDER BY MESSAGE_COUNT DESC");
        //
        // final ArrayList<Object> args = new ArrayList<Object>(size + 1);
        // for (final String index : wordList) {
        // args.add(index);
        // }
        // args.add(account);
        // args.add(owner);
        //
        // List<Map<String, Object>> questionList = Collections.emptyList();
        // try {
        // questionList = this.jdbcTemplate.queryForList(sql.toString(),
        // args.toArray());
        // } catch (final DataAccessException e) {
        // this.log.error(e.getMessage());
        // }
        // if (CollectionUtils.isNotEmpty(questionList)) {
        // int top = 0;
        // final List<String> subList = new
        // ArrayList<String>(questionList.size());
        // for (final Map<String, Object> next : questionList) {
        // final String question = (String) next.get(QQAiManager.MESSAGE);
        // final int count = ((BigDecimal)
        // next.get("message_count")).intValue();
        // if (count >= top) {
        // subList.add(question);
        // top = count;
        // } else {
        // break;
        // }
        // }
        // Collections.shuffle(subList);
        // return subList.get(0);
        // }
        return null;
    }

    private List<Map<String, Object>> queryAnswer(final String message,
                                                  final long account,
                                                  final long owner) {
        // final String sql = "select qq, message, answer from QQ_MESSAGES" +
        // " where message = ? and qq = ? and ((owner = ? and privatable = '1') or privatable = '0')";
        // try {
        // return this.jdbcTemplate.queryForList(sql,
        // message.toLowerCase(),
        // account,
        // owner);
        // } catch (final DataAccessException e) {
        // this.log.error(e.getMessage());
        // }
        return Collections.emptyList();
    }

    private String getAnswer(final List<Map<String, Object>> aiList) {
        if (CollectionUtils.isNotEmpty(aiList)) {
            Collections.shuffle(aiList);
            return (String) aiList.get(0).get(QQAiManager.ANSWER);
        }
        return null;
    }

    public void addAnswer(final String message,
                          final String answer,
                          final long owner) {
        // final String source = message.toLowerCase();
        // final long qq = this.context.getSelf().getAccount();
        // final String messageId = this.insertNewMessage(source,
        // answer,
        // owner,
        // qq);
        // this.createMessageIndex(messageId, source, qq);
    }

    // private void createMessageIndex(final String messageId,
    // final String source,
    // final long qq) {
    // final String sql =
    // "insert into QQ_INDEX (MESSAGE_INDEX, MESSAGE_ID, QQ) VALUES (?, ?, ?)";
    //
    // final List<String> indexs = this.analystString(source);
    // try {
    // final List<Object[]> batchArgs = new ArrayList<Object[]>();
    // for (final String index : indexs) {
    // batchArgs.add(new Object[] { index, messageId, qq });
    // }
    // this.jdbcTemplate.batchUpdate(sql, batchArgs);
    // } catch (final DataAccessException e) {
    // this.log.error(e.getMessage());
    // }
    // }

    // private List<String> analystString(final String source) {
    // final List<String> indexs = new ArrayList<String>();
    // int splitLength = source.length();
    // splitLength = splitLength > QQStringAnalyst.MAX_LENGTH ?
    // QQStringAnalyst.MAX_LENGTH : splitLength;
    // while (splitLength > 1) {
    // final List<String> analyst = QQStringAnalyst.analystString(source,
    // splitLength);
    // for (final String index : analyst) {
    // if (!indexs.contains(index)) {
    // indexs.add(index);
    // }
    // }
    // splitLength--;
    // }
    // return indexs;
    // }

    // private String insertNewMessage(final String source,
    // final String answer,
    // final long owner,
    // final long qq) {
    // final String sql =
    // "insert into QQ_MESSAGES (MESSAGE_ID, MESSAGE, ANSWER, ADD_DATE, OWNER, QQ, PRIVATABLE) VALUES (?, ?, ?, SYSDATE, ?, ?, '1')";
    // final String messageId = this.queryMessageId(this.jdbcTemplate);
    //
    // try {
    // this.jdbcTemplate.update(sql,
    // messageId,
    // source,
    // answer,
    // owner,
    // this.context.getSelf().getAccount());
    // } catch (final DataAccessException e) {
    // this.log.error(e.getMessage());
    // }
    // return messageId;
    // }

    // private String queryMessageId(final JdbcTemplate jdbcTemplate) {
    // final String sql = "select QQ_MESSAGES_SEQUENCE.nextval from dual";
    // final int nextId = jdbcTemplate.queryForInt(sql);
    // final String prefix = DateFormatUtils.format(System.currentTimeMillis(),
    // "yyyyMMdd");
    // return prefix + StringUtils.leftPad(String.valueOf(nextId), 12, "0");
    // }

    public void increaseFaith(final QQUser user, final int faith) {
        // final String sql;
        // if (this.hasFaith(user)) {
        // sql =
        // "update QQ_FAITH set faith = faith + ? where account = ? and qq = ?";
        // } else {
        // sql =
        // "insert into QQ_FAITH ( faith, account, qq ) values ( ?, ?, ? )";
        // }
        //
        // final long account = user.getAccount();
        // try {
        // this.jdbcTemplate.update(sql,
        // faith,
        // account,
        // this.context.getSelf().getAccount());
        // } catch (final DataAccessException e) {
        // this.log.error(e.getMessage());
        // }
    }

    public Map<String, Object> queryRank(final QQUser user) {
        // if (this.hasFaith(user)) {
        // final String sql = "select faith, rank from " +
        // "(select account, faith, ROW_NUMBER() OVER (ORDER BY FAITH DESC) AS RANK from QQ_FAITH where qq = ?)"
        // + " where account = ?";
        // final long account = user.getAccount();
        // try {
        // return this.jdbcTemplate.queryForMap(sql,
        // this.context.getSelf()
        // .getAccount(),
        // account);
        // } catch (final DataAccessException e) {
        // this.log.error(e.getMessage());
        // }
        // }
        return Collections.emptyMap();
    }

    public boolean hasFaith(final QQUser user) {
        // final String sql =
        // "select count(account) from QQ_FAITH where account = ? and qq = ?";
        // final long account = user.getAccount();
        // try {
        // return this.jdbcTemplate.queryForInt(sql,
        // account,
        // this.context.getSelf()
        // .getAccount()) > 0;
        // } catch (final DataAccessException e) {
        // this.log.error(e.getMessage());
        // }
        return false;
    }

    // public JdbcTemplate getJdbcTemplate() {
    // return this.jdbcTemplate;
    // }
    //
    // public void setJdbcTemplate(final JdbcTemplate jdbcTemplate) {
    // this.jdbcTemplate = jdbcTemplate;
    // }

    public QQContext getContext() {
        return this.context;
    }

    public void setContext(final QQContext context) {
        this.context = context;
    }

}
