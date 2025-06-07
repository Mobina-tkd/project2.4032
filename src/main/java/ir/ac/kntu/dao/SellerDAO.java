package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.model.Seller;
import ir.ac.kntu.model.User;

public class SellerDAO {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS sellers ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "agency_code TEXT UNIQUE,"
                + "first_name TEXT,"
                + "last_name TEXT,"
                + "ID_Number TEXT UNIQUE,"
                + "store_name TEXT UNIQUE,"
                + "state TEXT,"
                + "phone_number TEXT UNIQUE,"
                + "password TEXT NOT NULL,"
                + "identity_verified INTEGER," // fixed typo here
                + "wallet_balance REAL DEFAULT 0,"
                + "message TEXT"
                + ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

    public static Boolean insertSeller(Seller seller) {
        String sql = "INSERT INTO sellers(agency_code, first_name, last_name, ID_Number, store_name, state, phone_number, password, identity_verified, message, wallet_balance) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, seller.getAgencyCode());
            pstmt.setString(2, seller.getFirstName());
            pstmt.setString(3, seller.getLastName());
            pstmt.setString(4, seller.getidNumber());
            pstmt.setString(5, seller.getStoreName());
            pstmt.setString(6, seller.getState());
            pstmt.setString(7, seller.getPhoneNumber());
            pstmt.setString(8, seller.getPassword());
            pstmt.setInt(9, seller.isIdentityVerified() ? 1 : 0);
            pstmt.setString(10, "");
            pstmt.setDouble(11, 0); // initialize wallet_balance to 0

            pstmt.executeUpdate();
            System.out.println("Seller inserted successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
    }

    public static void setMessage(String message, String agencyCode) {
        String sql = "UPDATE sellers SET message = ? WHERE agency_code = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, message);
            stmt.setString(2, agencyCode);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Seller data updated successfully.");
            } else {
                System.out.println("No update was made.");
            }

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public static void setIdentityVerified(int value, String agencyCode) {
        String sql = "UPDATE sellers SET identity_verified = ? WHERE agency_code = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, value);
            stmt.setString(2, agencyCode);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println(ConsoleColors.GREEN + "Seller data updated successfully." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED + "No update was made." + ConsoleColors.RESET);
            }

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public static boolean printSellersData() { // modify print format
        String query = "SELECT first_name, last_name, store_name, agency_code FROM sellers WHERE identity_verified = 0";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet resultSet = pstmt.executeQuery()) {

            boolean found = false;
            while (resultSet.next()) {
                found = true;
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String agencyCode = resultSet.getString("agency_code");
                String storeName = resultSet.getString("store_name");

                System.out.printf("name: %s %s, store name: %s, agency code: %s\n", firstName, lastName, storeName,
                        agencyCode);
            }

            if (!found) {
                System.out.println(
                        ConsoleColors.RED + "No sellers found with identity_verified = 0" + ConsoleColors.RESET);
                return false;
            }

        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
            return false;
        }
        return true;
    }

    public static boolean printByAgencyCode(String agencyCode) {
        String query = "SELECT * FROM sellers WHERE agency_code = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, agencyCode);

            try (ResultSet resultSet = pstmt.executeQuery()) {

                ResultSetMetaData meta = resultSet.getMetaData();
                int columnCount = meta.getColumnCount();

                boolean found = false;
                while (resultSet.next()) {
                    found = true;
                    System.out.println("");
                    for (int i = 2; i <= columnCount; i++) {
                        System.out.print(meta.getColumnName(i) + ": " + resultSet.getString(i) + "\n");
                    }
                }

                if (!found) {
                    System.out.println(ConsoleColors.RED + "No sellers found with agency_code " + ConsoleColors.RESET
                            + agencyCode);
                    return false;
                }
            }

        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
            return false;
        }
        return true;

    }

    public static void chargeWallet(User user) {
    String sql = "SELECT seller_id, price FROM shoppingCart WHERE user_id = ?";
    String sqlUpdate = "UPDATE sellers SET wallet_balance = wallet_balance + ? WHERE id = ?";

    try (
        Connection conn = DriverManager.getConnection(DB_URL);
        PreparedStatement stmt = conn.prepareStatement(sql);
        PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)
    ) {
        int userId = UserDAO.findUserId(user.getEmail());
        stmt.setInt(1, userId);

        // Map to track total price per seller
        Map<Integer, Double> sellerEarnings = new HashMap<>();

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int sellerId = rs.getInt("seller_id");
                double price = rs.getDouble("price");

                sellerEarnings.put(sellerId, sellerEarnings.getOrDefault(sellerId, 0.0) + price);
            }
        }

        // Update each seller's wallet with their total earnings
        for (Map.Entry<Integer, Double> entry : sellerEarnings.entrySet()) {
            int sellerId = entry.getKey();
            double amountToAdd = entry.getValue();

            stmtUpdate.setDouble(1, amountToAdd);
            stmtUpdate.setInt(2, sellerId);
            stmtUpdate.executeUpdate();
        }

        System.out.println("Wallets charged based on cart prices.");

    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Database error occurred.");
    }
}


    public static void withdrawMoney(double balance, String agencyCode) {
        String sqlUpdateWallet = "UPDATE sellers SET wallet_balance = wallet_balance - ? WHERE agency_code = ?";
        if (balance > getBalance(agencyCode)) {
            System.out.println(ConsoleColors.RED + "There is not enough money in your wallet" + ConsoleColors.RESET);
            return;
        }
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(sqlUpdateWallet)) {

            stmt.setDouble(1, balance);
            stmt.setString(2, agencyCode);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("No seller found with that agency code.");
            } else {
                System.out.println("Balance updated successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static double getBalance(String agencyCode) {
        String query = "SELECT wallet_balance FROM sellers WHERE agency_code = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, agencyCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("wallet_balance");
                } else {
                    System.out.println("Seller not found.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0;

    }
}
