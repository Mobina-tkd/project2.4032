package ir.ac.kntu.model;

public class Product {
    private String name;
    private double price;
    private int inventory;

    public Product(String name, double price, int inventory) {
        this.name = name;
        this.price = price;
        this.inventory = inventory;
    }

    public Product() {
    }

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
