package ir.ac.kntu.model;

public abstract class Digital extends Product {
    private String brand;
    private int memory;
    private int ram;

    public Digital(String name, double price, int inventory, String brand, int memory, int ram) {
        super(name, price, inventory);
        this.brand = brand;
        this.memory = memory;
        this.ram = ram;
    }

    public Digital() {
    }

    public String getBrand() {
        return brand;
    }

    public int getMemory() {
        return memory;
    }

    public int getRam() {
        return ram;
    }
}
