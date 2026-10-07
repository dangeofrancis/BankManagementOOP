package ui;

import model.Account;
import service.BankingService;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class UserDashboard extends JFrame {

    private final Account account;
    private final BankingService bankingService;

    private JLabel balanceLabel;

    public UserDashboard(
            Account account,
            BankingService bankingService) {

        this.account = account;
        this.bankingService = bankingService;

        setTitle("Banking Application - User Dashboard");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createUI();
    }

    private void createUI() {

        JPanel panel =
                new JPanel(new GridBagLayout());

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(10, 10, 10, 10);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel =
                new JLabel("USER DASHBOARD");

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        titleLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        // Welcome message
        JLabel welcomeLabel =
                new JLabel(
                        "Welcome, " + account.getUsername()
                );

        welcomeLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        // Balance
        balanceLabel =
                new JLabel(
                        "Balance: ₹"
                                + account.getBalance()
                );

        balanceLabel.setFont(
                new Font("Arial", Font.BOLD, 20)
        );

        balanceLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        // Buttons
        JButton depositButton =
                new JButton("Deposit Money");

        JButton withdrawButton =
                new JButton("Withdraw Money");

		JButton transferButton = new JButton("Transfer Money");

        JButton logoutButton =
                new JButton("Logout");

        JButton exitButton =
                new JButton("Exit Application");

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panel.add(titleLabel, gbc);

        // Welcome
        gbc.gridy = 1;

        panel.add(welcomeLabel, gbc);

        // Balance
        gbc.gridy = 2;

        panel.add(balanceLabel, gbc);

        // Deposit button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;

        panel.add(depositButton, gbc);

        // Withdraw button
        gbc.gridx = 1;

        panel.add(withdrawButton, gbc);

		gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(transferButton, gbc);

        // Logout button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;

        panel.add(logoutButton, gbc);

        // Exit button
        gbc.gridy = 6;

        panel.add(exitButton, gbc);

        add(panel);

        // Button actions
        depositButton.addActionListener(
                e -> depositMoney()
        );

        withdrawButton.addActionListener(
                e -> withdrawMoney()
        );

		transferButton.addActionListener(e -> transferMoney());

        logoutButton.addActionListener(
                e -> logout()
        );

        exitButton.addActionListener(
                e -> exitApplication()
        );
    }

    private void depositMoney() {

        String input =
                JOptionPane.showInputDialog(
                        this,
                        "Enter amount to deposit:"
                );

        if (input == null) {
            return;
        }

        try {

            BigDecimal amount =
                    new BigDecimal(input);

            boolean success =
                    bankingService.deposit(
                            account,
                            amount
                    );

            if (success) {

                updateBalance();

                JOptionPane.showMessageDialog(
                        this,
                        "Deposit successful!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid deposit amount.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid amount.",
                    "Invalid Amount",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void withdrawMoney() {

        String input =
                JOptionPane.showInputDialog(
                        this,
                        "Enter amount to withdraw:"
                );

        if (input == null) {
            return;
        }

        try {

            BigDecimal amount =
                    new BigDecimal(input);

            boolean success =
                    bankingService.withdraw(
                            account,
                            amount
                    );

            if (success) {

                updateBalance();

                JOptionPane.showMessageDialog(
                        this,
                        "Withdrawal successful!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid amount or insufficient balance.",
                        "Withdrawal Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid amount.",
                    "Invalid Amount",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateBalance() {

        balanceLabel.setText(
                "Balance: ₹"
                        + account.getBalance()
        );
    }

    private void logout() {

        dispose();

        LoginFrame loginFrame =
                new LoginFrame(bankingService);

        loginFrame.setVisible(true);
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
		private void transferMoney() {
				String targetUser = JOptionPane.showInputDialog(this, "Enter recipient's username:");
				
				if (targetUser == null || targetUser.trim().isEmpty()) {
				return;
				}
		
				String input = JOptionPane.showInputDialog(this, "Enter amount to transfer to " + targetUser + ":");
				
				if (input == null) {
				return;
				}
		
				try {
				BigDecimal amount = new BigDecimal(input);
				boolean success = bankingService.transfer(account, targetUser.trim(), amount);
		
				if (success) {
						updateBalance(); 
						JOptionPane.showMessageDialog(
								this,
								"Successfully transferred ₹" + amount + " to '" + targetUser + "'.",
								"Transfer Complete",
								JOptionPane.INFORMATION_MESSAGE
						);
				} else {
						JOptionPane.showMessageDialog(
								this,
								"Transfer failed. Please check if the user exists, and ensure you have sufficient funds.",
								"Transfer Error",
								JOptionPane.ERROR_MESSAGE
						);
				}
		
				} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(
						this,
						"Please enter a valid numeric amount.",
						"Invalid Amount",
						JOptionPane.ERROR_MESSAGE
				);
				}
		}
}
