package ui;

import model.Account;
import service.BankingService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;

public class AdminDashboard extends JFrame {

    private final BankingService bankingService;
    
    // UI Tables
    private DefaultTableModel ledgerTableModel;
    private DefaultTableModel rankingTableModel;

    // --- Enterprise Color Palette ---
    private final Color COLOR_BG_APP = new Color(11, 19, 30);
    private final Color COLOR_BG_CARD = new Color(21, 30, 45);
    private final Color COLOR_ACCENT_TEAL = new Color(45, 225, 254);
    private final Color COLOR_WARNING_RED = new Color(255, 85, 85);
    private final Color COLOR_TEXT_MUTED = new Color(110, 130, 150);
    private final Color COLOR_CARD_BORDER = new Color(42, 55, 74);
    private final Color COLOR_POS = new Color(40, 200, 100); 

    public AdminDashboard(BankingService bankingService) {
        this.bankingService = bankingService;

        setTitle("Soverign Finances - Core Control Terminal");
        setSize(1200, 850);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG_APP);
        
        createUI();
        refreshData(); // Loads tables on boot
    }

    private void createUI() {
        JPanel mainWrapper = new JPanel(new GridBagLayout());
        mainWrapper.setOpaque(false);
        mainWrapper.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.insets = new Insets(10, 10, 10, 10);
        gbcMain.gridx = 0;

        // ==========================================
        // TOP ZONE: Actions (Left) & Rankings (Right)
        // ==========================================
        JPanel topZone = new JPanel(new GridLayout(1, 2, 20, 0));
        topZone.setOpaque(false);

        // --- 1. Admin Actions Card ---
        RoundedPanel actionCard = new RoundedPanel(15, COLOR_BG_CARD, COLOR_CARD_BORDER);
        actionCard.setLayout(new GridBagLayout());
        actionCard.setBorder(new EmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbcAct = new GridBagConstraints();
        gbcAct.fill = GridBagConstraints.HORIZONTAL; gbcAct.gridx = 0; gbcAct.weightx = 1.0;

        JLabel actHeader = new JLabel("● CORE DIRECTIVES");
        actHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        actHeader.setForeground(COLOR_WARNING_RED);
        gbcAct.gridy = 0; gbcAct.insets = new Insets(0, 0, 15, 0);
        actionCard.add(actHeader, gbcAct);

        JButton addFundsBtn = createSecondaryButton("💸  Inject Funds to Node", Color.WHITE);
        JButton unblockBtn = createSecondaryButton("🔓  Restore Node Access", Color.WHITE);
        JButton blockBtn = createSecondaryButton("🔒  Suspend Node Access", COLOR_WARNING_RED);
        JButton removeBtn = createSecondaryButton("🗑  Purge Node Permanently", COLOR_WARNING_RED);

        addFundsBtn.addActionListener(e -> addFunds());
        unblockBtn.addActionListener(e -> unblockUser());
        blockBtn.addActionListener(e -> blockUser());
        removeBtn.addActionListener(e -> removeUser());

        gbcAct.gridy = 1; gbcAct.insets = new Insets(0, 0, 10, 0); actionCard.add(addFundsBtn, gbcAct);
        gbcAct.gridy = 2; actionCard.add(unblockBtn, gbcAct);
        gbcAct.gridy = 3; actionCard.add(blockBtn, gbcAct);
        gbcAct.gridy = 4; actionCard.add(removeBtn, gbcAct);

        // --- 2. Node Ranking Card (Credit System) ---
        RoundedPanel rankingCard = new RoundedPanel(15, COLOR_BG_CARD, COLOR_CARD_BORDER);
        rankingCard.setLayout(new BorderLayout());
        rankingCard.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel rankHeader = new JLabel("🏆 NODE ACTIVITY & CREDIT SCORE");
        rankHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        rankHeader.setForeground(COLOR_TEXT_MUTED);
        rankHeader.setBorder(new EmptyBorder(0, 0, 10, 0));
        rankingCard.add(rankHeader, BorderLayout.NORTH);

        String[] rankCols = {"RANK", "NODE ID", "TOTAL TX", "SYSTEM CREDITS"};
        rankingTableModel = new DefaultTableModel(rankCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable rankingTable = new JTable(rankingTableModel);
        styleTable(rankingTable, true);
        JScrollPane rankScroll = new JScrollPane(rankingTable);
        rankScroll.getViewport().setBackground(COLOR_BG_CARD);
        rankScroll.setBorder(BorderFactory.createEmptyBorder());
        rankingCard.add(rankScroll, BorderLayout.CENTER);

        topZone.add(actionCard);
        topZone.add(rankingCard);

        gbcMain.gridy = 0; gbcMain.weightx = 1.0; gbcMain.weighty = 0.4;
        mainWrapper.add(topZone, gbcMain);

        // ==========================================
        // MIDDLE ZONE: Global Ledger Table
        // ==========================================
        RoundedPanel ledgerCard = new RoundedPanel(15, COLOR_BG_CARD, COLOR_CARD_BORDER);
        ledgerCard.setLayout(new BorderLayout());
        ledgerCard.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel ledgerHeader = new JLabel("🗄 GLOBAL LEDGER - ALL NETWORK ACTIVITY");
        ledgerHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        ledgerHeader.setForeground(COLOR_TEXT_MUTED);
        ledgerHeader.setBorder(new EmptyBorder(0, 0, 15, 0));
        ledgerCard.add(ledgerHeader, BorderLayout.NORTH);

        String[] ledgerCols = {"TIMESTAMP", "NODE (USERNAME)", "TX TYPE", "AMOUNT"};
        ledgerTableModel = new DefaultTableModel(ledgerCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        
        JTable ledgerTable = new JTable(ledgerTableModel);
        styleTable(ledgerTable, false);
        JScrollPane ledgerScroll = new JScrollPane(ledgerTable);
        ledgerScroll.getViewport().setBackground(COLOR_BG_CARD);
        ledgerScroll.setBorder(BorderFactory.createEmptyBorder());
        
        ledgerCard.add(ledgerScroll, BorderLayout.CENTER);

        gbcMain.gridy = 1; gbcMain.weighty = 0.5;
        mainWrapper.add(ledgerCard, gbcMain);

        // ==========================================
        // BOTTOM ZONE: Session Gateway
        // ==========================================
        RoundedPanel sessionCard = new RoundedPanel(15, COLOR_BG_CARD, COLOR_CARD_BORDER);
        sessionCard.setLayout(new BorderLayout());
        sessionCard.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel sessionInfo = new JLabel("🛡 OMNI-ADMINISTRATOR GATEWAY ACTIVE");
        sessionInfo.setFont(new Font("Monospaced", Font.BOLD, 12));
        sessionInfo.setForeground(COLOR_WARNING_RED);
        
        JPanel exitBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        exitBtns.setOpaque(false);
        JButton btnLogout = createSecondaryButton("Terminate Session", Color.WHITE);
        JButton btnExit = createSecondaryButton("Exit App", COLOR_WARNING_RED);
        
        btnLogout.setPreferredSize(new Dimension(180, 35));
        btnExit.setPreferredSize(new Dimension(120, 35));
        
        btnLogout.addActionListener(e -> { dispose(); new LoginFrame(bankingService).setVisible(true); });
        btnExit.addActionListener(e -> System.exit(0));
        
        exitBtns.add(btnLogout);
        exitBtns.add(btnExit);

        sessionCard.add(sessionInfo, BorderLayout.WEST);
        sessionCard.add(exitBtns, BorderLayout.EAST);

        gbcMain.gridy = 2; gbcMain.weighty = 0.1;
        mainWrapper.add(sessionCard, gbcMain);

        add(mainWrapper, BorderLayout.CENTER);
        addStatusBar();
    }

    // ==========================================
    // DATA SYNCING
    // ==========================================
    private void refreshData() {
        // 1. Refresh Global Ledger
        List<String[]> globalHistory = bankingService.getAllTransactions();
        ledgerTableModel.setRowCount(0);
        for (String[] row : globalHistory) {
            ledgerTableModel.addRow(new Object[]{row[0], row[1], row[2], "₹" + row[3]});
        }

        // 2. Refresh Node Rankings & Credits
        List<String[]> rankings = bankingService.getAccountRankings();
        rankingTableModel.setRowCount(0);
        int rank = 1;
        for (String[] row : rankings) {
            String username = row[0];
            int txCount = Integer.parseInt(row[1]);
            int credits = txCount * 25; // 25 Credits per transaction
            rankingTableModel.addRow(new Object[]{"#" + rank, username, txCount, "💎 " + credits});
            rank++;
        }
    }

    // ==========================================
    // ACTIONS
    // ==========================================
    private void addFunds() {
        String username = JOptionPane.showInputDialog(this, "Enter target username:");
        if (username == null || username.trim().isEmpty()) return;
        Account account = bankingService.findUser(username.trim());
        if (account == null) {
            JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String input = JOptionPane.showInputDialog(this, "Enter amount to inject:");
        if (input != null) {
            try {
                if (bankingService.addFunds(account, new BigDecimal(input))) {
                    refreshData(); // Instantly updates global ledger
                    JOptionPane.showMessageDialog(this, "Funds injected.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Invalid amount."); }
        }
    }

    private void blockUser() {
        String username = JOptionPane.showInputDialog(this, "Enter username to block:");
        if (username != null && !username.trim().isEmpty()) {
            Account account = bankingService.findUser(username.trim());
            if (account != null && bankingService.blockUser(account)) {
                JOptionPane.showMessageDialog(this, "User suspended.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void unblockUser() {
        String username = JOptionPane.showInputDialog(this, "Enter username to unblock:");
        if (username != null && !username.trim().isEmpty()) {
            Account account = bankingService.findUser(username.trim());
            if (account != null && bankingService.unblockUser(account)) {
                JOptionPane.showMessageDialog(this, "User restored.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void removeUser() {
        String username = JOptionPane.showInputDialog(this, "Enter username to purge:");
        if (username != null && !username.trim().isEmpty()) {
            if (JOptionPane.showConfirmDialog(this, "Purge user '" + username + "'?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (bankingService.deleteUser(username.trim())) {
                    refreshData(); // Instantly updates rankings to remove purged user
                    JOptionPane.showMessageDialog(this, "User purged.");
                }
            }
        }
    }

    // ==========================================
    // STYLING HELPERS
    // ==========================================
    private void styleTable(JTable table, boolean isRankingTable) {
        table.setBackground(COLOR_BG_CARD);
        table.setForeground(Color.WHITE);
        table.setGridColor(COLOR_CARD_BORDER);
        table.setRowHeight(35);
        table.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JTableHeader header = table.getTableHeader();
        header.setBackground(COLOR_BG_APP);
        header.setForeground(COLOR_TEXT_MUTED);
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setBorder(BorderFactory.createEmptyBorder());

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, s, f, r, c);
                if (isRankingTable && c == 3) {
                    comp.setForeground(COLOR_ACCENT_TEAL); // Neon blue credits
                } else if (!isRankingTable && c == 3) {
                    comp.setForeground(COLOR_POS); // Green money in ledger
                } else {
                    comp.setForeground(Color.WHITE);
                }
                return comp;
            }
        };
        
        // Apply coloring to the specific columns
        if (isRankingTable) { table.getColumnModel().getColumn(3).setCellRenderer(renderer); } 
        else { table.getColumnModel().getColumn(3).setCellRenderer(renderer); }
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
        JLabel statusLeft = new JLabel("● ROOT ACCESS GRANTED   ·   SYSTEM OBSERVATION MODE");
        statusLeft.setFont(new Font("Monospaced", Font.PLAIN, 11));
        statusLeft.setForeground(COLOR_WARNING_RED);
        statusBar.add(statusLeft, BorderLayout.WEST);
        add(statusBar, BorderLayout.SOUTH);
    }

    class RoundedPanel extends JPanel {
        private final int radius; private final Color bgColor, borderColor;
        public RoundedPanel(int r, Color bg, Color border) { radius = r; bgColor = bg; borderColor = border; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor); g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(borderColor); g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.dispose(); super.paintComponent(g);
        }
    }
}
