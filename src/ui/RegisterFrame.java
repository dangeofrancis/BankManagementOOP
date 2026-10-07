package ui;

import service.BankingService;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    private final BankingService bankingService;

    public RegisterFrame(BankingService bankingService) {

        this.bankingService = bankingService;

        setTitle("Banking Application - Create Account");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createUI();
    }

    private void createUI() {

        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel =
                new JLabel("CREATE ACCOUNT");

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        titleLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        // Fields
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);

        JButton createButton =
                new JButton("Create Account");

        JButton backButton =
                new JButton("Back to Login");

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panel.add(titleLabel, gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;

        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;

        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;

        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;

        panel.add(passwordField, gbc);

        // Confirm password
        gbc.gridx = 0;
        gbc.gridy = 3;

        panel.add(new JLabel("Confirm Password:"), gbc);

        gbc.gridx = 1;

        panel.add(confirmPasswordField, gbc);

        // Create button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;

        panel.add(createButton, gbc);

        // Back button
        gbc.gridy = 5;

        panel.add(backButton, gbc);

        add(panel);

        // Create account action
        createButton.addActionListener(e -> createAccount());

        // Back action
        backButton.addActionListener(e -> {
            dispose();
            new LoginFrame(bankingService).setVisible(true); 
        });    }

    private void createAccount() {

        String username =
                usernameField.getText().trim();

        String password =
                new String(passwordField.getPassword());

        String confirmPassword =
                new String(confirmPasswordField.getPassword());

        // Check empty fields
        if (username.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Check username length
        if (username.length() < 6) {

            JOptionPane.showMessageDialog(
                    this,
                    "Username must be at least 6 characters long.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Check password match
        if (!password.equals(confirmPassword)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Passwords do not match.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Register user
        boolean success =
                bankingService.registerUser(
                        username,
                        password
                );

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Account created successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();
		new LoginFrame(bankingService).setVisible(true);
        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Username already exists.",
                    "Registration Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
