package ui;

import model.Account;
import service.BankingService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private final BankingService bankingService;

    // --- COLOR PALETTE FROM MOCKUP ---
    private final Color COLOR_BG_APP = new Color(11, 19, 30);      // Deep outer navy
    private final Color COLOR_BG_CARD = new Color(21, 30, 45);     // Lighter inner card
    private final Color COLOR_ACCENT_TEAL = new Color(45, 225, 254); // Neon cyan button
    private final Color COLOR_TEXT_MUTED = new Color(110, 130, 150); // Gray text
    private final Color COLOR_CARD_BORDER = new Color(42, 55, 74);   // Outline of card

    public LoginFrame(BankingService bankingService) {
        this.bankingService = bankingService;

        setTitle("fApexCore Banking Client");
        setSize(1024, 768); // Larger window to show off the centered card
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Sets the entire app background
        getContentPane().setBackground(COLOR_BG_APP);
        setLayout(new BorderLayout());

        createUI();
    }

    private void createUI() {
        // --- CENTER CARD CONTAINER ---
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false); // Transparent so outer background shows

        // Custom rounded panel for the main card
        RoundedPanel cardPanel = new RoundedPanel(20, COLOR_BG_CARD, COLOR_CARD_BORDER);
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setBorder(new EmptyBorder(40, 50, 40, 50)); // Inner padding

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        // 1. LOGO
        try {
            ImageIcon originalIcon = new ImageIcon("assets/logo.png");
            Image scaledImage = originalIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.gridy = 0; gbc.gridwidth = 1;
            cardPanel.add(logoLabel, gbc);
        } catch (Exception e) {
            System.out.println("Logo not found, continuing without it.");
        }

        // 2. TITLE & SUBTITLE
        JLabel titleLabel = new JLabel("ApexCore Banking");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1; gbc.insets = new Insets(10, 0, 2, 0);
        cardPanel.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("● WORKSTATION CLIENT EDITION");
        subtitleLabel.setFont(new Font("Monospaced", Font.BOLD, 10));
        subtitleLabel.setForeground(COLOR_ACCENT_TEAL);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 30, 0);
        cardPanel.add(subtitleLabel, gbc);

        // 3. INPUT FIELDS
        gbc.insets = new Insets(5, 0, 2, 0);
        JLabel userLabel = new JLabel("ACCOUNT / USERNAME");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        userLabel.setForeground(COLOR_TEXT_MUTED);
        gbc.gridy = 3;
        cardPanel.add(userLabel, gbc);

        usernameField = new JTextField(25);
        styleTextField(usernameField, "👤  Enter username or account ID");
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 15, 0);
        cardPanel.add(usernameField, gbc);

        gbc.insets = new Insets(5, 0, 2, 0);
        JLabel passLabel = new JLabel("PASSWORD");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        passLabel.setForeground(COLOR_TEXT_MUTED);
        gbc.gridy = 5;
        cardPanel.add(passLabel, gbc);

        passwordField = new JPasswordField(25);
        styleTextField(passwordField, "🔒  ••••••••••••");
        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 25, 0);
        cardPanel.add(passwordField, gbc);

        // 4. MAIN LOGIN BUTTON
        JButton loginButton = new JButton("➔ Login to Account");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(COLOR_ACCENT_TEAL);
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(300, 45));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        getRootPane().setDefaultButton(loginButton);
        gbc.gridy = 7; gbc.insets = new Insets(0, 0, 25, 0);
        cardPanel.add(loginButton, gbc);

        // 5. SEPARATOR (DIRECTIVES)
        JPanel separatorPanel = new JPanel(new BorderLayout());
        separatorPanel.setOpaque(false);
        JSeparator leftSep = new JSeparator(); leftSep.setForeground(COLOR_CARD_BORDER);
        JSeparator rightSep = new JSeparator(); rightSep.setForeground(COLOR_CARD_BORDER);
        JLabel dirLabel = new JLabel("  DIRECTIVES  ");
        dirLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        dirLabel.setForeground(COLOR_TEXT_MUTED);
        separatorPanel.add(leftSep, BorderLayout.WEST);
        separatorPanel.add(dirLabel, BorderLayout.CENTER);
        separatorPanel.add(rightSep, BorderLayout.EAST);
        
        gbc.gridy = 8; gbc.insets = new Insets(0, 0, 15, 0);
        cardPanel.add(separatorPanel, gbc);

        // 6. SECONDARY BUTTONS
        JButton registerButton = createSecondaryButton("👤+  Create New Account");
        gbc.gridy = 9; gbc.insets = new Insets(0, 0, 10, 0);
        cardPanel.add(registerButton, gbc);

        JButton adminButton = createSecondaryButton("🛡  Login as Administrator");
        gbc.gridy = 10;
        cardPanel.add(adminButton, gbc);

        JButton exitButton = createSecondaryButton("⏻  Exit Application");
        gbc.gridy = 11;
        cardPanel.add(exitButton, gbc);

        // 7. CARD FOOTER
        JPanel cardFooter = new JPanel(new BorderLayout());
        cardFooter.setOpaque(false);
        JLabel buildLabel = new JLabel("Build 1.2");
        buildLabel.setForeground(COLOR_TEXT_MUTED);
        buildLabel.setFont(new Font("Monospaced", Font.PLAIN, 10));
        JLabel statusLabel = new JLabel("● Operational Core");
        statusLabel.setForeground(new Color(40, 200, 100)); // Green dot
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        cardFooter.add(buildLabel, BorderLayout.WEST);
        cardFooter.add(statusLabel, BorderLayout.EAST);
        
        gbc.gridy = 12; gbc.insets = new Insets(30, 0, 0, 0);
        cardPanel.add(cardFooter, gbc);

        // Add Card to Wrapper, Wrapper to Frame
        centerWrapper.add(cardPanel);
        add(centerWrapper, BorderLayout.CENTER);

        // --- 8. BOTTOM STATUS BAR ---
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(COLOR_BG_APP);
        statusBar.setBorder(new EmptyBorder(5, 10, 5, 10));
        JLabel statusLeft = new JLabel("● Connected to Secure Core   ·   Node: 127.0.0.1   ·   Port: 8443 (mTLS)");
        statusLeft.setFont(new Font("Monospaced", Font.PLAIN, 11));
        statusLeft.setForeground(COLOR_TEXT_MUTED);
        JLabel statusRight = new JLabel("TLS 1.3 FIPS 140-3");
        statusRight.setFont(new Font("Monospaced", Font.PLAIN, 11));
        statusRight.setForeground(COLOR_TEXT_MUTED);
        statusBar.add(statusLeft, BorderLayout.WEST);
        statusBar.add(statusRight, BorderLayout.EAST);
        add(statusBar, BorderLayout.SOUTH);

        // --- EVENT LISTENERS ---
        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> {
            new RegisterFrame(bankingService).setVisible(true);
            dispose();
        });
        adminButton.addActionListener(e -> {
            new AdminLoginFrame(bankingService).setVisible(true);
            dispose();
        });
        exitButton.addActionListener(e -> System.exit(0));
    }

    // Helper: Styles inputs to match the dark outlined look
    private void styleTextField(JTextField field, String placeholder) {
        field.setPreferredSize(new Dimension(300, 40));
        field.setBackground(COLOR_BG_APP); // Darker inside
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.putClientProperty("JTextField.placeholderText", placeholder);
        field.putClientProperty("JComponent.roundRect", true);
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

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Account account = bankingService.loginUser(username, password);
        if (account == null) {
            JOptionPane.showMessageDialog(this, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        } else {
            new UserDashboard(account, bankingService).setVisible(true);
            dispose();
        }
    }

    class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bgColor;
        private final Color borderColor;

        public RoundedPanel(int radius, Color bgColor, Color borderColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            this.borderColor = borderColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            // Enables smooth anti-aliased corners
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Fill background
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            
            // Draw subtle border
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
