package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.helper.ConsoleColors;

public class InformProductDAO {

    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS inform_user ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "product_id INTEGER NOT NULL,"
                + "product_name TEXT NOT NULL,"
                + "email TEXT NOT NULL,"
                + "FOREIGN KEY (email) REFERENCES users(email) ON DELETE CASCADE"
                + ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Table created or already exists.");

        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

    public static void insertInformUser(int productId, String productName, String email) {
        String query1 = "INSERT INTO inform_user(product_id, product_name, email) "
                + "VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement insertStmt = conn.prepareStatement(query1)) {

            insertStmt.setInt(1, productId);
            insertStmt.setString(2, productName);
            insertStmt.setString(3, email);

            insertStmt.executeUpdate();
            System.out.println(
                    ConsoleColors.GREEN + "We will inform you when this product is available" + ConsoleColors.RESET);
        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Insert failed: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    public static void sendInventoryNotifToUsers(String productName, int productId) {
        String query = "SELECT email FROM inform_user WHERE product_id = ? AND product_name = ?";
    
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setInt(1, productId);
            stmt.setString(2, productName);
    
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    String email = resultSet.getString("email");
                    NotificationDAO.insertNotification(email, "Product is available",
                            productName + " " + productId);
                }
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
