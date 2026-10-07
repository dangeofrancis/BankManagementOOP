package ui;

import model.Account;
import service.BankingService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;

public class AdminDashboard extends JFrame {

    private final BankingService bankingService;

    private final Color COLOR_BG_APP = new Color(11, 19, 30);
    private final Color COLOR_BG_CARD = new Color(21, 30, 45);
    private final Color COLOR_WARNING_RED = new Color(255, 85, 85);
    private final Color COLOR_TEXT_MUTED = new Color(110, 130, 150);
    private final Color COLOR_CARD_BORDER = new Color(42, 55, 74);

    public AdminDashboard(BankingService bankingService) {
        this.bankingService = bankingService;

        setTitle("fApexCore - Core Control Terminal");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG_APP);
        setLayout(new BorderLayout());

        createUI();
    }

    private void createUI() {
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        RoundedPanel cardPanel = new RoundedPanel(20, COLOR_BG_CARD, COLOR_CARD_BORDER);
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        // TITLE
        JLabel titleLabel = new JLabel("Core Control");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0; gbc.insets = new Insets(10, 0, 2, 0);
        cardPanel.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("● ELEVATED PRIVILEGES ACTIVE");
        subtitleLabel.setFont(new Font("Monospaced", Font.BOLD, 10));
        subtitleLabel.setForeground(COLOR_WARNING_RED);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 30, 0);
        cardPanel.add(subtitleLabel, gbc);

        // ADMINISTRATIVE DIRECTIVES
        JButton addFundsButton = createSecondaryButton("💸  Inject Funds to Node", Color.WHITE);
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 10, 0);
        cardPanel.add(addFundsButton, gbc);

        JButton unblockButton = createSecondaryButton("🔓  Restore Node Access", Color.WHITE);
        gbc.gridy = 3; 
        cardPanel.add(unblockButton, gbc);

        JButton blockButton = createSecondaryButton("🔒  Suspend Node Access", COLOR_WARNING_RED);
        gbc.gridy = 4;
        cardPanel.add(blockButton, gbc);

        JButton removeUserButton = createSecondaryButton("🗑  Purge Node Permanently", COLOR_WARNING_RED);
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 25, 0);
        cardPanel.add(removeUserButton, gbc);

        // SEPARATOR
        JPanel separatorPanel = new JPanel(new BorderLayout());
        separatorPanel.setOpaque(false);
        separatorPanel.add(new JSeparator(), BorderLayout.CENTER);
        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 15, 0);
        cardPanel.add(separatorPanel, gbc);

        // EXIT BUTTONS
        JButton logoutButton = createSecondaryButton("⏴  Terminate Secure Session", Color.WHITE);
        gbc.gridy = 7; gbc.insets = new Insets(0, 0, 10, 0);
        cardPanel.add(logoutButton, gbc);

        JButton exitButton = createSecondaryButton("⏻  Shutdown Interface", Color.WHITE);
        gbc.gridy = 8;
        cardPanel.add(exitButton, gbc);

        centerWrapper.add(cardPanel);
        add(centerWrapper, BorderLayout.CENTER);

        // BOTTOM BAR
        addStatusBar();

        // EVENT LISTENERS
        addFundsButton.addActionListener(e -> addFunds());
        blockButton.addActionListener(e -> blockUser());
        unblockButton.addActionListener(e -> unblockUser());
        removeUserButton.addActionListener(e -> removeUser());
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame(bankingService).setVisible(true);
        });
        exitButton.addActionListener(e -> System.exit(0));
    }

    private JButton createSecondaryButton(String text, Color textColor) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(300, 45));
        btn.setBackground(COLOR_BG_CARD);
        btn.setForeground(textColor);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_CARD_BORDER, 1),
                new EmptyBorder(5, 15, 5, 15)
        ));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(COLOR_CARD_BORDER); }
            public void mouseExited(MouseEvent e) { btn.setBackground(COLOR_BG_CARD); }
        });
        return btn;
    }

    private void addStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(COLOR_BG_APP);
        statusBar.setBorder(new EmptyBorder(5, 10, 5, 10));
        JLabel statusLeft = new JLabel("● Admin Bridge Active   ·   RESTRICTED AREA");
        statusLeft.setFont(new Font("Monospaced", Font.PLAIN, 11));
        statusLeft.setForeground(COLOR_WARNING_RED);
        statusBar.add(statusLeft, BorderLayout.WEST);
        add(statusBar, BorderLayout.SOUTH);
    }

    // --- ACTIONS ---
    private void addFunds() {
        String username = JOptionPane.showInputDialog(this, "Enter target username:");
        if (username == null || username.trim().isEmpty()) return;
        Account account = bankingService.findUser(username.trim());
        if (account == null) {
            JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String input = JOptionPane.showInputDialog(this, "Enter amount to inject:");
        if (input == null) return;
        try {
            BigDecimal amount = new BigDecimal(input);
            if (bankingService.addFunds(account, amount)) {
                JOptionPane.showMessageDialog(this, "Funds injected successfully!\nNew Balance: ₹" + account.getBalance(), "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Invalid", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void blockUser() {
        String username = JOptionPane.showInputDialog(this, "Enter username to block:");
        if (username == null || username.trim().isEmpty()) return;
        Account account = bankingService.findUser(username.trim());
        if (account == null) {
            JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (bankingService.blockUser(account)) {
            JOptionPane.showMessageDialog(this, "User '" + username + "' has been suspended.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void unblockUser() {
        String username = JOptionPane.showInputDialog(this, "Enter username to unblock:");
        if (username == null || username.trim().isEmpty()) return;
        Account account = bankingService.findUser(username.trim());
        if (account == null) {
            JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (bankingService.unblockUser(account)) {
            JOptionPane.showMessageDialog(this, "User '" + username + "' has been restored.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void removeUser() {
        String username = JOptionPane.showInputDialog(this, "Enter username to permanently purge:");
        if (username == null || username.trim().isEmpty()) return;
        int choice = JOptionPane.showConfirmDialog(this, "WARNING: Are you sure you want to delete user '" + username + "'? This cannot be undone.", "Confirm Purge", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            if (bankingService.deleteUser(username.trim())) {
                JOptionPane.showMessageDialog(this, "User '" + username + "' has been successfully purged.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Purge failed. User not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bgColor;
        private final Color borderColor;
        public RoundedPanel(int radius, Color bgColor, Color borderColor) {
            this.radius = radius; this.bgColor = bgColor; this.borderColor = borderColor; setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor); g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(borderColor); g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose(); super.paintComponent(g);
        }
    }
}
