package ir.ac.kntu;

public class Mobile extends Digital{
    String rareCameraResolution;
    String frontCameraResolution;
    String networkInternet;

    public Mobile(String name, double price, int inventory, String brand, int memory,
     int RAM, String rareCameraResolution, String frontCameraResolution, String networkInternet ) {
        this.name = name;
        this.price = price;
        this.inventory = inventory;
        this.brand = brand;
        this.memory = memory;
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
    
    
    public Mobile readData(){
        
        String name ="Mobile";

        System.out.print("Enter price: ");
        double price = ScannerWrapper.getInstance().nextDouble();

        System.out.print("Enter inventory: ");
        int inventory = ScannerWrapper.getInstance().nextInt();
        ScannerWrapper.getInstance().nextLine(); // Consume leftover newline

        System.out.print("Enter brand: ");
        String brand = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter memory (GB): ");
        int memory = ScannerWrapper.getInstance().nextInt();

        System.out.print("Enter RAM (GB): ");
        int RAM = ScannerWrapper.getInstance().nextInt();
        ScannerWrapper.getInstance().nextLine(); // Consume leftover newline

        System.out.print("Enter rare camera resolution: ");
        String rareCameraResolution = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter front camera resolution: ");
        String frontCameraResolution = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter network/internet (e.g., 5G, 4G): ");
        String networkInternet = ScannerWrapper.getInstance().nextLine();

        return new Mobile(name, price, inventory, brand, memory, RAM,
                          rareCameraResolution, frontCameraResolution, networkInternet);
    }

    public static void search() {
        Menu.searchMenu();
        Vendilo.SearchMenu option = Menu.getSearchMenuOption();

        switch (option) {
            case SHOW_ALL-> {
                Utils.showAllProducts("Mobile");
            }


            case SEARCH_BY_PRICE-> {
                System.out.println("Enter the min price: ");
                double min = ScannerWrapper.getInstance().nextDouble();
                System.out.println("Enter the max price: ");
                double max = ScannerWrapper.getInstance().nextDouble();
                Utils.searchByPrice( "Mobile", min, max);
            }




            case UNDEFINED-> {
                System.out.println("Undefined Choice; Try again...\n");
                break;
            }
        }
        
    }

    
    }

