package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.helper.ConsoleColors;
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
                + "state TEXT NOT NULL,"
                + "street TEXT NOT NULL,"
                + "houseNumber TEXT NOT NULL,"
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
        String sqlInsertAddress = "INSERT INTO addresses(user_id, location, state, street, houseNumber) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement selectStmt = conn.prepareStatement(sqlSelectUserId)) {

            selectStmt.setString(1, user.getEmail());
            int userId = -1;
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                if (resultSet.next()) {
                    userId = resultSet.getInt("id");
                }
            }

            if (userId == -1) {
                System.out.println(ConsoleColors.RED + "User not found with email: " + ConsoleColors.RESET + user.getEmail());
                return false;
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsertAddress)) {
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, address.getLocation());
                insertStmt.setString(3, address.getState());
                insertStmt.setString(4, address.getStreet());
                insertStmt.setString(5, address.getHouseNumber());

                insertStmt.executeUpdate();
                System.out.println(ConsoleColors.GREEN + "Address inserted successfully." + ConsoleColors.RESET);
                return true;
            }

        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Insert failed: " + e.getMessage() + ConsoleColors.RESET);
            return false;
        }
    }

    public static Address findAddress(int addressId) {
        String query = "SELECT * FROM addresses WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement selectStmt = conn.prepareStatement(query)) {

            selectStmt.setInt(1, addressId);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                if (resultSet.next()) {
                    String location = resultSet.getString("location");
                    String state = resultSet.getString("state");
                    String street = resultSet.getString("street");
                    String houseNumber = resultSet.getString("houseNumber");
                    return new Address(location, state, street, houseNumber);
                } else {
                    System.out.println(ConsoleColors.RED +"Address not found with id: "+ ConsoleColors.RESET + addressId );
                    return null;
                }
            }

        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED +"Find failed: " + e.getMessage() + ConsoleColors.RESET);
            return null;
        }
    }

    public static void printAllAddresses(User user) {
        String email = user.getEmail();
        String sqlGetUserId = "SELECT id FROM users WHERE email = ?";
        String sqlGetAddresses = "SELECT id, location, state, street, houseNumber FROM addresses WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement getUserStmt = conn.prepareStatement(sqlGetUserId)) {

            getUserStmt.setString(1, email);
            int userId = -1;
            try (ResultSet userResult = getUserStmt.executeQuery()) {
                if (userResult.next()) {
                    userId = userResult.getInt("id");
                }
            }

            if (userId == -1) {
                System.out.println(ConsoleColors.RED +"No user found with email: " + ConsoleColors.RESET + email);
                return;
            }

            try (PreparedStatement getAddressStmt = conn.prepareStatement(sqlGetAddresses)) {
                getAddressStmt.setInt(1, userId);
                try (ResultSet addressResult = getAddressStmt.executeQuery()) {
                    System.out.println("Addresses for user: " + email);
                    while (addressResult.next()) {
                        int addressId = addressResult.getInt("id");
                        String location = addressResult.getString("location");
                        String state = addressResult.getString("state");
                        String street = addressResult.getString("street");
                        String houseNumber = addressResult.getString("houseNumber");

                        System.out.printf("%d ) Location: %s, State: %s, Street: %s, House Number: %s%n",
                                addressId, location, state, street, houseNumber);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println(ConsoleColors.RED + "Error retrieving addresses: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    public static void deleteAddress(User user, String location) {
        String email = user.getEmail();
        String sqlGetUserId = "SELECT id FROM users WHERE email = ?";
        String sqlDeleteAddress = "DELETE FROM addresses WHERE user_id = ? AND location = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement getUserStmt = conn.prepareStatement(sqlGetUserId)) {

            getUserStmt.setString(1, email);
            int userId = -1;
            try (ResultSet userResult = getUserStmt.executeQuery()) {
                if (userResult.next()) {
                    userId = userResult.getInt("id");
                }
            }

            if (userId == -1) {
                System.out.println(ConsoleColors.RED +"No user found with email: " + ConsoleColors.RESET + email);
                return;
            }

            try (PreparedStatement deleteStmt = conn.prepareStatement(sqlDeleteAddress)) {
                deleteStmt.setInt(1, userId);
                deleteStmt.setString(2, location);

                int affectedRows = deleteStmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println(ConsoleColors.GREEN +"Address with location '" + ConsoleColors.RESET+ location + ConsoleColors.GREEN + "' deleted successfully."+ ConsoleColors.RESET );
                } else {
                    System.out.println("No address found for location: " + location);
                }
            }

        } catch (SQLException e) {
            System.err.println(ConsoleColors.RED + "Error deleting address: " + e.getMessage() + ConsoleColors.RESET);
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
            int userId = -1;
            try (ResultSet userResult = getUserStmt.executeQuery()) {
                if (userResult.next()) {
                    userId = userResult.getInt("id");
                }
            }

            if (userId == -1) {
                System.out.println(ConsoleColors.RED +"No user found with email: "+ ConsoleColors.RESET + email);
                return;
            }

            try (PreparedStatement updateStmt = conn.prepareStatement(sqlUpdateAddress)) {
                updateStmt.setString(1, state);
                updateStmt.setString(2, street);
                updateStmt.setString(3, houseNumber);
                updateStmt.setInt(4, userId);
                updateStmt.setString(5, location);

                int rowsUpdated = updateStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println(ConsoleColors.GREEN +"Address updated successfully."+ ConsoleColors.RESET);
                } else {
                    System.out.println(ConsoleColors.RED +"No address found for the given location."+ ConsoleColors.RESET);
                }
            }

        } catch (SQLException e) {
            System.err.println(ConsoleColors.RED +"Error updating address: " + e.getMessage() + ConsoleColors.RESET);
        }
    }
}
