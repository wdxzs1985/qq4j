package org.qq4j.app;

import java.io.FileNotFoundException;

import org.qq4j.impl.QQRobotImpl;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 
 */
public class QQRobotMain {

    public static void main(final String[] args) throws FileNotFoundException {
        final ConfigurableApplicationContext context = new FileSystemXmlApplicationContext(new String[] { "/spring/application-context.xml" });
        final QQRobotImpl robot = (QQRobotImpl) context.getBean("default");
        robot.startup();
        context.close();
    }
}
