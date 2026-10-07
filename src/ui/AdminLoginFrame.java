package ui;

import service.BankingService;

import javax.swing.*;
import java.awt.*;

public class AdminLoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    private final BankingService bankingService;

    public AdminLoginFrame(BankingService bankingService) {

        this.bankingService = bankingService;

        setTitle("Banking Application - Admin Login");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createUI();
    }

    private void createUI() {

        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(10, 10, 10, 10);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        JLabel titleLabel =
                new JLabel("ADMIN LOGIN");

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        titleLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        usernameField =
                new JTextField(20);

        passwordField =
                new JPasswordField(20);

        JButton loginButton =
                new JButton("Login");

        JButton backButton =
                new JButton("Back");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;

        panel.add(
                new JLabel("Username:"),
                gbc
        );

        gbc.gridx = 1;

        panel.add(
                usernameField,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy = 2;

        panel.add(
                new JLabel("Password:"),
                gbc
        );

        gbc.gridx = 1;

        panel.add(
                passwordField,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;

        panel.add(
                loginButton,
                gbc
        );

        gbc.gridy = 4;

        panel.add(
                backButton,
                gbc
        );

        add(panel);

        loginButton.addActionListener(
                e -> login()
        );

        backButton.addActionListener(
                e -> dispose()
        );
    }

    private void login() {

        String username =
                usernameField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        if (username.isEmpty()
                || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Existing admin credentials
        if (username.equals("admin")
                && password.equals("password")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Admin login successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            AdminDashboard dashboard =
                    new AdminDashboard(bankingService);

            dashboard.setVisible(true);

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid admin username or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}