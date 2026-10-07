package service;

import database.BankManager;
import model.Account;

import java.math.BigDecimal;
import java.util.List;

public class BankingService {

    private final BankManager bankManager;

    public BankingService(BankManager bankManager) {
        this.bankManager = bankManager;
    }

    public boolean registerUser(String username, String password) {
        if (username == null || username.length() < 6) return false;
        if (password == null || password.isEmpty()) return false;
        return bankManager.registerUser(username, password);
    }

    public Account loginUser(String username, String password) {
        Account account = bankManager.authenticateUser(username, password);
        if (account == null || account.isBlocked()) return null;
        return account;
    }

    public boolean deposit(Account account, BigDecimal amount) {
        if (account == null) return false;
        if (account.deposit(amount)) {
            bankManager.updateAccount(account);
            bankManager.logTransaction(account.getUsername(), "CREDIT", amount);
            return true;
        }
        return false;
    }

    public boolean withdraw(Account account, BigDecimal amount) {
        if (account == null) return false;
        if (account.withdraw(amount)) {
            bankManager.updateAccount(account);
            bankManager.logTransaction(account.getUsername(), "DEBIT", amount);
            return true;
        }
        return false;
    }

    public boolean transfer(Account sender, String receiverUsername, BigDecimal amount) {
        if (sender == null || receiverUsername == null || receiverUsername.trim().isEmpty()) return false;
        if (sender.getUsername().equals(receiverUsername)) return false;

        Account receiver = bankManager.getAccountForAdmin(receiverUsername);
        if (receiver == null) return false; 

        if (sender.withdraw(amount)) {
            if (receiver.deposit(amount)) {
                bankManager.updateAccount(sender);
                bankManager.updateAccount(receiver);
                
                bankManager.logTransaction(sender.getUsername(), "TRANSFER OUT", amount);
                bankManager.logTransaction(receiverUsername, "TRANSFER IN", amount);
                return true;
            } else {
                sender.deposit(amount);
                return false;
            }
        }
        return false; 
    }

    public Account findUser(String username) {
        return bankManager.getAccountForAdmin(username);
    }

    public boolean addFunds(Account account, BigDecimal amount) {
        if (account == null) return false;
        if (account.deposit(amount)) {
            bankManager.updateAccount(account);
            bankManager.logTransaction(account.getUsername(), "CREDIT (ADMIN)", amount);
            return true;
        }
        return false;
    }

    public boolean blockUser(Account account) {
        if (account == null) return false;
        account.setBlocked(true);
        bankManager.updateAccount(account);
        return true;
    }

    public boolean unblockUser(Account account) {
        if (account == null) return false;
        account.setBlocked(false);
        bankManager.updateAccount(account);
        return true;
    }

    public boolean deleteUser(String username) {
        if (username == null || username.trim().isEmpty()) return false;
        return bankManager.deleteUser(username);
    }
    
    public List<String[]> getTransactionHistory(String username) {
        return bankManager.getTransactionHistory(username);
    }

    // --- NEW PASS-THROUGH METHODS FOR ADMIN ---
    
    public List<String[]> getAllTransactions() {
        return bankManager.getAllTransactions();
    }

    public List<String[]> getAccountRankings() {
        return bankManager.getAccountRankings();
    }
} // <-- This is the final closing bracket that everything MUST be inside of!
