package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.model.Laptop;


public class LaptopDAO {
    private static final String DB_URL = "jdbc:sqlite:data.db";


    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Laptop ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "seller_id INTEGER NOT NULL,"
            + "name TEXT NOT NULL,"
            + "price REAL NOT NULL,"
            + "inventory INTEGER NOT NULL,"
            + "brand TEXT NOT NULL,"
            + "memory INTEGER NOT NULL,"
            + "RAM INTEGER NOT NULL,"
            + "model TEXT NOT NULL,"
            + "GPU TEXT NOT NULL,"
            + "hasBluetooth BOOLEAN NOT NULL,"
            + "hasWebcam BOOLEAN NOT NULL,"
            + "FOREIGN KEY (seller_id) REFERENCES sellers(id) ON DELETE CASCADE"
            + ");";
    
        try (Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

    public static boolean insertLaptop(Laptop laptop, String agencyCode) {
        String query = "SELECT id FROM sellers WHERE agency_code = ?";
        String sql = "INSERT INTO laptops(seller_id, name, price, inventory, brand, memory, RAM, model, GPU, hasBluetooth, hasWebcam )"
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, agencyCode);
                var rs = pstmt.executeQuery();
        
                if (rs.next()) {
                    int sellerId = rs.getInt("id");
        
                try (PreparedStatement insertStmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, sellerId);
                    pstmt.setString(2, laptop.getName());
                    pstmt.setDouble(3, laptop.getPrice());
                    pstmt.setInt(4, laptop.getInventory());
                    pstmt.setString(5, laptop.getBrand());
                    pstmt.setInt(6, laptop.getMemory());
                    pstmt.setInt(7, laptop.getRAM());
                    pstmt.setString(8, laptop.getModel());
                    pstmt.setString(9, laptop.getGPU());
                    pstmt.setBoolean(10, laptop.HasBluetooth());
                    pstmt.setBoolean(11, laptop.HasWebcam());
                    insertStmt.executeUpdate();
                    System.out.println("mobile inserted successfully.");
                    return true;
                    } 
                }else {
                    return false;
                }

            }catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
    }
    
}
