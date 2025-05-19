package ir.ac.kntu.model;

public  abstract  class Digital extends Product {
    String brand;
    int memory;
    int RAM;  

    public Digital(String name, double price, int inventory,String brand, int memory, int RAM) {
        super(name, price, inventory);
        this.brand = brand;
        this.memory = memory;
        this.RAM = RAM;
    }

    public Digital() {}

    public String getBrand() {
        return brand;
    }
    
    public int getMemory() {
        return memory;
    }
    
    public int getRAM() {
        return RAM;
    }
}
