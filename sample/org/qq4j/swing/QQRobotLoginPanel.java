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
import org.qq4j.core.QQUserManager;
import org.qq4j.domain.QQUser;

public class QQRobotLoginPanel extends JPanel {

    private JTextField mIdInput = null;
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

        this.mIdInput = new JTextField(16);
        this.add(this.mIdInput);

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
                final long account = Long.parseLong(QQRobotLoginPanel.this.mIdInput.getText());
                final String password = new String(QQRobotLoginPanel.this.mPasswordInput.getPassword());
                String verifyCode = null;
                try {
                    // initail login
                    final QQUserManager userManager = robot.getContext()
                                                           .getUserManager();
                    if (!QQRobotLoginPanel.this.needVerify) {
                        verifyCode = userManager.getVerifyCode();
                    } else {
                        verifyCode = QQRobotLoginPanel.this.mVerifyCodeInput.getText();
                    }
                    if (verifyCode == null) {
                        QQRobotLoginPanel.this.needVerify = true;
                        QQRobotLoginPanel.this.showVerifyCode(userManager);
                    } else {
                        QQRobotLoginPanel.this.needVerify = false;
                        final QQUser user = userManager.login(password,
                                                              verifyCode);
                        if (user == null) {
                            QQRobotLoginPanel.this.showLogin();
                        } else {
                            robot.startup(user);
                        }
                    }
                } catch (final NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.add(this.mLoginButton);
        this.showLogin();
    }

    private void showLogin() {
        this.mIdInput.setText("");
        this.mPasswordInput.setText("");
        this.mVerifyCodeInput.setText("");

        this.mVerifyCodeLabel.setVisible(this.needVerify);
        this.mVerifyCodeInput.setVisible(this.needVerify);
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
