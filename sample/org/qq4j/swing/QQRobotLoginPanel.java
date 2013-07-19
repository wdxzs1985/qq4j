package org.qq4j.swing;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;
import org.qq4j.core.QQRobot;
import org.qq4j.core.exception.NeedVerifyCodeException;

public class QQRobotLoginPanel extends JPanel {

    private JTextField mIdInput = null;
    private JPasswordField mPasswordInput = null;
    private JLabel mVerifyCodeLabel = null;
    private JTextField mVerifyCodeInput = null;
    private JButton mLoginButton = null;

    /**
     * 
     */
    private static final long serialVersionUID = -804023106173229071L;

    public QQRobotLoginPanel(final QQRobot robot) {
        final FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        this.setLayout(layout);

        this.mIdInput = new JTextField(16);
        this.add(this.mIdInput);

        this.mPasswordInput = new JPasswordField(16);
        this.add(this.mPasswordInput);

        this.mVerifyCodeLabel = new JLabel();
        this.add(this.mVerifyCodeLabel);

        this.mVerifyCodeInput = new JTextField(4);
        this.mVerifyCodeInput.setVisible(robot.isNeedVerify());
        this.add(this.mVerifyCodeInput);

        this.mLoginButton = new JButton("Login");
        this.mLoginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent ae) {
                final long account = Long.parseLong(QQRobotLoginPanel.this.mIdInput.getText());
                final String password = new String(QQRobotLoginPanel.this.mPasswordInput.getPassword());
                final String verifyCode = QQRobotLoginPanel.this.mVerifyCodeInput.getText();
                try {
                    if (robot.isNeedVerify()) {
                        robot.login(account, password);
                    } else {
                        robot.login(account, password, verifyCode);
                    }
                    if (robot.isRun()) {
                        robot.startup();
                    } else {
                        QQRobotLoginPanel.this.showLogin(robot);
                    }
                } catch (final NumberFormatException ex) {
                    ex.printStackTrace();
                } catch (final NeedVerifyCodeException ex) {
                    QQRobotLoginPanel.this.showVerifyCode(robot, account);
                }
            }
        });
        this.add(this.mLoginButton);

        this.showLogin(robot);
    }

    private void showLogin(final QQRobot robot) {
        this.mIdInput.setText("");
        this.mPasswordInput.setText("");
        this.mVerifyCodeInput.setText("");

        this.mVerifyCodeLabel.setVisible(robot.isNeedVerify());
        this.mVerifyCodeInput.setVisible(robot.isNeedVerify());
        this.invalidate();
    }

    private void showVerifyCode(final QQRobot robot, final long account) {
        try {
            final byte[] verifyImageData = robot.downloadVerifyImage(account);
            final File verifyImageFile = new File("temp/verify.jpg");
            FileUtils.writeByteArrayToFile(verifyImageFile, verifyImageData);
            final BufferedImage myPicture = ImageIO.read(verifyImageFile);
            this.mVerifyCodeLabel.setIcon(new ImageIcon(myPicture));
            this.mVerifyCodeInput.setText("");
            this.mVerifyCodeLabel.setVisible(robot.isNeedVerify());
            this.mVerifyCodeInput.setVisible(robot.isNeedVerify());
        } catch (final IOException ioe) {
            ioe.printStackTrace();
        }
        QQRobotLoginPanel.this.invalidate();
    }
}
