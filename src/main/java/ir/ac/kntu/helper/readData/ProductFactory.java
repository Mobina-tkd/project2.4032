package ir.ac.kntu.helper.readData;

import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.model.Book;
import ir.ac.kntu.model.Laptop;
import ir.ac.kntu.model.Mobile;

public class ProductFactory {

    public static Book readBookData() {
        String name = "Book";

        System.out.print("Enter title: ");
        String title = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter price: ");
        double price = ScannerWrapper.getInstance().nextDouble();

        System.out.print("Enter inventory: ");
        int inventory = ScannerWrapper.getInstance().nextInt();
        ScannerWrapper.getInstance().nextLine(); 
        System.out.print("Enter writer name: ");
        String writerName = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter page number: ");
        int pageNumber = ScannerWrapper.getInstance().nextInt();
        ScannerWrapper.getInstance().nextLine(); 

        System.out.print("Enter genre: ");
        String genre = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter age group (ADULT, TEENAGER, CHILDREN): ");
        String ageGroup = ScannerWrapper.getInstance().nextLine();
        System.out.print("Enter ISBN: ");
        String ISBN = ScannerWrapper.getInstance().nextLine();

        return new Book(name, title, price, inventory, writerName, pageNumber, genre, ageGroup, ISBN);
    }

    public static Mobile readMobileData(){
        
        String name ="Mobile";

        System.out.print("Enter price: ");
        double price = ScannerWrapper.getInstance().nextDouble();

        System.out.print("Enter inventory: ");
        int inventory = ScannerWrapper.getInstance().nextInt();
        ScannerWrapper.getInstance().nextLine(); 

        System.out.print("Enter brand: ");
        String brand = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter memory (GB): ");
        int memory = ScannerWrapper.getInstance().nextInt();

        System.out.print("Enter RAM (GB): ");
        int RAM = ScannerWrapper.getInstance().nextInt();
        ScannerWrapper.getInstance().nextLine(); 

        System.out.print("Enter rare camera resolution: ");
        String rareCameraResolution = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter front camera resolution: ");
        String frontCameraResolution = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter network/internet (e.g., 5G, 4G): ");
        String networkInternet = ScannerWrapper.getInstance().nextLine();

        return new Mobile(name, price, inventory, brand, memory, RAM,
                          rareCameraResolution, frontCameraResolution, networkInternet);
    }

    public static Laptop readLaptopData() {
        String name = "Laptop";
        System.out.print("Enter price: ");
        double price = ScannerWrapper.getInstance().nextDouble();

        System.out.print("Enter inventory: ");
        int inventory = ScannerWrapper.getInstance().nextInt();
        ScannerWrapper.getInstance().nextLine(); 

        System.out.print("Enter brand: ");
        String brand = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter memory : ");
        int memory = ScannerWrapper.getInstance().nextInt();

        System.out.print("Enter RAM : ");
        int RAM = ScannerWrapper.getInstance().nextInt();
        ScannerWrapper.getInstance().nextLine(); 

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
    
}
