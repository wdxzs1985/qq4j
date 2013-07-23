package org.qq4j.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.qq4j.core.QQContext;
import org.qq4j.core.QQRobot;
import org.qq4j.domain.QQUser;
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
    public void startup(final QQUser user) {
        this.mRobot.startup(user);
        this.vFrame.setVisible(false);
    }

    @Override
    public void shutdown() {
        this.mRobot.shutdown();
    }

    @Override
    public QQContext getContext() {
        return this.mRobot.getContext();
    }

}
