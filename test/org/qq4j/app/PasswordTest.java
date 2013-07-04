package org.qq4j.app;

import org.qq4j.core.QQLogin;

public class PasswordTest {

    /**
     * @param args
     */
    public static void main(final String[] args) {
        final QQLogin loginObject = new QQLogin();

        final String uin = "\\x00\\x00\\x00\\x00\\x6d\\xb5\\x47\\xfd";
        final String uin2 = Long.toHexString(1840596989);
        final String pass = "bushing";
        final String code = "nksx".toUpperCase();
        System.out.println(uin);
        System.out.println(loginObject.encodePass(pass, code, uin));
        System.out.println(loginObject.encodePass(pass, code, uin)
                                      .equals("616C06D5ACA4852599C7412605E45936"));
        System.out.println(loginObject.encodePass(pass, code, uin2));

        final String search = "ptui_checkVC('0','!OKU','\\x00\\x00\\x00\\x00\\x17\\xc7\\x59\\x1c');";
        final String[] verifyCode = loginObject.findString("'(.*?)'", search);
        System.out.println(verifyCode[0]);
        System.out.println(verifyCode[1]);
        System.out.println(verifyCode[2]);

        final String search2 = "ptui_checkVC('1','37688d32b019a9a7b31fda00bde2fa013e479c1d2168a8ba','\\x00\\x00\\x00\\x00\\x6d\\xb5\\x47\\xfd');";
        final String[] verifyCode2 = loginObject.findString("'(.*?)'", search2);
        System.out.println(verifyCode2[0]);
        System.out.println(verifyCode2[1]);
        System.out.println(verifyCode2[2]);
    }

}
