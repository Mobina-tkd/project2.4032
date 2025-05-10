package ir.ac.kntu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Address {

    private static final String DB_URL = "jdbc:sqlite:data.db";

    private String location;
    private String state;
    private String street;
    private String houseNumber;

    
    public Address(String location, String state, String street, String houseNumber) {
        this.location = location;
        this.state = state;
        this.street = street;
        this.houseNumber = houseNumber;
    }

    public String getLocation() {
        return location;
    }
    public String getState() {
        return state;
    }
    public String getStreet() {
        return street;
    }
    public String getHouseNumber() {
        return houseNumber;
    }

    public static void handleAddress(User user) {

    while(true) {
        Menu.AddressMenue();
        Vendilo.AddressOption addressOption = Menu.getAddressOption();
        switch (addressOption) {
            case INSERT_NEW_ADDRESS -> {
                while(true) {
                    Address address = Utils.readAddressFromUser();
                    boolean inserted = AddressDAO.insertAddress(address, user);
                    if(!inserted) {
                        continue;
                    }
                    break;
                }
            }
            case VIEW_ADDRESSES -> {
                printAllAddresses(user);
            }
            case EDIT_ADDRESSES -> {
                System.out.print("Enter the location you want to edit : ");
                String location = ScannerWrapper.getInstance().nextLine();
                editAddress(user, location);
                   
            }
            case DELETE_ADDRESSES -> {
                System.out.print("Enter the location you want to delete : ");
                String location = ScannerWrapper.getInstance().nextLine();
                deleteAddress(user, location);
            }
            case UNDEFINED -> {
                System.out.println("Undefined Choice; Try again...\\n");
                
            }
        }
    }




    }

    public static void printAllAddresses(User user) {
    String email = user.getEmail();
    String sqlGetUserId = "SELECT id FROM users WHERE email = ?";
    String sqlGetAddresses = "SELECT location, state, street, houseNumber FROM addresses WHERE user_id = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement getUserStmt = conn.prepareStatement(sqlGetUserId)) {

        // Step 1: Get user ID by email
        getUserStmt.setString(1, email);
        ResultSet userResult = getUserStmt.executeQuery();

        if (userResult.next()) {
            int userId = userResult.getInt("id");

            // Step 2: Get addresses using user ID
            try (PreparedStatement getAddressStmt = conn.prepareStatement(sqlGetAddresses)) {
                getAddressStmt.setInt(1, userId);
                ResultSet addressResult = getAddressStmt.executeQuery();

                System.out.println("Addresses for user: " + email);
                int i =1;
                while (addressResult.next()) {
                    String location = addressResult.getString("location");
                    String state = addressResult.getString("state");
                    String street = addressResult.getString("street");
                    String houseNumber = addressResult.getString("houseNumber");

                    System.out.printf("%d ) Location: %s, State: %s, Street: %s, House Number: %s%n",
                            i, location, state, street, houseNumber);
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
        
                // Step 1: Get user ID by email
                getUserStmt.setString(1, email);
                ResultSet userResult = getUserStmt.executeQuery();
        
                if (userResult.next()) {
                    int userId = userResult.getInt("id");
        
                    // Step 2: Delete address with matching location for the user
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
