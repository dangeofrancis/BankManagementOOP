import java.sql.*;
import java.math.BigDecimal;

public class BankManager {
    private final String url = "jdbc:sqlite:bank.db";

public BankManager() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("CRITICAL ERROR: The sqlite-jdbc.jar file is missing or corrupted!");
        }

        String sql = "CREATE TABLE IF NOT EXISTS accounts ("
                   + "username TEXT PRIMARY KEY,"
                   + "password TEXT,"
                   + "balance TEXT,"
                   + "isBlocked INTEGER"
                   + ");";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Database initialization failed: " + e.getMessage());
        }
    }

    public boolean registerUser(String username, String password) {
        String sql = "INSERT INTO accounts(username, password, balance, isBlocked) VALUES(?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, "0.00");
            pstmt.setInt(4, 0); 
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("CRITICAL DB ERROR: " + e.getMessage());
            return false; 
        }
    }

    public Account authenticateUser(String username, String password) {
        String sql = "SELECT * FROM accounts WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) { 
                String dbPass = rs.getString("password");
                if (dbPass.equals(password)) { // Check password
                    BigDecimal bal = new BigDecimal(rs.getString("balance"));
                    boolean isBlocked = rs.getInt("isBlocked") == 1;
                    return new Account(username, dbPass, bal, isBlocked);
                }
            }
        } catch (SQLException e) {
            System.out.println("Auth Error: " + e.getMessage());
        }
        return null;
    }

    public Account getAccountForAdmin(String username) {
        String sql = "SELECT * FROM accounts WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String dbPass = rs.getString("password");
                BigDecimal bal = new BigDecimal(rs.getString("balance"));
                boolean isBlocked = rs.getInt("isBlocked") == 1;
                return new Account(username, dbPass, bal, isBlocked);
            }
        } catch (SQLException e) {
            System.out.println("Lookup Error: " + e.getMessage());
        }
        return null;
    }
    
    public void updateAccount(Account acc) {
        String sql = "UPDATE accounts SET balance = ?, isBlocked = ? WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, acc.getBalance().toString());
            pstmt.setInt(2, acc.isBlocked() ? 1 : 0);
            pstmt.setString(3, acc.getUsername());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Sync Error: " + e.getMessage());
        }
    }
}
