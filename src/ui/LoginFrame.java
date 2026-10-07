package ui;

import model.Account;
import service.BankingService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    private final BankingService bankingService;

    public LoginFrame(BankingService bankingService) {

        this.bankingService = bankingService;

        setTitle("Banking Application - Login");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createUI();
    }

    private void createUI() {

        JPanel panel =
                new JPanel(new GridBagLayout());

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(8, 8, 8, 8);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel =
                new JLabel("BANKING APPLICATION");

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        titleLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        // Fields
        usernameField =
                new JTextField(20);

        passwordField =
                new JPasswordField(20);

        // Buttons
        JButton loginButton =
                new JButton("Login");

        JButton registerButton =
                new JButton("Create Account");

        JButton adminButton =
                new JButton("Login as Admin");

        JButton exitButton =
                new JButton("Exit Application");

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panel.add(titleLabel, gbc);

        // Username
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

        // Password
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

        // Login
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;

        panel.add(
                loginButton,
                gbc
        );

        // Create Account
        gbc.gridy = 4;

        panel.add(
                registerButton,
                gbc
        );

        // Admin Login
        gbc.gridy = 5;

        panel.add(
                adminButton,
                gbc
        );

        // Exit
        gbc.gridy = 6;

        panel.add(
                exitButton,
                gbc
        );

        add(panel);

        // Button actions
        loginButton.addActionListener(
                e -> login()
        );

        registerButton.addActionListener(e -> {

            RegisterFrame registerFrame =
                    new RegisterFrame(bankingService);

            registerFrame.setVisible(true);
		dispose();
        });

        adminButton.addActionListener(e -> {

            AdminLoginFrame adminLoginFrame =
                    new AdminLoginFrame(bankingService);

            adminLoginFrame.setVisible(true);
		dispose();
        });

        exitButton.addActionListener(
                e -> exitApplication()
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

        Account account =
                bankingService.loginUser(
                        username,
                        password
                );

        if (account == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid username/password or account is blocked.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Login successful!\nWelcome, "
                            + account.getUsername(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            UserDashboard dashboard =
                    new UserDashboard(
                            account,
                            bankingService
                    );

            dashboard.setVisible(true);

            dispose();
        }
    }

    private void exitApplication() {

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to exit?",
                        "Exit Application",
                        JOptionPane.YES_NO_OPTION
                );

        if (choice == JOptionPane.YES_OPTION) {

            System.exit(0);
        }
    }
}
