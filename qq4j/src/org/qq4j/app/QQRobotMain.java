package org.qq4j.app;

import java.io.FileNotFoundException;

import org.qq4j.core.exception.NeedVerifyCodeException;

/**
 * 
 */
public class QQRobotMain {

    static QQRobotMainFrame frame = null;

    public static void main(final String[] args) throws FileNotFoundException,
                                                NeedVerifyCodeException {
        frame = new QQRobotMainFrame();
        frame.setVisible(true);
    }
}
