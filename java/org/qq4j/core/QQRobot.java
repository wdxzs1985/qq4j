package org.qq4j.core;

import org.qq4j.core.exception.NeedVerifyCodeException;

public interface QQRobot {

    void login(long account, String password) throws NeedVerifyCodeException;

    void login(long account, String password, String verifyCode);

    public void startup();

    public void shutdown();

    public boolean isRun();

    public boolean isNeedVerify();

    public String getName();

    public byte[] downloadVerifyImage(long account);
}
