package org.qq4j.swing;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;
import org.qq4j.core.QQRobot;
import org.qq4j.core.QQUserManager;
import org.qq4j.domain.QQUser;

public class QQRobotLoginPanel extends JPanel {

    private JPasswordField mPasswordInput = null;
    private JLabel mVerifyCodeLabel = null;
    private JTextField mVerifyCodeInput = null;
    private JButton mLoginButton = null;

    private boolean needVerify = false;
    /**
     * 
     */
    private static final long serialVersionUID = -804023106173229071L;

    public QQRobotLoginPanel(final QQRobot robot) {
        final FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        this.setLayout(layout);

        this.mPasswordInput = new JPasswordField(16);
        this.add(this.mPasswordInput);

        this.mVerifyCodeLabel = new JLabel();
        this.add(this.mVerifyCodeLabel);

        this.mVerifyCodeInput = new JTextField(4);
        this.mVerifyCodeInput.setVisible(this.needVerify);
        this.add(this.mVerifyCodeInput);

        this.mLoginButton = new JButton("Login");
        this.mLoginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent ae) {
                final String password = new String(QQRobotLoginPanel.this.mPasswordInput.getPassword());
                final String verifyCode = QQRobotLoginPanel.this.mVerifyCodeInput.getText();
                try {
                    // initail login
                    final QQUserManager userManager = robot.getContext()
                                                           .getUserManager();
                    QQRobotLoginPanel.this.needVerify = false;
                    final QQUser user = userManager.login(password, verifyCode);
                    if (user == null) {
                        QQRobotLoginPanel.this.showLogin(robot);
                    } else {
                        robot.startup(user);
                    }
                } catch (final NumberFormatException ex) {
                    ex.printStackTrace();
                } catch (final UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        this.add(this.mLoginButton);
        this.showLogin(robot);
    }

    private void showLogin(final QQRobot robot) {
        final QQUserManager userManager = robot.getContext().getUserManager();
        final String verifyCode = userManager.getVerifyCode();
        if (verifyCode == null) {
            this.needVerify = true;
            this.showVerifyCode(userManager);
        } else {
            this.needVerify = false;
            this.mVerifyCodeLabel.setVisible(this.needVerify);
            this.mVerifyCodeInput.setVisible(this.needVerify);
        }
        this.mPasswordInput.setText("");
        this.mVerifyCodeInput.setText(verifyCode);
        this.invalidate();
    }

    private void showVerifyCode(final QQUserManager userManager) {
        try {
            final byte[] verifyImageData = userManager.downloadVerifyImage();
            final File verifyImageFile = new File("temp/verify.jpg");
            FileUtils.writeByteArrayToFile(verifyImageFile, verifyImageData);
            final BufferedImage myPicture = ImageIO.read(verifyImageFile);
            this.mVerifyCodeLabel.setIcon(new ImageIcon(myPicture));
            this.mVerifyCodeInput.setText("");
            this.mVerifyCodeLabel.setVisible(true);
            this.mVerifyCodeInput.setVisible(true);
        } catch (final IOException ioe) {
            ioe.printStackTrace();
        }
        QQRobotLoginPanel.this.invalidate();
    }
}
