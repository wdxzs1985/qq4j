package org.qq4j.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.codec.digest.DigestUtils;
import org.qq4j.core.QQLogin;

public class PasswordTest {

    public static void main(final String[] args) throws FileNotFoundException,
            ScriptException {
        new QQLogin();

        final String epass = PasswordTest.encodePass("bushing",
                                                     "!P8P",
                                                     "\\x00\\x00\\x00\\x00\\x99\\xcc\\x39\\x2e");

        System.out.println("23C316BA89C36FB9D7343335E5B33187");
        System.out.println(epass);
    }

    private static String encodePass(final String pass,
                                     final String vc,
                                     final String uin)
            throws FileNotFoundException, ScriptException {
        // var M=C.p.value;
        // var I=hexchar2bin(md5(M));
        // var H=;
        // var
        // G=md5(md5(hexchar2bin(md5(M))+pt.uin)+C.verifycode.value.toUpperCase());

        final ScriptEngineManager m = new ScriptEngineManager();
        final ScriptEngine se = m.getEngineByName("javascript");
        se.eval(new FileReader(new File(PasswordTest.class.getClassLoader()
                                                          .getResource("org/qq4j/test/common2.js")
                                                          .getPath())));

        Object t = se.eval("md5('" + pass + "')");
        System.out.println("md5(pass) = " + t);
        System.out.println(DigestUtils.md5Hex(pass).toUpperCase());

        t = se.eval("hexchar2bin(md5('" + pass + "'))");
        System.out.println("hexchar2bin(md5(pass)) = " + t);
        System.out.println(DigestUtils.md5Hex(pass).toUpperCase());

        t = se.eval("md5(md5(hexchar2bin(md5('"
                    + pass
                    + "'))+'"
                    + uin
                    + "')+'"
                    + vc
                    + "')");
        return t.toString();
    }
}
