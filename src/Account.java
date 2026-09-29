import java.math.BigDecimal;

public class Account {
    private String password;
    private BigDecimal balance;
    private boolean isBlocked;

    public Account(String password) {
        this.password = password;
        this.balance = new BigDecimal("0.00");
        this.isBlocked = false;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean status) {
        this.isBlocked = status;
    }

    public boolean deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Invalid deposit amount.");
            return false;
        }
        this.balance = this.balance.add(amount);
        return true;
    }

    public boolean withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Invalid withdrawal amount.");
            return false;
        }
        
        if (this.balance.compareTo(amount) < 0) {
            System.out.println("Transaction failed: Insufficient funds.");
            return false;
        }

        this.balance = this.balance.subtract(amount);
        return true;
    }
}
