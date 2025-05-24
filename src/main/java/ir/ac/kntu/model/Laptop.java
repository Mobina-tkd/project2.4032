package ir.ac.kntu.model;

public class Laptop extends Digital {
    private String model;
    private String gpu;
    private boolean hasBluetooth;
    private boolean hasWebcam;

    public Laptop(String name, double price, int inventory, String brand, int memory,
            int ram, String model, String gpu, boolean hasBluetooth, boolean hasWebcam) {
        super(name, price, inventory, brand, memory, ram);
        this.model = model;
        this.gpu = gpu;
        this.hasBluetooth = hasBluetooth;
        this.hasWebcam = hasWebcam;
    }

    public Laptop() {
    }

    public String getModel() {
        return model;
    }

    public String getGpu() {
        return gpu;
    }

    public boolean hasBluetooth() {
        return hasBluetooth;
    }

    public boolean hasWebcam() {
        return hasWebcam;
    }

}
