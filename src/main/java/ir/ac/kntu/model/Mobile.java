package ir.ac.kntu.model;

public class Mobile extends Digital {
    private String rearCamRes;
    private String frontCamRes;
    private String netType;

    public Mobile(String name, double price, int inventory, String brand, int memory,
            int ram, String rearCamRes, String frontCamRes, String netType) {
        super(name, price, inventory, brand, memory, ram);
        this.rearCamRes = rearCamRes;
        this.frontCamRes = frontCamRes;
        this.netType = netType;
    }

    public Mobile() {
    }

    public String getRearCamRes() {
        return rearCamRes;
    }

    public String getFrontCamRes() {
        return frontCamRes;
    }

    public String getNetType() {
        return netType;
    }
}
