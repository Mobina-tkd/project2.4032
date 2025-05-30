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
        String sql1 = "SELECT id FROM users WHERE email = ?";
        String sql2 = "SELECT * FROM shoppingCart WHERE user_id = ?";
        String sql3 = "INSERT INTO purchases(user_id, seller_id, name, information, price, date, address) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);

            int userId;
            try (PreparedStatement pstmt1 = conn.prepareStatement(sql1)) {
                pstmt1.setString(1, user.getEmail());
                try (ResultSet resultSet1 = pstmt1.executeQuery()) {
                    if (!resultSet1.next()) {
                        return false;
                    }
                    userId = resultSet1.getInt("id");
                }
            }

            try (PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
                pstmt2.setInt(1, userId);
                try (ResultSet resultSet2 = pstmt2.executeQuery()) {

                    boolean hasItems = false;

                    try (PreparedStatement pstmt3 = conn.prepareStatement(sql3)) {
                        while (resultSet2.next()) {
                            hasItems = true;

                            // Set parameters for insert statement here:
                            pstmt3.setInt(1, userId);
                            pstmt3.setInt(2, resultSet2.getInt("seller_id"));
                            pstmt3.setString(3, resultSet2.getString("name"));
                            pstmt3.setString(4, resultSet2.getString("information"));
                            pstmt3.setDouble(5, resultSet2.getDouble("price"));
                            pstmt3.setString(6, date);
                            pstmt3.setString(7, address);
                            pstmt3.addBatch();
                        }

                        if (!hasItems) {
                            conn.rollback();
                            return false;
                        }

                        pstmt3.executeBatch();
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
        String queryUserId = "SELECT id FROM users WHERE email = ?";
        String queryPurchases = "SELECT id, name, seller_id, date, price FROM purchases WHERE user_id = ?";
        String queryStoreName = "SELECT store_name FROM sellers WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            int userId = -1;
            try (PreparedStatement stmt = conn.prepareStatement(queryUserId)) {
                stmt.setString(1, user.getEmail());
                try (ResultSet resultSet = stmt.executeQuery()) {
                    if (resultSet.next()) {
                        userId = resultSet.getInt("id");
                    } else {
                        System.out.println(ConsoleColors.RED + "User not found." + ConsoleColors.RESET);
                        return;
                    }
                }
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
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }catch (Exception e) {
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

    private static void setValues(ResultSet resultSet2, PreparedStatement pstmt3, int userId, String date,
            String address) throws SQLException {
        int sellerId = resultSet2.getInt("seller_id");
        String name = resultSet2.getString("name");
        String info = resultSet2.getString("information");
        double price = resultSet2.getDouble("price");

        pstmt3.setInt(1, userId);
        pstmt3.setInt(2, sellerId);
        pstmt3.setString(3, name);
        pstmt3.setString(4, info);
        pstmt3.setDouble(5, price);
        pstmt3.setString(6, date);
        pstmt3.setString(7, address);
    }
}
