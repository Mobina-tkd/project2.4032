package ir.ac.kntu.model;

public class SellerWallet {
    private double walletBalance = 0;

    public SellerWallet() {

    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void addBalance(double balance) {
        walletBalance += balance;
    }

    public boolean withdraw(double balance) {
        if (walletBalance >= balance) {
            walletBalance -= balance;
            return true;
        }
        return false;
    }

}
