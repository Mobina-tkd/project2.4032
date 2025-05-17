package ir.ac.kntu.model;


public class Seller {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    private String firstName;
    private String lastName;
    private String IDNumber;
    private String storeName;
    private String state;
    private String phoneNumber;
    private String password;
    private boolean identityVerified = false;
    private String agencyCode;
    private SellerWallet wallet = new SellerWallet();

    

    public Seller(String firstName, String lastName, String IDNumber, String storeName,
        String state, String phoneNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.IDNumber = IDNumber;
        this.storeName = storeName;
        this.state = state;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getIDNumber() {
        return IDNumber;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getState() {
        return state;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public double getWalletBalance() {
        return wallet.getWalletBalance();
    }

    public void addBalance(double balance) {
        wallet.addBalance(balance);
    }

    public boolean  withdraw(double balance) {
        return wallet.withdraw(balance);
    }

    public boolean isIdentityVerified() {
        return identityVerified;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

}
