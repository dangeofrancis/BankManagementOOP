package ui;

import service.BankingService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminLoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private final BankingService bankingService;

    private final Color COLOR_BG_APP = new Color(11, 19, 30);
    private final Color COLOR_BG_CARD = new Color(21, 30, 45);
    private final Color COLOR_ACCENT_TEAL = new Color(45, 225, 254);
    private final Color COLOR_WARNING_RED = new Color(255, 85, 85);
    private final Color COLOR_TEXT_MUTED = new Color(110, 130, 150);
    private final Color COLOR_CARD_BORDER = new Color(42, 55, 74);

    public AdminLoginFrame(BankingService bankingService) {
        this.bankingService = bankingService;

        setTitle("fApexCore - Admin Terminal");
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
        JLabel titleLabel = new JLabel("Admin Terminal");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0; gbc.insets = new Insets(10, 0, 2, 0);
        cardPanel.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("● ELEVATED PRIVILEGES REQUIRED");
        subtitleLabel.setFont(new Font("Monospaced", Font.BOLD, 10));
        subtitleLabel.setForeground(COLOR_WARNING_RED); // Red warning text
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 25, 0);
        cardPanel.add(subtitleLabel, gbc);

        // INPUTS
        addInputLabel(cardPanel, gbc, "ADMIN IDENTIFIER", 2);
        usernameField = new JTextField(25);
        styleTextField(usernameField, "🛡  Enter admin ID");
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 15, 0);
        cardPanel.add(usernameField, gbc);

        addInputLabel(cardPanel, gbc, "MASTER PASSPHRASE", 4);
        passwordField = new JPasswordField(25);
        styleTextField(passwordField, "🔒  ••••••••••••");
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 25, 0);
        cardPanel.add(passwordField, gbc);

        // BUTTONS
        JButton loginButton = new JButton("➔ Authorize Access");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(COLOR_ACCENT_TEAL);
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(300, 45));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        getRootPane().setDefaultButton(loginButton);
        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 15, 0);
        cardPanel.add(loginButton, gbc);

        JButton backButton = createSecondaryButton("⏴  Return to Secure Login");
        gbc.gridy = 7;
        cardPanel.add(backButton, gbc);

        centerWrapper.add(cardPanel);
        add(centerWrapper, BorderLayout.CENTER);

        // BOTTOM BAR
        addStatusBar();

        // ACTIONS
        loginButton.addActionListener(e -> login());
        backButton.addActionListener(e -> {
            dispose();
            new LoginFrame(bankingService).setVisible(true);
        });
    }

    private void addInputLabel(JPanel panel, GridBagConstraints gbc, String text, int gridy) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(COLOR_TEXT_MUTED);
        gbc.gridy = gridy; gbc.insets = new Insets(5, 0, 2, 0);
        panel.add(label, gbc);
    }

    private void styleTextField(JTextField field, String placeholder) {
        field.setPreferredSize(new Dimension(300, 40));
        field.setBackground(COLOR_BG_APP);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.putClientProperty("JTextField.placeholderText", placeholder);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_CARD_BORDER, 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
    }

    private JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(300, 40));
        btn.setBackground(COLOR_BG_CARD);
        btn.setForeground(Color.WHITE);
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
        JLabel statusLeft = new JLabel("● Administrative Bridge Active   ·   RESTRICTED AREA");
        statusLeft.setFont(new Font("Monospaced", Font.PLAIN, 11));
        statusLeft.setForeground(COLOR_WARNING_RED);
        statusBar.add(statusLeft, BorderLayout.WEST);
        add(statusBar, BorderLayout.SOUTH);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter credentials.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (username.equals("admin") && password.equals("password")) {
            new AdminDashboard(bankingService).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid admin credentials.", "Access Denied", JOptionPane.ERROR_MESSAGE);
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
