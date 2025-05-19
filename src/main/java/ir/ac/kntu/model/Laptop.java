package ir.ac.kntu.model;

public class Laptop extends Digital {
    String model;
    String GPU;
    boolean hasBluetooth;
    boolean hasWebcam;

    public Laptop(String name, double price, int inventory, String brand, int memory,
                  int RAM, String model, String GPU, boolean hasBluetooth, boolean hasWebcam) {
        super(name, price, inventory, brand,  memory,  RAM);
        this.price = price;
        this.model = model;
        this.GPU = GPU;
        this.hasBluetooth = hasBluetooth;
        this.hasWebcam = hasWebcam;
    }

    public Laptop() {}

    public String getModel() {
    return model;
    }

    public String getGPU() {
        return GPU;
    }

    public boolean HasBluetooth() {
        return hasBluetooth;
    }

    public boolean HasWebcam() {
        return hasWebcam;
    }

}
