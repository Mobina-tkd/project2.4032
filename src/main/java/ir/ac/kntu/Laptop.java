package ir.ac.kntu;

public class Laptop extends Digital {
    String model;
    String GPU;
    boolean hasBluetooth;
    boolean hasWebcam;

    public Laptop(String name, double price, int inventory, String brand, int memory,
                  int RAM, String model, String GPU, boolean hasBluetooth, boolean hasWebcam) {
        this.name = name;
        this.price = price;
        this.inventory = inventory;
        this.brand = brand;
        this.memory = memory;
        this.RAM = RAM;
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

    public Laptop readData() {
        System.out.print("Enter name: ");
        String name = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter price: ");
        double price = ScannerWrapper.getInstance().nextDouble();

        System.out.print("Enter inventory: ");
        int inventory = ScannerWrapper.getInstance().nextInt();
        ScannerWrapper.getInstance().nextLine(); // Consume leftover newline

        System.out.print("Enter brand: ");
        String brand = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter memory : ");
        int memory = ScannerWrapper.getInstance().nextInt();

        System.out.print("Enter RAM : ");
        int RAM = ScannerWrapper.getInstance().nextInt();
        ScannerWrapper.getInstance().nextLine(); // Consume leftover newline

        System.out.print("Enter model: ");
        String model = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter GPU: ");
        String GPU = ScannerWrapper.getInstance().nextLine();

        System.out.print("Has Bluetooth? (true/false): ");
        boolean hasBluetooth = ScannerWrapper.getInstance().nextBoolean();

        System.out.print("Has Webcam? (true/false): ");
        boolean hasWebcam = ScannerWrapper.getInstance().nextBoolean();

        return new Laptop(name, price, inventory, brand, memory, RAM, model, GPU, hasBluetooth, hasWebcam);
    }

    public static void search() {
        Menu.searchMenu();
        Vendilo.SearchMenu option = Menu.getSearchMenuOption();

        switch (option) {
            case SHOW_ALL-> {
                Utils.showAllProducts("Laptop");
            }


            case SEARCH_BY_PRICE-> {
                System.out.println("Enter the min price: ");
                double min = ScannerWrapper.getInstance().nextDouble();
                System.out.println("Enter the max price: ");
                double max = ScannerWrapper.getInstance().nextDouble();
                Utils.searchByPrice("Laptop", min, max);
            }



            

            case UNDEFINED-> {
                System.out.println("Undefined Choice; Try again...\n");
                break;
            }
        }
        
    }
}
