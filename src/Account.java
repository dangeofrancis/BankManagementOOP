import java.math.BigDecimal;

public class Account {
    private String username;
    private String password;
    private BigDecimal balance;
    private boolean isBlocked;

    public Account(String username, String password, BigDecimal balance, boolean isBlocked) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.isBlocked = isBlocked;
    }
	//getter method for encapsulation
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public BigDecimal getBalance() { return balance; }
    public boolean isBlocked() { return isBlocked; }

    public void setBlocked(boolean status) { 
        this.isBlocked = status; 
    }

    public boolean deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        this.balance = this.balance.add(amount);
        return true;
    }

    public boolean withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        if (this.balance.compareTo(amount) < 0) return false;
        this.balance = this.balance.subtract(amount);
        return true;
    }
}
