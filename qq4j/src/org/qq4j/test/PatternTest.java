package org.qq4j.test;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;

public class PatternTest {

    /**
     * @param args
     */
    public static void main(final String[] args) {

        final String source = "ptui_checkVC('0','!FKZ', '\\x00\\x00\\x00\\x00\\x99\\xcc\\x39\\x2e');";

        String[] result = PatternTest.findString(",\\'([!\\w]+)\\'", source);
        System.out.println(Arrays.toString(result));
        result = PatternTest.findString("\\'([!\\\\0-9a-zA-Z]{2,})\\'", source);
        System.out.println(Arrays.toString(result));
    }

    private static String[] findString(final String pattern, final String search) {
        final Pattern p = Pattern.compile(pattern);
        final Matcher m = p.matcher(search);
        String[] targets = {};
        while (m.find()) {
            targets = ArrayUtils.add(targets, m.group());
        }
        return targets;
    }
}
