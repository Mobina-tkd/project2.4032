package ir.ac.kntu.model;

public class ShoppingCart {
    private double price;
    private String information;
    private int sellerId;
    private int productId;
    private String name;

    public ShoppingCart(String name, double price, String informaton, int sellerId, int productId) {
        this.name = name;
        this.price = price;
        this.information = informaton;
        this.sellerId = sellerId;
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getInformation() {
        return information;
    }

    public int getSellerId() {
        return sellerId;
    }

    public int getProcuctId() {
        return productId;
    }

}
