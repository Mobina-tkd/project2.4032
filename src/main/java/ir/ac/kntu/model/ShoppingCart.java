package ir.ac.kntu.model;

public class ShoppingCart {
    private double  price;
    private String  information;
    private int sellerId;

    public ShoppingCart(double price, String informaton, int sellerId) {
        this.price = price;
        this.information = informaton;
        this.sellerId = sellerId;
    }

    public double  getPrice() {
        return price;
    }

    public String  getInformation() {
        return information;
    }

    public int getSellerId() {
        return sellerId;
    }
    
}
