package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;

import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.model.User;

public class PurchasesDAO {

    private static final String DB_URL = "jdbc:sqlite:data.db";

    // Query Constants
    private static final String PURCHASES_TABLE = "CREATE TABLE IF NOT EXISTS purchases (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "user_id INTEGER NOT NULL," +
            "seller_id INTEGER NOT NULL," +
            "information TEXT NOT NULL ," +
            "name TEXT NOT NULL ," +
            "price REAL NOT NULL," +
            "date TEXT NOT NULL, " +
            "address TEXT NOT NULL," +
            "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE," +
            "FOREIGN KEY (seller_id) REFERENCES sellers(id) ON DELETE CASCADE" +
            ");";

    private static final String SELECT_STORE = "SELECT store_name FROM sellers WHERE id = ?";
    private static final String SELECT_PURCHASES = "SELECT id, name, seller_id, date, price FROM purchases WHERE user_id = ?";
    private static final String ALL_PURCHASES = "SELECT id, name, seller_id, user_id, date, price FROM purchases";
    private static final String PURCHASE_BY_ID = "SELECT * FROM purchases WHERE id = ?";
    private static final String PURCHASES_SELLER = "SELECT id, name, user_id, date, price FROM purchases WHERE seller_id = ?";
    private static final String SELECT_CART_ITEMS = "SELECT * FROM shoppingCart WHERE user_id = ?";
    private static final String INSERT_PURCHASE = "INSERT INTO purchases(user_id, seller_id, name, information, price, date, address) "
            +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    public static void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            stmt.execute(PURCHASES_TABLE);
            System.out.println("Table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

    public static boolean insertToPurchases(User user, String date, String address) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);

            int userId = UserDAO.findUserId(user.getEmail());

            if (!hasItemsInCart(conn, userId)) {
                conn.rollback();
                return false;
            }

            insertCartItemsToPurchases(conn, userId, date, address);
            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean hasItemsInCart(Connection conn, int userId) throws SQLException {
        try (PreparedStatement cartStmt = conn.prepareStatement(SELECT_CART_ITEMS)) {
            cartStmt.setInt(1, userId);
            try (ResultSet cartItems = cartStmt.executeQuery()) {
                return cartItems.isBeforeFirst(); // true if there's at least one row
            }
        }
    }

    private static void insertCartItemsToPurchases(Connection conn, int userId, String date, String address)
            throws SQLException {
        try (PreparedStatement cartStmt = conn.prepareStatement(SELECT_CART_ITEMS);
                PreparedStatement insertStmt = conn.prepareStatement(INSERT_PURCHASE)) {

            cartStmt.setInt(1, userId);
            try (ResultSet cartItems = cartStmt.executeQuery()) {
                while (cartItems.next()) {
                    insertStmt.setInt(1, userId);
                    insertStmt.setInt(2, cartItems.getInt("seller_id"));
                    insertStmt.setString(3, cartItems.getString("name"));
                    insertStmt.setString(4, cartItems.getString("information"));
                    insertStmt.setDouble(5, cartItems.getDouble("price"));
                    insertStmt.setString(6, date);
                    insertStmt.setString(7, address);
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }
        }
    }

    public static void printUserPurchases(User user) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(SELECT_PURCHASES)) {

            int userId = UserDAO.findUserId(user.getEmail());
            if (userId == -1) {
                System.out.println(ConsoleColors.RED + "User not found." + ConsoleColors.RESET);
                return;
            }

            stmt.setInt(1, userId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int purchaseId = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int sellerId = resultSet.getInt("seller_id");
                    String date = resultSet.getString("date");
                    double price = resultSet.getDouble("price");
                    String storeName = getStoreName(conn, sellerId);

                    System.out.println("ID: " + purchaseId +
                            " | Purchase: " + name +
                            " | Store: " + storeName +
                            " | Date: " + date +
                            " | Price: " + String.format("%.2f", price));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void printAllPurchases() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(ALL_PURCHASES);
                ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                int purchaseId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int sellerId = resultSet.getInt("seller_id");
                int userId = resultSet.getInt("user_id");
                String date = resultSet.getString("date");
                double price = resultSet.getDouble("price");

                String storeName = getStoreName(conn, sellerId);
                String email = UserDAO.findEmailById(userId);

                System.out.printf("Purchase ID: %d | Name: %s | Store: %s | Date: %s | Price: %.2f | User Email: %s%n",
                        purchaseId, name, storeName, date, price, email);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void printAllSellerPurchases(String agencyCode) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            int sellerId = SellerDAO.findSellerIdByCode(agencyCode);
            if (sellerId == -1) {
                System.out.println(ConsoleColors.RED + "Seller not found." + ConsoleColors.RESET);
                return;
            }

            String storeName = getStoreName(conn, sellerId);
            printPurchases(conn, sellerId, storeName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printPurchases(Connection conn, int sellerId, String storeName) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(PURCHASES_SELLER)) {
            stmt.setInt(1, sellerId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int purchaseId = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int userId = resultSet.getInt("user_id");
                    String date = resultSet.getString("date");
                    double price = resultSet.getDouble("price");

                    String email = UserDAO.findEmailById(userId);

                    System.out.printf(
                            "Purchase ID: %d | Name: %s | Store: %s | Date: %s | Price: %.2f | User Email: %s%n",
                            purchaseId, name, storeName, date, price, email);
                }
            }
        }
    }

    public static void printAlldetailsOfPurchase(int purchaseId) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt1 = conn.prepareStatement(PURCHASE_BY_ID)) {

            stmt1.setInt(1, purchaseId);
            try (ResultSet resultSet1 = stmt1.executeQuery()) {
                if (resultSet1.next()) {
                    String name = resultSet1.getString("name");
                    double price = resultSet1.getDouble("price");
                    String date = resultSet1.getString("date");
                    int userId = resultSet1.getInt("user_id");
                    int sellerId = resultSet1.getInt("seller_id");
                    String info = resultSet1.getString("information");

                    String email = UserDAO.findEmailById(userId);
                    String storeName = getStoreName(conn, sellerId);

                    System.out.printf(
                            "Name: %s, Price: %.2f, Date: %s, User email: %s, Store name: %s, More information: %s\n",
                            name, price, date, email, storeName, info);
                } else {
                    System.out.println(ConsoleColors.RED + "Purchase not found." + ConsoleColors.RESET);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getStoreName(Connection conn, int sellerId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_STORE)) {
            stmt.setInt(1, sellerId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("store_name");
                }
            }
        }
        return "Unknown";
    }

    public static double findLastMonthTransaction(int userId, String userType) {
        double totalTransaction = 0.0;
        String query = "SELECT price, date FROM purchases WHERE " + userType + "_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            try (ResultSet resultSet = stmt.executeQuery()) {

                Instant oneMonthAgo = Instant.now().minus(Duration.ofDays(30));

                while (resultSet.next()) {
                    double price = resultSet.getDouble("price");
                    String dateStr = resultSet.getString("date");
                    Instant transactionDate = Instant.parse(dateStr);

                    if (transactionDate.isAfter(oneMonthAgo)) {
                        totalTransaction += price;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalTransaction;
    }
}
