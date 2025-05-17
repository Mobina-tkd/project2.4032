package ir.ac.kntu.model;

public class ShoppingCart {
    private double  price;
    private String  information;

    public ShoppingCart(double price, String informaton) {
        this.price = price;
        this.information = informaton;
    }

    public double  getPrice() {
        return price;
    }

    public String  getInformation() {
        return information;
    }
    
}
