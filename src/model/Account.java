package model;

import java.math.BigDecimal;

public class Account {

    private String username;
    private String password;
    private BigDecimal balance;
    private boolean blocked;

    public Account(String username, String password,
                   BigDecimal balance, boolean blocked) {

        this.username = username;
        this.password = password;
        this.balance = balance;
        this.blocked = blocked;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean isBlocked() {
        return blocked;
    }

    // Block / Unblock account
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    // Deposit money
    public boolean deposit(BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        balance = balance.add(amount);
        return true;
    }

    // Withdraw money
    public boolean withdraw(BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        if (balance.compareTo(amount) < 0) {
            return false;
        }

        balance = balance.subtract(amount);
        return true;
    }
}