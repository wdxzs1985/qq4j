package org.qq4j.app;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;
import org.qq4j.core.exception.NeedVerifyCodeException;
import org.qq4j.impl.QQRobotImpl;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class QQRobotMainFrame extends JFrame {

    private boolean isNeedVerify = false;

    private JTextField mIdInput = null;
    private JPasswordField mPasswordInput = null;
    private JLabel mVerifyCodeLabel = null;
    private JTextField mVerifyCodeInput = null;
    private JButton mLoginButton = null;

    /**
     * 
     */
    private static final long serialVersionUID = -804023106173229071L;

    private QQRobotImpl robot = null;

    public QQRobotMainFrame() {
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        final ConfigurableApplicationContext context = new FileSystemXmlApplicationContext(new String[] { "/spring/application-context.xml" });
        this.robot = (QQRobotImpl) context.getBean("default");
        context.close();

        this.initComponents();
    }

    protected void initComponents() {
        final JPanel panel = new JPanel();
        final FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        panel.setLayout(layout);

        this.mIdInput = new JTextField(16);
        this.mIdInput.setText("");
        panel.add(this.mIdInput);

        this.mPasswordInput = new JPasswordField(16);
        this.mPasswordInput.setText("");
        panel.add(this.mPasswordInput);

        this.mVerifyCodeLabel = new JLabel();
        this.mVerifyCodeLabel.setVisible(this.isNeedVerify);
        panel.add(this.mVerifyCodeLabel);

        this.mVerifyCodeInput = new JTextField(4);
        this.mVerifyCodeInput.setText("");
        this.mVerifyCodeInput.setVisible(this.isNeedVerify);
        panel.add(this.mVerifyCodeInput);

        this.mLoginButton = new JButton("Login");
        this.mLoginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent ae) {
                final long account = Long.parseLong(QQRobotMainFrame.this.mIdInput.getText());
                final String password = new String(QQRobotMainFrame.this.mPasswordInput.getPassword());
                final String verifyCode = QQRobotMainFrame.this.mVerifyCodeInput.getText();
                try {
                    if (!QQRobotMainFrame.this.isNeedVerify) {
                        QQRobotMainFrame.this.robot.login(account, password);
                    } else {
                        QQRobotMainFrame.this.robot.login(account,
                                                          password,
                                                          verifyCode);
                    }
                    QQRobotMainFrame.this.robot.startup();
                } catch (final NumberFormatException ex) {
                    ex.printStackTrace();
                } catch (final NeedVerifyCodeException ex) {
                    QQRobotMainFrame.this.showVerifyCode(account);
                }
            }
        });
        panel.add(this.mLoginButton);

        this.add(panel);
    }

    private void showVerifyCode(final long account) {
        try {
            this.isNeedVerify = true;
            final byte[] verifyImageData = QQRobotMainFrame.this.robot.downloadVerifyImage(account);
            final File verifyImageFile = new File("temp/verify.jpg");
            FileUtils.writeByteArrayToFile(verifyImageFile, verifyImageData);
            final BufferedImage myPicture = ImageIO.read(verifyImageFile);
            this.mVerifyCodeLabel.setIcon(new ImageIcon(myPicture));
            this.mVerifyCodeLabel.setVisible(this.isNeedVerify);
            this.mVerifyCodeInput.setText("");
            this.mVerifyCodeInput.setVisible(this.isNeedVerify);
        } catch (final IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
