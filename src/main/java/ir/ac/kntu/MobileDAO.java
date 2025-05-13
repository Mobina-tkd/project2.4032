package ir.ac.kntu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class MobileDAO {
    private static final String DB_URL = "jdbc:sqlite:data.db";


    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Mobile ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "name TEXT NOT NULL,"
            + "price REAL NOT NULL,"
            + "inventory INTEGER NOT NULL,"
            + "brand TEXT NOT NULL,"
            + "memory INTEGER NOT NULL,"
            + "RAM INTEGER NOT NULL,"
            + "rareCameraResolution TEXT,"
            + "frontCameraResolution TEXT,"
            + "networkInternet TEXT"
            + ");";
    
        try (Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

    public static boolean insertMobile(Mobile mobile) {
        String sql = "INSERT INTO mobiles(name, price, inventory, brand, memory, RAM, rareCameraResolution, frontCameraResolution, networkInternet)"
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, mobile.getName());
            pstmt.setDouble(2, mobile.getPrice());
            pstmt.setInt(3, mobile.getInventory());
            pstmt.setString(4, mobile.getBrand());
            pstmt.setInt(5, mobile.getMemory());
            pstmt.setInt(6, mobile.getRAM());
            pstmt.setString(7, mobile.getRareCameraResolution());
            pstmt.setString(8, mobile.getFrontCameraResolution());
            pstmt.setString(9, mobile.getNetworkInternet());

    
            pstmt.executeUpdate();
            System.out.println("mobile inserted successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
    
    }
}
