package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.helper.ScannerWrapper;
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
    

    public static void printAllAddresses(User user) {
    String email = user.getEmail();
    String sqlGetUserId = "SELECT id FROM users WHERE email = ?";
    String sqlGetAddresses = "SELECT location, state, street, houseNumber FROM addresses WHERE user_id = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement getUserStmt = conn.prepareStatement(sqlGetUserId)) {

        getUserStmt.setString(1, email);
        ResultSet userResult = getUserStmt.executeQuery();

        if (userResult.next()) {
            int userId = userResult.getInt("id");

            try (PreparedStatement getAddressStmt = conn.prepareStatement(sqlGetAddresses)) {
                getAddressStmt.setInt(1, userId);
                ResultSet addressResult = getAddressStmt.executeQuery();

                System.out.println("Addresses for user: " + email);
                while (addressResult.next()) {
                    int id = addressResult.getInt("id");
                    String location = addressResult.getString("location");
                    String state = addressResult.getString("state");
                    String street = addressResult.getString("street");
                    String houseNumber = addressResult.getString("houseNumber");

                    System.out.printf("%d ) Location: %s, State: %s, Street: %s, House Number: %s%n",
                            id, location, state, street, houseNumber);
                }
            }
        } else {
            System.out.println("No user found with email: " + email);
        }

        } catch (SQLException e) {
            System.err.println("Error retrieving addresses: " + e.getMessage());
        }
    }

        public static void deleteAddress(User user, String location) {
            String email = user.getEmail();
            String sqlGetUserId = "SELECT id FROM users WHERE email = ?";
            String sqlDeleteAddress = "DELETE FROM addresses WHERE user_id = ? AND location = ?";
        
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement getUserStmt = conn.prepareStatement(sqlGetUserId)) {
        
                getUserStmt.setString(1, email);
                ResultSet userResult = getUserStmt.executeQuery();
        
                if (userResult.next()) {
                    int userId = userResult.getInt("id");
        
                    try (PreparedStatement deleteStmt = conn.prepareStatement(sqlDeleteAddress)) {
                        deleteStmt.setInt(1, userId);
                        deleteStmt.setString(2, location);
        
                        int affectedRows = deleteStmt.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Address with location '" + location + "' deleted successfully.");
                        } else {
                            System.out.println("No address found for location: " + location);
                        }
                    }
                } else {
                    System.out.println("No user found with email: " + email);
                }
        
            } catch (SQLException e) {
                System.err.println("Error deleting address: " + e.getMessage());
            }
        }    

        public static void editAddress(User user, String location) {
            System.out.print("Enter the state: ");
            String state = ScannerWrapper.getInstance().nextLine();
        
            System.out.print("Enter the street: ");
            String street = ScannerWrapper.getInstance().nextLine();
        
            System.out.print("Enter the house number: ");
            String houseNumber = ScannerWrapper.getInstance().nextLine();
        
            String email = user.getEmail();
            String sqlGetUserId = "SELECT id FROM users WHERE email = ?";
            String sqlUpdateAddress = "UPDATE addresses SET state = ?, street = ?, houseNumber = ? WHERE user_id = ? AND location = ?";
        
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement getUserStmt = conn.prepareStatement(sqlGetUserId)) {
        
                getUserStmt.setString(1, email);
                ResultSet userResult = getUserStmt.executeQuery();
        
                if (userResult.next()) {
                    int userId = userResult.getInt("id");
                            try (PreparedStatement updateStmt = conn.prepareStatement(sqlUpdateAddress)) {
                        updateStmt.setString(1, state);
                        updateStmt.setString(2, street);
                        updateStmt.setString(3, houseNumber);
                        updateStmt.setInt(4, userId);
                        updateStmt.setString(5, location);
        
                        int rowsUpdated = updateStmt.executeUpdate();
                        if (rowsUpdated > 0) {
                            System.out.println("Address updated successfully.");
                        } else {
                            System.out.println("No address found for the given location.");
                        }
                    }
        
                } else {
                    System.out.println("No user found with email: " + email);
                }
        
            } catch (SQLException e) {
                System.err.println("Error updating address: " + e.getMessage());
            }
        }


    
}
