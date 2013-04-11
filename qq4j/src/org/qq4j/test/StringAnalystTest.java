package org.qq4j.test;

import java.util.ArrayList;
import java.util.List;

import org.qq4j.helper.QQStringAnalyst;

public class StringAnalystTest {

    public static void main(final String[] args) {
        String source = "";
        source = "饿了吗？我去给你做饭吧我去给你做饭吧我去给你做饭吧0A0";
        source = "饿了吗？我去给你做饭吧";

        System.out.println("message = " + source);
        int indexLength = source.length();
        indexLength = indexLength > 20 ? 20 : indexLength;
        final List<String> setList = new ArrayList<String>();

        while (indexLength > 1) {
            final List<String> indexs = QQStringAnalyst.analystString(source,
                                                                      indexLength);
            for (final String index : indexs) {
                // System.out.println("index = " + index);
                if (!setList.contains(index)) {
                    setList.add(index);
                }
            }
            indexLength--;
        }

        for (final String index : setList) {
            System.out.println("index = " + index);
        }
    }
}
