package database;

import model.Account;

import java.math.BigDecimal;
import java.sql.*;

public class BankManager {

    private final String url = "jdbc:sqlite:bank.db";

    public BankManager() {

        // Load SQLite JDBC driver
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found.");
        }

        // Create accounts table
        String sql = """
                CREATE TABLE IF NOT EXISTS accounts (
                    username TEXT PRIMARY KEY,
                    password TEXT,
                    balance TEXT,
                    isBlocked INTEGER
                );
                """;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

        } catch (SQLException e) {
            System.out.println("Database initialization failed: "
                    + e.getMessage());
        }
    }

    // Register a new user
    public boolean registerUser(String username, String password) {

        String sql = """
                INSERT INTO accounts
                (username, password, balance, isBlocked)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, "0.00");
            pstmt.setInt(4, 0);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Registration failed: "
                    + e.getMessage());
            return false;
        }
    }

    // Authenticate user
    public Account authenticateUser(String username, String password) {

        String sql =
                "SELECT * FROM accounts WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                String dbPassword =
                        rs.getString("password");

                if (dbPassword.equals(password)) {

                    BigDecimal balance =
                            new BigDecimal(rs.getString("balance"));

                    boolean blocked =
                            rs.getInt("isBlocked") == 1;

                    return new Account(
                            username,
                            dbPassword,
                            balance,
                            blocked
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Authentication error: "
                    + e.getMessage());
        }

        return null;
    }

    // Get account for administrator
    public Account getAccountForAdmin(String username) {

        String sql =
                "SELECT * FROM accounts WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                String password =
                        rs.getString("password");

                BigDecimal balance =
                        new BigDecimal(rs.getString("balance"));

                boolean blocked =
                        rs.getInt("isBlocked") == 1;

                return new Account(
                        username,
                        password,
                        balance,
                        blocked
                );
            }

        } catch (SQLException e) {
            System.out.println("Account lookup error: "
                    + e.getMessage());
        }

        return null;
    }

    // Save account changes to database
    public void updateAccount(Account account) {

        String sql = """
                UPDATE accounts
                SET balance = ?, isBlocked = ?
                WHERE username = ?
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1,
                    account.getBalance().toString());

            pstmt.setInt(2,
                    account.isBlocked() ? 1 : 0);

            pstmt.setString(3,
                    account.getUsername());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Account update failed: "
                    + e.getMessage());
        }
    }
		// Delete user account
		public boolean deleteUser(String username) {
				String sql = "DELETE FROM accounts WHERE username = ?";
		
				try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
		
				pstmt.setString(1, username);
				
				// executeUpdate returns the number of rows changed. 
				// If it returns > 0, the user was successfully deleted.
				int rowsAffected = pstmt.executeUpdate();
				return rowsAffected > 0;
		
				} catch (SQLException e) {
				System.out.println("Deletion error: " + e.getMessage());
				return false;
				}
		}
	}
