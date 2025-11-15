package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.helper.ConsoleColors;
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

        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

    public static boolean insertLaptop(Laptop laptop, String agencyCode) {
        String query = "SELECT id FROM sellers WHERE agency_code = ?";
        String sql = "INSERT INTO Laptop(seller_id, name, price, inventory, brand, memory, RAM, model, GPU, hasBluetooth, hasWebcam) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement selectStmt = conn.prepareStatement(query)
        ) {
            selectStmt.setString(1, agencyCode);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                if (resultSet.next()) {
                    int sellerId = resultSet.getInt("id");
    
                    try (PreparedStatement insertStmt = conn.prepareStatement(sql)) {
                        insertStmt.setInt(1, sellerId);
                        insertStmt.setString(2, laptop.getName());
                        insertStmt.setDouble(3, laptop.getPrice());
                        insertStmt.setInt(4, laptop.getInventory());
                        insertStmt.setString(5, laptop.getBrand());
                        insertStmt.setInt(6, laptop.getMemory());
                        insertStmt.setInt(7, laptop.getRam());
                        insertStmt.setString(8, laptop.getModel());
                        insertStmt.setString(9, laptop.getGpu());
                        insertStmt.setBoolean(10, laptop.hasBluetooth());
                        insertStmt.setBoolean(11, laptop.hasWebcam());
    
                        insertStmt.executeUpdate();
                        System.out.println(ConsoleColors.GREEN +"Laptop inserted successfully." + ConsoleColors.RESET);
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED +"Insert failed: " + e.getMessage() + ConsoleColors.RESET);
        }
    
        return false;
    }
    
}
