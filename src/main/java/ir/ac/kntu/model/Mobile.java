package ir.ac.kntu.model;

public class Mobile extends Digital{
    String rareCameraResolution;
    String frontCameraResolution;
    String networkInternet;

    public Mobile(String name, double price, int inventory, String brand, int memory,
     int RAM, String rareCameraResolution, String frontCameraResolution, String networkInternet ) {
        super(name, price, inventory, brand,  memory,  RAM);
        this.RAM = RAM;
        this.rareCameraResolution = rareCameraResolution;
        this.frontCameraResolution = frontCameraResolution;
        this.networkInternet = networkInternet;
    }

    public Mobile(){}

    public String getRareCameraResolution() {
    return rareCameraResolution;
    }

    public String getFrontCameraResolution() {
        return frontCameraResolution;
    }

    public String getNetworkInternet() {
        return networkInternet;
    }
}

