package service;

import database.BankManager;
import model.Account;

import java.math.BigDecimal;

public class BankingService {

    private final BankManager bankManager;

    public BankingService(BankManager bankManager) {
        this.bankManager = bankManager;
    }

    // Register a new user
    public boolean registerUser(String username, String password) {

        if (username == null || username.length() < 6) {
            return false;
        }

        if (password == null || password.isEmpty()) {
            return false;
        }

        return bankManager.registerUser(username, password);
    }

    // User login
    public Account loginUser(String username, String password) {

        Account account =
                bankManager.authenticateUser(username, password);

        if (account == null) {
            return null;
        }

        if (account.isBlocked()) {
            return null;
        }

        return account;
    }

    // Deposit money
    public boolean deposit(Account account, BigDecimal amount) {

        if (account == null) {
            return false;
        }

        if (account.deposit(amount)) {
            bankManager.updateAccount(account);
            return true;
        }

        return false;
    }

    // Withdraw money
    public boolean withdraw(Account account, BigDecimal amount) {

        if (account == null) {
            return false;
        }

        if (account.withdraw(amount)) {
            bankManager.updateAccount(account);
            return true;
        }

        return false;
    }

    // Find user for admin
    public Account findUser(String username) {

        return bankManager.getAccountForAdmin(username);
    }

    // Add funds to a user
    public boolean addFunds(Account account, BigDecimal amount) {

        if (account == null) {
            return false;
        }

        if (account.deposit(amount)) {
            bankManager.updateAccount(account);
            return true;
        }

        return false;
    }

    // Block user
    public boolean blockUser(Account account) {

        if (account == null) {
            return false;
        }

        account.setBlocked(true);
        bankManager.updateAccount(account);

        return true;
    }

    // Unblock user
    public boolean unblockUser(Account account) {

        if (account == null) {
            return false;
        }

        account.setBlocked(false);
        bankManager.updateAccount(account);

        return true;
    }
		// Delete user account
		public boolean deleteUser(String username) {
				
				if (username == null || username.trim().isEmpty()) {
				return false;
				}
		
				return bankManager.deleteUser(username);
		}
		// Transfer money between accounts
		public boolean transfer(Account sender, String receiverUsername, BigDecimal amount) {
				
				if (sender == null || receiverUsername == null || receiverUsername.trim().isEmpty()) {
				return false;
				}
		
				if (sender.getUsername().equals(receiverUsername)) {
				return false;
				}
		
				Account receiver = bankManager.getAccountForAdmin(receiverUsername);
				if (receiver == null) {
				return false; // Receiver does not exist
				}
		
				if (sender.withdraw(amount)) {
				
				if (receiver.deposit(amount)) {
						
						bankManager.updateAccount(sender);
						bankManager.updateAccount(receiver);
						return true;
						
				} else {
						sender.deposit(amount);
						return false;
				}
		}
				
				// Returns false if the sender had insufficient funds or entered a negative amount
				return false; 
		}
}
