package org.qq4j.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.qq4j.helper.QQStringAnalyst;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class StringAnalystBatch {

    public static void main(final String[] args) {
        final String ROOT = "D:/Documents/works/qqweb/ROOT/";
        final ApplicationContext context = new FileSystemXmlApplicationContext(new String[] { ROOT
                                                                                              + "/WEB-INF/spring/jdbc-context.xml" });

        final JdbcTemplate jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");

        final String sql = "select message_id, message, qq from QQ_MESSAGES ";
        final List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        for (final Map<String, Object> resultMap : result) {
            final String messageId = (String) resultMap.get("message_id");
            final String message = (String) resultMap.get("message");
            final BigDecimal qq = (BigDecimal) resultMap.get("qq");
            message.toLowerCase();
            System.out.println("===========================");
            System.out.println("message = " + message);

            int indexLength = message.length();
            indexLength = indexLength > 20 ? 20 : indexLength;
            final List<String> setList = new ArrayList<String>();
            while (indexLength > 1) {
                final List<String> indexs = QQStringAnalyst.analystString(message,
                                                                          indexLength);
                for (final String index : indexs) {
                    if (!setList.contains(index)) {
                        setList.add(index);
                    }
                }
                indexLength--;
            }

            try {
                final List<Object[]> batchArgs = new ArrayList<Object[]>();
                for (final String index : setList) {
                    System.out.println("index = " + index);
                    batchArgs.add(new Object[] { index, messageId, qq.longValue() });
                }

                final String sql2 = "insert into QQ_INDEX (MESSAGE_INDEX, MESSAGE_ID, QQ) VALUES (?, ?, ?)";
                jdbcTemplate.batchUpdate(sql2, batchArgs);
            } catch (final DataAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
