package ir.ac.kntu.model;

public class UserWallet {
    private double balance;

    public UserWallet() {
        this.balance = 0;
    }

    public double getBalance() {
        return balance;
    }

    public void charge(double balance) {
        this.balance += balance;
    }

    public boolean purchase(double balance) {
        if (this.balance < balance) {
            return false;
        }
        this.balance -= balance;
        return true;
    }

}
