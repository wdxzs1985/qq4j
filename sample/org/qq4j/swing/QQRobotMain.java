package org.qq4j.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.qq4j.core.QQRobot;
import org.qq4j.core.exception.NeedVerifyCodeException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 */
public class QQRobotMain implements QQRobot {

    public static void main(final String[] args) {
        final ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/application-context.xml");
        final QQRobot original = (QQRobot) context.getBean("default");
        context.close();

        final QQRobotMain app = new QQRobotMain(original);
        app.startApplication();
    }

    private JFrame vFrame = null;
    private JPanel vLoginPanel = null;
    private QQRobot mRobot = null;

    public void startApplication() {
        this.vFrame = new JFrame();
        this.vFrame.setSize(400, 300);
        this.vFrame.setLocationRelativeTo(null);
        this.vFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.vLoginPanel = new QQRobotLoginPanel(this);
        this.vFrame.add(this.vLoginPanel);
        this.vFrame.setVisible(true);
        this.vFrame.invalidate();
    }

    QQRobotMain(final QQRobot robot) {
        this.mRobot = robot;
    }

    @Override
    public void login(final long account, final String password)
                                                                throws NeedVerifyCodeException {
        this.mRobot.login(account, password);
    }

    @Override
    public void login(final long account,
                      final String password,
                      final String verifyCode) {
        this.mRobot.login(account, password, verifyCode);
    }

    @Override
    public void startup() {
        this.mRobot.startup();
        this.vFrame.setVisible(false);
    }

    @Override
    public void shutdown() {
        this.mRobot.shutdown();
    }

    @Override
    public boolean isRun() {
        return this.mRobot.isRun();
    }

    @Override
    public String getName() {
        return this.mRobot.getName();
    }

    @Override
    public byte[] downloadVerifyImage(final long account) {
        return this.mRobot.downloadVerifyImage(account);
    }

    @Override
    public boolean isNeedVerify() {
        return this.mRobot.isNeedVerify();
    }
}
