package ui;

import model.Account;
import service.BankingService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;

public class UserDashboard extends JFrame {

    private final Account account;
    private final BankingService bankingService;
    private JLabel balanceLabel;

    private final Color COLOR_BG_APP = new Color(11, 19, 30);
    private final Color COLOR_BG_CARD = new Color(21, 30, 45);
    private final Color COLOR_ACCENT_TEAL = new Color(45, 225, 254);
    private final Color COLOR_TEXT_MUTED = new Color(110, 130, 150);
    private final Color COLOR_CARD_BORDER = new Color(42, 55, 74);

    public UserDashboard(Account account, BankingService bankingService) {
        this.account = account;
        this.bankingService = bankingService;

        setTitle("fApexCore - Node Operations");
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

        // TITLE & SUBTITLE
        JLabel titleLabel = new JLabel("Node Operations");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0; gbc.insets = new Insets(10, 0, 2, 0);
        cardPanel.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("● WELCOME, " + account.getUsername().toUpperCase());
        subtitleLabel.setFont(new Font("Monospaced", Font.BOLD, 10));
        subtitleLabel.setForeground(COLOR_ACCENT_TEAL);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 30, 0);
        cardPanel.add(subtitleLabel, gbc);

        // BALANCE DISPLAY
        JLabel balanceHeader = new JLabel("AVAILABLE LIQUIDITY");
        balanceHeader.setFont(new Font("Segoe UI", Font.BOLD, 11));
        balanceHeader.setForeground(COLOR_TEXT_MUTED);
        balanceHeader.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 0, 0);
        cardPanel.add(balanceHeader, gbc);

        balanceLabel = new JLabel("₹ " + account.getBalance());
        balanceLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        balanceLabel.setForeground(COLOR_ACCENT_TEAL);
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 30, 0);
        cardPanel.add(balanceLabel, gbc);

        // ACTION BUTTONS
        JButton depositButton = createSecondaryButton("⭳  Deposit Funds");
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 10, 0);
        cardPanel.add(depositButton, gbc);

        JButton withdrawButton = createSecondaryButton("⭱  Withdraw Funds");
        gbc.gridy = 5;
        cardPanel.add(withdrawButton, gbc);

        JButton transferButton = createSecondaryButton("⇌  Transfer to Node");
        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 25, 0);
        cardPanel.add(transferButton, gbc);

        // SEPARATOR
        JPanel separatorPanel = new JPanel(new BorderLayout());
        separatorPanel.setOpaque(false);
        separatorPanel.add(new JSeparator(), BorderLayout.CENTER);
        gbc.gridy = 7; gbc.insets = new Insets(0, 0, 15, 0);
        cardPanel.add(separatorPanel, gbc);

        // EXIT BUTTONS
        JButton logoutButton = createSecondaryButton("⏴  Terminate Session");
        gbc.gridy = 8; gbc.insets = new Insets(0, 0, 10, 0);
        cardPanel.add(logoutButton, gbc);

        JButton exitButton = createSecondaryButton("⏻  Exit Application");
        gbc.gridy = 9;
        cardPanel.add(exitButton, gbc);

        centerWrapper.add(cardPanel);
        add(centerWrapper, BorderLayout.CENTER);

        // BOTTOM STATUS BAR
        addStatusBar();

        // EVENT LISTENERS
        depositButton.addActionListener(e -> depositMoney());
        withdrawButton.addActionListener(e -> withdrawMoney());
        transferButton.addActionListener(e -> transferMoney());
        logoutButton.addActionListener(e -> logout());
        exitButton.addActionListener(e -> System.exit(0));
    }

    private JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(300, 45));
        btn.setBackground(COLOR_BG_CARD);
        btn.setForeground(Color.WHITE);
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
        JLabel statusLeft = new JLabel("● Session Active   ·   Encrypted Tunnel Established");
        statusLeft.setFont(new Font("Monospaced", Font.PLAIN, 11));
        statusLeft.setForeground(new Color(40, 200, 100)); // Green
        statusBar.add(statusLeft, BorderLayout.WEST);
        add(statusBar, BorderLayout.SOUTH);
    }

    private void depositMoney() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
        if (input == null) return;
        try {
            BigDecimal amount = new BigDecimal(input);
            if (bankingService.deposit(account, amount)) {
                updateBalance();
                JOptionPane.showMessageDialog(this, "Deposit successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid deposit amount.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Invalid", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void withdrawMoney() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
        if (input == null) return;
        try {
            BigDecimal amount = new BigDecimal(input);
            if (bankingService.withdraw(account, amount)) {
                updateBalance();
                JOptionPane.showMessageDialog(this, "Withdrawal successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid amount or insufficient balance.", "Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Invalid", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void transferMoney() {
        String targetUser = JOptionPane.showInputDialog(this, "Enter recipient's username:");
        if (targetUser == null || targetUser.trim().isEmpty()) return;
        String input = JOptionPane.showInputDialog(this, "Enter amount to transfer to " + targetUser + ":");
        if (input == null) return;
        try {
            BigDecimal amount = new BigDecimal(input);
            if (bankingService.transfer(account, targetUser.trim(), amount)) {
                updateBalance();
                JOptionPane.showMessageDialog(this, "Successfully transferred ₹" + amount + " to '" + targetUser + "'.", "Transfer Complete", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Transfer failed. Check user existence and funds.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Invalid", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBalance() {
        balanceLabel.setText("₹ " + account.getBalance());
    }

    private void logout() {
        dispose();
        new LoginFrame(bankingService).setVisible(true);
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
