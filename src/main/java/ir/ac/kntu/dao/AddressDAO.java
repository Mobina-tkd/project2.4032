package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.model.Address;
import ir.ac.kntu.model.User;



public class AddressDAO {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS addresses ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "user_id INTEGER NOT NULL,"
                        + "location TEXT NOT NULL,"
                        + "state TEXT NOT NULL ,"
                        + "street TEXT NOT NULL ,"
                        + "houseNumber TEXT NOT NULL ,"  
                        + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
                        + ");";
                        
    try (Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

    public static boolean insertAddress(Address address, User user) {
        String sqlSelectUserId = "SELECT id FROM users WHERE email = ?";
        String sqlInsertAddress = "INSERT INTO addresses(user_id, location, state, street, houseNumber) "
                                + "VALUES (?, ?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement selectStmt = conn.prepareStatement(sqlSelectUserId)) {
    
            // Get user ID from email
            selectStmt.setString(1, user.getEmail());
            var rs = selectStmt.executeQuery();
    
            if (rs.next()) {
                int userId = rs.getInt("id");
    
                try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsertAddress)) {
                    insertStmt.setInt(1, userId);
                    insertStmt.setString(2, address.getLocation());
                    insertStmt.setString(3, address.getState());
                    insertStmt.setString(4, address.getStreet());
                    insertStmt.setString(5, address.getHouseNumber());
    
                    insertStmt.executeUpdate();
                    System.out.println("Address inserted successfully.");
                    return true;
                }
    
            } else {
                System.out.println("User not found with email: " + user.getEmail());
                return false;
            }
    
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
    }

    public static String findState(int id) {
        String query = "SELECT state FROM addresses WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement selectStmt = conn.prepareStatement(query)) {
    
            var rs = selectStmt.executeQuery();
            selectStmt.setInt(1,id);
    
            if (rs.next()) {
                String state = rs.getString("state");
                return state;
    
            } else {
                System.out.println("Address not found with id: " + id);
                return "";
            }
    
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return "";
        }



    }
    


    
}
