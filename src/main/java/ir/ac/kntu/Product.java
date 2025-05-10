package ir.ac.kntu;

public abstract class Product {
    String name;
    double price;
    int inventory;
    public abstract Product readData();

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getInventory() {
        return inventory;
    }
    
}
