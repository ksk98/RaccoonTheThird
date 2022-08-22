package com.bots.RacoonClient.forms;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {
    private JTextField addressField;
    private JTextField usernameField;
    private JButton loginButton;
    private JPasswordField passwordField;
    private JCheckBox checkBox1;
    private JPanel contentPanel;

    public LoginWindow(String title) throws HeadlessException {
        super(title);
        setContentPane(contentPanel);
        setMinimumSize(new Dimension(200, 100));
        pack();
    }
}
