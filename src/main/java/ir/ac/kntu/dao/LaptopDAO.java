package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.model.Laptop;


public class LaptopDAO {
    private static final String DB_URL = "jdbc:sqlite:data.db";


    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Laptop ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "name TEXT NOT NULL,"
            + "price REAL NOT NULL,"
            + "inventory INTEGER NOT NULL,"
            + "brand TEXT NOT NULL,"
            + "memory INTEGER NOT NULL,"
            + "RAM INTEGER NOT NULL,"
            + "model TEXT NOT NULL,"
            + "GPU TEXT NOT NULL,"
            + "hasBluetooth BOOLEAN NOT NULL,"
            + "hasWebcam BOOLEAN NOT NULL"
            + ");";
    
        try (Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

    public static boolean insertLaptop(Laptop laptop) {
        String sql = "INSERT INTO laptops(name, price, inventory, brand, memory, RAM, model, GPU, hasBluetooth, hasWebcam )"
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, laptop.getName());
            pstmt.setDouble(2, laptop.getPrice());
            pstmt.setInt(3, laptop.getInventory());
            pstmt.setString(4, laptop.getBrand());
            pstmt.setInt(5, laptop.getMemory());
            pstmt.setInt(6, laptop.getRAM());
            pstmt.setString(7, laptop.getModel());
            pstmt.setString(8, laptop.getGPU());
            pstmt.setBoolean(9, laptop.HasBluetooth());
            pstmt.setBoolean(10, laptop.HasWebcam());

    
            pstmt.executeUpdate();
            System.out.println("laptop inserted successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
    }

    public static Laptop readData() {
        String name = "Laptop";

        System.out.print("Enter price: ");
        double price = ScannerWrapper.getInstance().nextDouble();
        ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter inventory count: ");
        int inventory = ScannerWrapper.getInstance().nextInt();
        ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter brand: ");
        String brand = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter memory (GB): ");
        int memory = ScannerWrapper.getInstance().nextInt();
        ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter RAM (GB): ");
        int ram = ScannerWrapper.getInstance().nextInt();
        ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter model: ");
        String model = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter GPU: ");
        String gpu = ScannerWrapper.getInstance().nextLine();

        System.out.print("Has Bluetooth? (true/false): ");
        boolean hasBluetooth = Boolean.parseBoolean(ScannerWrapper.getInstance().nextLine());

        System.out.print("Has Webcam? (true/false): ");
        boolean hasWebcam = Boolean.parseBoolean(ScannerWrapper.getInstance().nextLine());

        return new Laptop(name, price, inventory, brand, memory, ram, model, gpu, hasBluetooth, hasWebcam);
    }
    
}
