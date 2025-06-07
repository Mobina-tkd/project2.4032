package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.model.User;

public class PurchasesDAO {

    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS purchases ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_id INTEGER NOT NULL,"
                + "seller_id INTEGER NOT NULL,"
                + "information TEXT NOT NULL ,"
                + "name TEXT NOT NULL ,"
                + "price REAL NOT NULL,"
                + "date TEXT NOT NULL, "
                + "address TEXT NOT NULL,"
                + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,"
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

    public static boolean insertToPurchases(User user, String date, String address) {
        String getCartSQL = "SELECT * FROM shoppingCart WHERE user_id = ?";
        String insertPurchaseSQL = "INSERT INTO purchases(user_id, seller_id, name, information, price, date, address) "
                                 + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);
    
            int userId = UserDAO.findUserId(user.getEmail());
    
            // Retrieve shopping cart items
            try (PreparedStatement cartStmt = conn.prepareStatement(getCartSQL)) {
                cartStmt.setInt(1, userId);
                try (ResultSet cartItems = cartStmt.executeQuery()) {
                    boolean hasItems = false;
    
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertPurchaseSQL)) {
                        while (cartItems.next()) {
                            hasItems = true;
    
                            insertStmt.setInt(1, userId);
                            insertStmt.setInt(2, cartItems.getInt("seller_id"));
                            insertStmt.setString(3, cartItems.getString("name"));
                            insertStmt.setString(4, cartItems.getString("information"));
                            insertStmt.setDouble(5, cartItems.getDouble("price"));
                            insertStmt.setString(6, date);
                            insertStmt.setString(7, address);
                            insertStmt.addBatch();
                        }
    
                        if (!hasItems) {
                            conn.rollback();
                            return false;
                        }
    
                        insertStmt.executeBatch();
                    }
                }
            }
    
            conn.commit();
            return true;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static void printUserPurchases(User user) {
        String queryPurchases = "SELECT id, name, seller_id, date, price FROM purchases WHERE user_id = ?";
        String queryStoreName = "SELECT store_name FROM sellers WHERE id = ?";
    
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
    
            int userId = UserDAO.findUserId(user.getEmail());
            if (userId == -1) {
                System.out.println(ConsoleColors.RED + "User not found." + ConsoleColors.RESET);
                return;
            }
    
            try (PreparedStatement stmt = conn.prepareStatement(queryPurchases)) {
                stmt.setInt(1, userId);
                try (ResultSet resultSet = stmt.executeQuery()) {
    
                    while (resultSet.next()) {
    
                        int purchaseId = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        int sellerId = resultSet.getInt("seller_id");
                        String date = resultSet.getString("date");
                        double price = resultSet.getDouble("price");
                        String storeName = "Unknown";
    
                        try (PreparedStatement storeStmt = conn.prepareStatement(queryStoreName)) {
                            storeStmt.setInt(1, sellerId);
                            try (ResultSet storeRs = storeStmt.executeQuery()) {
                                if (storeRs.next()) {
                                    storeName = storeRs.getString("store_name");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
    
                        System.out.println("ID: " + purchaseId +
                                " | Purchase: " + name +
                                " | Store: " + storeName +
                                " | Date: " + date +
                                " | Price: " + String.format("%.2f", price));
                    }
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public static void printAllPurchases() {
        String queryPurchases = "SELECT id, name, seller_id, user_id, date, price FROM purchases";
        String queryStoreName = "SELECT store_name FROM sellers WHERE id = ?";
        String queryUserEmail = "SELECT email FROM users WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmtPurchases = conn.prepareStatement(queryPurchases);
                ResultSet resultSet = stmtPurchases.executeQuery()) {

            while (resultSet.next()) {
                int purchaseId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int sellerId = resultSet.getInt("seller_id");
                int userId = resultSet.getInt("user_id");
                String date = resultSet.getString("date");
                double price = resultSet.getDouble("price");

                String storeName = "Unknown";
                try (PreparedStatement storeStmt = conn.prepareStatement(queryStoreName)) {
                    storeStmt.setInt(1, sellerId);
                    try (ResultSet storeRs = storeStmt.executeQuery()) {
                        if (storeRs.next()) {
                            storeName = storeRs.getString("store_name");
                        }
                    }
                }

                String email = "Unknown";
                try (PreparedStatement userStmt = conn.prepareStatement(queryUserEmail)) {
                    userStmt.setInt(1, userId);
                    try (ResultSet userRs = userStmt.executeQuery()) {
                        if (userRs.next()) {
                            email = userRs.getString("email");
                        }
                    }
                }

                System.out.printf("Purchase ID: %d | Name: %s | Store: %s | Date: %s | Price: %.2f | User Email: %s%n",
                        purchaseId, name, storeName, date, price, email);
            }

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public static void printAllSellerPurchases(String agencyCode) {
        String querySellerId = "SELECT id, store_name FROM sellers WHERE agency_code = ?";
        String queryPurchases = "SELECT id, name, user_id, date, price FROM purchases WHERE seller_id = ?";
        String queryUserEmail = "SELECT email FROM users WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            int sellerId = -1;
            String storeName = "Unknown";
            try (PreparedStatement stmt = conn.prepareStatement(querySellerId)) {
                stmt.setString(1, agencyCode);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        sellerId = resultSet.getInt("id");
                        storeName = resultSet.getString("store_name");
                    } else {
                        System.out.println(ConsoleColors.RED + "Seller not found." + ConsoleColors.RESET);
                        return;
                    }
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(queryPurchases)) {
                stmt.setInt(1, sellerId);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        int purchaseId = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        int userId = resultSet.getInt("user_id");
                        String date = resultSet.getString("date");
                        double price = resultSet.getDouble("price");
                        String email = "Unknown";
                        try (PreparedStatement userStmt = conn.prepareStatement(queryUserEmail)) {
                            userStmt.setInt(1, userId);
                            try (ResultSet userRs = userStmt.executeQuery()) {
                                if (userRs.next()) {
                                    email = userRs.getString("email");
                                }
                            }
                        }
                        System.out.printf(
                                "Purchase ID: %d | Name: %s | Store: %s | Date: %s | Price: %.2f | User Email: %s%n",
                                purchaseId, name, storeName, date, price, email);
                    }
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public static void printAlldetailsOfPurchase(int purchaseId) {
        String query1 = "SELECT * FROM purchases WHERE id = ?";
        String query2 = "SELECT email FROM users WHERE id = ?";
        String query3 = "SELECT store_name FROM sellers WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt1 = conn.prepareStatement(query1)) {

            stmt1.setInt(1, purchaseId);
            try (ResultSet resultSet1 = stmt1.executeQuery()) {
                if (resultSet1.next()) {
                    String name = resultSet1.getString("name");
                    double price = resultSet1.getDouble("price");
                    String date = resultSet1.getString("date");
                    int userId = resultSet1.getInt("user_id");
                    int sellerId = resultSet1.getInt("seller_id");
                    String info = resultSet1.getString("information");

                    String email = "";
                    try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                        stmt2.setInt(1, userId);
                        try (ResultSet resultSet2 = stmt2.executeQuery()) {
                            if (resultSet2.next()) {
                                email = resultSet2.getString("email");
                            }
                        }
                    }

                    String storeName = "";
                    try (PreparedStatement stmt3 = conn.prepareStatement(query3)) {
                        stmt3.setInt(1, sellerId);
                        try (ResultSet resultSet3 = stmt3.executeQuery()) {
                            if (resultSet3.next()) {
                                storeName = resultSet3.getString("store_name");
                            }
                        }
                    }

                    System.out.printf(
                            "Name: %s, Price: %.2f, Date: %s, User email: %s, Store name: %s, More information: %s\n",
                            name, price, date, email, storeName, info);
                } else {
                    System.out.println(ConsoleColors.RED + "Purchase not found." + ConsoleColors.RESET);
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
    }

}
