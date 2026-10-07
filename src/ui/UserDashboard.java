package ui;

import model.Account;
import service.BankingService;

// JFreeChart Imports
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

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

public class UserDashboard extends JFrame {

    private final Account account;
    private final BankingService bankingService;
    
    // UI Updatables
    private JLabel balanceLabel;
    private DefaultTableModel tableModel;
    private DefaultCategoryDataset chartDataset;

    // --- Enterprise Color Palette ---
    private final Color COLOR_BG_APP = new Color(11, 19, 30);
    private final Color COLOR_BG_CARD = new Color(21, 30, 45);
    private final Color COLOR_ACCENT_TEAL = new Color(45, 225, 254);
    private final Color COLOR_TEXT_MUTED = new Color(110, 130, 150);
    private final Color COLOR_CARD_BORDER = new Color(42, 55, 74);
    private final Color COLOR_POS = new Color(40, 200, 100); // Green for credits
    private final Color COLOR_NEG = new Color(255, 85, 85);  // Red for debits

    public UserDashboard(Account account, BankingService bankingService) {
        this.account = account;
        this.bankingService = bankingService;

        setTitle("Soverign Finances - Workstation Client");
        setSize(1200, 850); // Expanded window for the massive new layout
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG_APP);
        
        createUI();
        refreshData(); // Loads the graph and table on boot
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
        // TOP ZONE: Balance (Left) & Graph (Right)
        // ==========================================
        JPanel topZone = new JPanel(new GridLayout(1, 2, 20, 0));
        topZone.setOpaque(false);

        // --- 1. Balance Card ---
        RoundedPanel balanceCard = new RoundedPanel(15, COLOR_BG_CARD, COLOR_CARD_BORDER);
        balanceCard.setLayout(new GridBagLayout());
        balanceCard.setBorder(new EmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbcBal = new GridBagConstraints();
        gbcBal.fill = GridBagConstraints.HORIZONTAL; gbcBal.gridx = 0;

        JLabel balHeader = new JLabel("● TOTAL AVAILABLE BALANCE");
        balHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        balHeader.setForeground(COLOR_TEXT_MUTED);
        gbcBal.gridy = 0; gbcBal.insets = new Insets(0, 0, 10, 0);
        balanceCard.add(balHeader, gbcBal);

        balanceLabel = new JLabel("$0.00");
        balanceLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        balanceLabel.setForeground(COLOR_ACCENT_TEAL);
        gbcBal.gridy = 1; gbcBal.insets = new Insets(0, 0, 30, 0);
        balanceCard.add(balanceLabel, gbcBal);

        // Action Buttons inside Balance Card
        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        btnPanel.setOpaque(false);
        JButton btnDeposit = createSolidButton("Deposit", COLOR_ACCENT_TEAL, Color.BLACK);
        JButton btnWithdraw = createOutlinedButton("Withdraw");
        JButton btnTransfer = createOutlinedButton("Transfer");
        
        btnDeposit.addActionListener(e -> depositMoney());
        btnWithdraw.addActionListener(e -> withdrawMoney());
        btnTransfer.addActionListener(e -> transferMoney());

        btnPanel.add(btnDeposit);
        btnPanel.add(btnWithdraw);
        btnPanel.add(btnTransfer);
        
        gbcBal.gridy = 2; gbcBal.insets = new Insets(20, 0, 0, 0);
        balanceCard.add(btnPanel, gbcBal);

        // --- 2. Graph Card ---
        RoundedPanel graphCard = new RoundedPanel(15, COLOR_BG_CARD, COLOR_CARD_BORDER);
        graphCard.setLayout(new BorderLayout());
        graphCard.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel graphHeader = new JLabel("📈 BALANCE TRAJECTORY");
        graphHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        graphHeader.setForeground(COLOR_TEXT_MUTED);
        graphCard.add(graphHeader, BorderLayout.NORTH);

        // Initialize empty chart (Data populated in refreshData)
        chartDataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createLineChart(
                null, null, null, chartDataset, PlotOrientation.VERTICAL, false, true, false);
        
        styleChart(chart);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setOpaque(false);
        chartPanel.setBackground(new Color(0,0,0,0));
        graphCard.add(chartPanel, BorderLayout.CENTER);

        topZone.add(balanceCard);
        topZone.add(graphCard);

        gbcMain.gridy = 0; gbcMain.weightx = 1.0; gbcMain.weighty = 0.4;
        mainWrapper.add(topZone, gbcMain);

        // ==========================================
        // MIDDLE ZONE: Ledger Table
        // ==========================================
        RoundedPanel ledgerCard = new RoundedPanel(15, COLOR_BG_CARD, COLOR_CARD_BORDER);
        ledgerCard.setLayout(new BorderLayout());
        ledgerCard.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel ledgerHeader = new JLabel("🗄 RECENT LEDGER ACTIVITY & REAL-TIME POSTINGS");
        ledgerHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        ledgerHeader.setForeground(COLOR_TEXT_MUTED);
        ledgerHeader.setBorder(new EmptyBorder(0, 0, 15, 0));
        ledgerCard.add(ledgerHeader, BorderLayout.NORTH);

        // Table Setup
        String[] columns = {"DATE / TIMESTAMP", "TYPE", "AMOUNT"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override // Prevent users from editing the cells
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        JTable table = new JTable(tableModel);
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(COLOR_BG_CARD);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        ledgerCard.add(scrollPane, BorderLayout.CENTER);

        gbcMain.gridy = 1; gbcMain.weighty = 0.5;
        mainWrapper.add(ledgerCard, gbcMain);

        // ==========================================
        // BOTTOM ZONE: Session Gateway
        // ==========================================
        RoundedPanel sessionCard = new RoundedPanel(15, COLOR_BG_CARD, COLOR_CARD_BORDER);
        sessionCard.setLayout(new BorderLayout());
        sessionCard.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel sessionInfo = new JLabel("🛡 WORKSTATION SESSION GATEWAY | User: " + account.getUsername());
        sessionInfo.setFont(new Font("Monospaced", Font.BOLD, 12));
        sessionInfo.setForeground(COLOR_TEXT_MUTED);
        
        JPanel exitBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        exitBtns.setOpaque(false);
        JButton btnLogout = createOutlinedButton("Terminate Session");
        JButton btnExit = createOutlinedButton("Exit App");
        btnExit.setForeground(COLOR_NEG); // Make exit red
        
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
    // DATA SYNCING (Graph & Table Math)
    // ==========================================
    private void refreshData() {
        // 1. Update Balance Label
        balanceLabel.setText("₹ " + account.getBalance());

        // 2. Fetch History
        List<String[]> history = bankingService.getTransactionHistory(account.getUsername());
        
        // Clear old data
        tableModel.setRowCount(0);
        chartDataset.clear();

        // 3. Process Data
        BigDecimal runningBalance = BigDecimal.ZERO;
        chartDataset.addValue(runningBalance, "Balance", "Start"); // Base point

        int plotIndex = 1;
        for (String[] row : history) {
            String type = row[0];
            BigDecimal amt = new BigDecimal(row[1]);
            String time = row[2];

            // A. Update Table (Add to top so newest is first)
            String displayAmt = (type.contains("CREDIT") || type.contains("IN")) ? "+₹" + amt : "-₹" + amt;
            tableModel.insertRow(0, new Object[]{time, type, displayAmt});

            // B. Math for Graph: Add or subtract to find historical balances
            if (type.contains("CREDIT") || type.contains("IN")) {
                runningBalance = runningBalance.add(amt);
            } else {
                runningBalance = runningBalance.subtract(amt);
            }
            
            // Add point to chart
            chartDataset.addValue(runningBalance, "Balance", "Tx " + plotIndex++);
        }
    }

    // ==========================================
    // ACTIONS
    // ==========================================
    private void depositMoney() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
        if (input != null) {
            try {
                if (bankingService.deposit(account, new BigDecimal(input))) refreshData();
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Invalid amount."); }
        }
    }

    private void withdrawMoney() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
        if (input != null) {
            try {
                if (bankingService.withdraw(account, new BigDecimal(input))) refreshData();
                else JOptionPane.showMessageDialog(this, "Insufficient funds.");
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Invalid amount."); }
        }
    }

    private void transferMoney() {
        String target = JOptionPane.showInputDialog(this, "Enter recipient's username:");
        if (target == null || target.trim().isEmpty()) return;
        String input = JOptionPane.showInputDialog(this, "Enter amount to transfer:");
        if (input != null) {
            try {
                if (bankingService.transfer(account, target.trim(), new BigDecimal(input))) refreshData();
                else JOptionPane.showMessageDialog(this, "Transfer failed.");
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Invalid amount."); }
        }
    }

    // ==========================================
    // STYLING HELPERS
    // ==========================================
    private void styleChart(JFreeChart chart) {
        chart.setBackgroundPaint(COLOR_BG_CARD);
        
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(COLOR_BG_CARD);
        plot.setDomainGridlinePaint(COLOR_CARD_BORDER);
        plot.setRangeGridlinePaint(COLOR_CARD_BORDER);
        plot.getDomainAxis().setTickLabelPaint(COLOR_TEXT_MUTED);
        plot.getRangeAxis().setTickLabelPaint(COLOR_TEXT_MUTED);

        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, COLOR_ACCENT_TEAL);
        renderer.setSeriesStroke(0, new BasicStroke(3.0f)); // Thicker neon line
        renderer.setSeriesShapesVisible(0, true);
    }

    private void styleTable(JTable table) {
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

        // Custom Cell Renderer to make Credits Green and Debits Red
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, s, f, r, c);
                if (c == 2) { // The Amount Column
                    String val = v.toString();
                    comp.setForeground(val.startsWith("+") ? COLOR_POS : COLOR_NEG);
                } else {
                    comp.setForeground(Color.WHITE);
                }
                return comp;
            }
        };
        table.getColumnModel().getColumn(2).setCellRenderer(renderer);
    }

    private JButton createSolidButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg); btn.setForeground(fg);
        btn.setFocusPainted(false); btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createOutlinedButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(COLOR_BG_CARD); btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(COLOR_CARD_BORDER, 1));
        return btn;
    }

    private void addStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(COLOR_BG_APP);
        statusBar.setBorder(new EmptyBorder(5, 10, 5, 10));
        JLabel statusLeft = new JLabel("● LIVE MAPPING ACTIVE   ·   TLS 1.3");
        statusLeft.setFont(new Font("Monospaced", Font.PLAIN, 11));
        statusLeft.setForeground(COLOR_POS);
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
