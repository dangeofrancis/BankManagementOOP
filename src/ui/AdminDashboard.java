package ui;

import model.Account;
import service.BankingService;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class AdminDashboard extends JFrame {

    private final BankingService bankingService;

    public AdminDashboard(BankingService bankingService) {

        this.bankingService = bankingService;

        setTitle("Banking Application - Admin Dashboard");
        setSize(500, 400);
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
                new JLabel("ADMIN DASHBOARD");

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        titleLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        JButton addFundsButton =
                new JButton("Add Funds to User");

        JButton blockButton =
                new JButton("Block User");

        JButton unblockButton =
                new JButton("Unblock User");

		JButton removeUserButton = new JButton("Remove User");

        JButton logoutButton =
                new JButton("Logout");

        JButton exitButton =
                new JButton("Exit Application");
        gbc.gridy = 5;

        panel.add(
                exitButton,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panel.add(titleLabel, gbc);

        gbc.gridwidth = 2;

        gbc.gridy = 1;

        panel.add(addFundsButton, gbc);

        gbc.gridy = 2;

        panel.add(blockButton, gbc);

        gbc.gridy = 3;

        panel.add(unblockButton, gbc);

		gbc.gridy = 4;
        panel.add(removeUserButton, gbc);

        gbc.gridy = 5;

        panel.add(logoutButton, gbc);

        gbc.gridy = 6;

        panel.add(exitButton, gbc);

        add(panel);

        addFundsButton.addActionListener(
                e -> addFunds()
        );

        blockButton.addActionListener(
                e -> blockUser()
        );

        unblockButton.addActionListener(
                e -> unblockUser()
        );

		removeUserButton.addActionListener(e -> removeUser());

        logoutButton.addActionListener(
                e -> logout()
        );
        exitButton.addActionListener(
                e -> System.exit(0)
        );
    }

    private void addFunds() {

        String username =
                JOptionPane.showInputDialog(
                        this,
                        "Enter target username:"
                );

        if (username == null || username.trim().isEmpty()) {
            return;
        }

        Account account =
                bankingService.findUser(
                        username.trim()
                );

        if (account == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "User not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        String input =
                JOptionPane.showInputDialog(
                        this,
                        "Enter amount to add:"
                );

        if (input == null) {
            return;
        }

        try {

            BigDecimal amount =
                    new BigDecimal(input);

            boolean success =
                    bankingService.addFunds(
                            account,
                            amount
                    );

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Funds added successfully!\n"
                                + "New Balance: ₹"
                                + account.getBalance(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid amount.",
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

    private void blockUser() {

        String username =
                JOptionPane.showInputDialog(
                        this,
                        "Enter username to block:"
                );

        if (username == null || username.trim().isEmpty()) {
            return;
        }

        Account account =
                bankingService.findUser(
                        username.trim()
                );

        if (account == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "User not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        boolean success =
                bankingService.blockUser(account);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "User '" + username
                            + "' has been blocked.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void unblockUser() {

        String username =
                JOptionPane.showInputDialog(
                        this,
                        "Enter username to unblock:"
                );

        if (username == null || username.trim().isEmpty()) {
            return;
        }

        Account account =
                bankingService.findUser(
                        username.trim()
                );

        if (account == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "User not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        boolean success =
                bankingService.unblockUser(account);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "User '" + username
                            + "' has been unblocked.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void logout() {

        dispose();

        LoginFrame loginFrame =
                new LoginFrame(bankingService);

        loginFrame.setVisible(true);
    }

		private void removeUser() {
				String username = JOptionPane.showInputDialog(this, "Enter username to permanently delete:");
				
				if (username == null || username.trim().isEmpty()) {
				return;
				}
		
				// Add a safety confirmation pop-up
				int choice = JOptionPane.showConfirmDialog(
						this,
						"WARNING: Are you sure you want to delete user '" + username + "'? This cannot be undone.",
						"Confirm Deletion",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE
				);
		
				if (choice == JOptionPane.YES_OPTION) {
				boolean success = bankingService.deleteUser(username.trim());
		
				if (success) {
						JOptionPane.showMessageDialog(
								this,
								"User '" + username + "' has been successfully deleted.",
								"Success",
								JOptionPane.INFORMATION_MESSAGE
						);
				} else {
						JOptionPane.showMessageDialog(
								this,
								"Deletion failed. User not found.",
								"Error",
								JOptionPane.ERROR_MESSAGE
						);
				}
			}
	}
}
