package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.model.Mobile;


public class MobileDAO {
    private static final String DB_URL = "jdbc:sqlite:data.db";


    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Mobile ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "seller_id INTEGER NOT NULL,"
            + "name TEXT NOT NULL,"
            + "price REAL NOT NULL,"
            + "inventory INTEGER NOT NULL,"
            + "brand TEXT NOT NULL,"
            + "memory INTEGER NOT NULL,"
            + "RAM INTEGER NOT NULL,"
            + "rareCameraResolution TEXT,"
            + "frontCameraResolution TEXT,"
            + "networkInternet TEXT,"
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

    public static boolean insertMobile(Mobile mobile, String agencyCode) {
        if (mobile == null || agencyCode == null || agencyCode.isBlank()) {
            System.out.println("Invalid input: mobile or agencyCode is null.");
            return false;
        }

        String query = "SELECT id FROM sellers WHERE agency_code = ?";
        String sql = """
            INSERT INTO Mobile(
                seller_id, name, price, inventory, brand, memory, RAM, 
                rareCameraResolution, frontCameraResolution, networkInternet
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, agencyCode);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                System.out.println("No seller found for agency code: " + agencyCode);
                return false;
            }

            int sellerId = rs.getInt("id");

            try (PreparedStatement insertStmt = conn.prepareStatement(sql)) {
                insertStmt.setInt(1, sellerId);
                insertStmt.setString(2, mobile.getName());
                insertStmt.setDouble(3, mobile.getPrice());
                insertStmt.setInt(4, mobile.getInventory());
                insertStmt.setString(5, mobile.getBrand());
                insertStmt.setInt(6, mobile.getMemory());
                insertStmt.setInt(7, mobile.getRAM());
                insertStmt.setString(8, mobile.getRareCameraResolution());
                insertStmt.setString(9, mobile.getFrontCameraResolution());
                insertStmt.setString(10, mobile.getNetworkInternet());

                insertStmt.executeUpdate();
                System.out.println("Mobile inserted successfully.");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            e.printStackTrace();  // better for debugging
            return false;
        }
    }

}
