package org.qq4j.test;

import java.io.FileNotFoundException;

import org.qq4j.impl.QQRobot;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 
 */
public class QQRobotTest {

    public static void main(final String[] args) throws FileNotFoundException {
        final String ROOT = "/ROOT";
        final ApplicationContext context = new FileSystemXmlApplicationContext(new String[] { ROOT + "/WEB-INF/spring/application-test-context.xml" });
        final QQRobot robot = (QQRobot) context.getBean("default");
        robot.startup();
        // final SinaRobot sinarobot = (SinaRobot)
        // context.getBean("sina_robot");
        // sinarobot.sendWeibo("http://www.tongrenlu.info  #东方同人录#  收集资源中～");
    }
}
