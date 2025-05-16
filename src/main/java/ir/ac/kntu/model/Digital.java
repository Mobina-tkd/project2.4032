package ir.ac.kntu.model;

public  abstract  class Digital extends Product {
    String brand;
    int memory;
    int RAM;  

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
