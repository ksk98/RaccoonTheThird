package com.bots.RacoonClient.Forms;

import com.bots.RacoonClient.Communication.ConnectionSocketManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class LoginWindow extends JFrame {
    private JTextField URLField;
    private JTextField usernameField;
    private JButton loginButton;
    private JPasswordField passwordField;
    private JCheckBox rememberMeCheckBox;
    private JPanel contentPanel;
    private JTextField portField;

    // TODO: save input if checkbox says so
    // TODO: https://stackoverflow.com/questions/48736420/intellij-contentpane-cannot-be-set-to-null-using-swing-designer
    public LoginWindow(String title) throws HeadlessException {
        super(title);
        setContentPane(contentPanel);
        setMinimumSize(new Dimension(200, 100));
        pack();

        loginButton.addActionListener(event -> {
            ConnectionSocketManager connectionSocketManager = ConnectionSocketManager.getInstance();
            int port;
            try {
                port = Integer.parseInt(portField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid port.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                connectionSocketManager.connectTo(URLField.getText(), port);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            connectionSocketManager.login(usernameField.getText(), Arrays.toString(passwordField.getPassword()));
        });
    }
}
