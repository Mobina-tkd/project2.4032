package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    Connection conn = null;
    PreparedStatement pstmt1 = null;
    PreparedStatement pstmt2 = null;
    PreparedStatement pstmt3 = null;
    ResultSet rs1 = null;
    ResultSet rs2 = null;

    try {
        conn = DriverManager.getConnection(DB_URL);
        conn.setAutoCommit(false); 

        pstmt1 = conn.prepareStatement(sql1);
        pstmt1.setString(1, user.getEmail());
        rs1 = pstmt1.executeQuery();

        if (!rs1.next()) {
            return false;
        }

        int userId = rs1.getInt("id");

        pstmt2 = conn.prepareStatement(sql2);
        pstmt2.setInt(1, userId);
        rs2 = pstmt2.executeQuery();

        boolean hasItems = false;

        pstmt3 = conn.prepareStatement(sql3);

        while (rs2.next()) {
            hasItems = true;
            int sellerId = rs2.getInt("seller_id");
            String name = rs2.getString("name");
            String info = rs2.getString("information");
            double price = rs2.getDouble("price");

            pstmt3.setInt(1, userId);
            pstmt3.setInt(2, sellerId);
            pstmt3.setString(3, name);
            pstmt3.setString(4, info);
            pstmt3.setDouble(5, price);
            pstmt3.setString(6, date);
            pstmt3.setString(7, address);

            pstmt3.addBatch();
        }

        if (!hasItems) {
            conn.rollback();
            return false; 
        }

        pstmt3.executeBatch();
        conn.commit();
        return true;

    } catch (SQLException e) {
        e.printStackTrace();
        try {
            if (conn != null) conn.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    } finally {
        try { if (rs1 != null) rs1.close(); } catch (Exception e) {}
        try { if (rs2 != null) rs2.close(); } catch (Exception e) {}
        try { if (pstmt1 != null) pstmt1.close(); } catch (Exception e) {}
        try { if (pstmt2 != null) pstmt2.close(); } catch (Exception e) {}
        try { if (pstmt3 != null) pstmt3.close(); } catch (Exception e) {}
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }
}


public static void printUserPurchases(User user) {
    String queryUserId = "SELECT id FROM users WHERE email = ?";
    String queryPurchases = "SELECT id, name, seller_id, date, price FROM purchases WHERE user_id = ?";
    String queryStoreName = "SELECT store_name FROM sellers WHERE id = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL)) {

        // Step 1: Get user ID from email
        int userId = -1;
        try (PreparedStatement stmt = conn.prepareStatement(queryUserId)) {
            stmt.setString(1, user.getEmail());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("id");
            } else {
                System.out.println("User not found.");
                return;
            }
        }

        // Step 2: Get purchases for that user ID
        try (PreparedStatement stmt = conn.prepareStatement(queryPurchases)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int purchaseId = rs.getInt("id");
                String name = rs.getString("name");
                int sellerId = rs.getInt("seller_id");
                Date date = rs.getDate("date");
                double price = rs.getDouble("price");

                // Step 3: Get store name for each purchase
                String storeName = "Unknown";
                try (PreparedStatement storeStmt = conn.prepareStatement(queryStoreName)) {
                    storeStmt.setInt(1, sellerId);
                    ResultSet storeRs = storeStmt.executeQuery();
                    if (storeRs.next()) {
                        storeName = storeRs.getString("store_name");
                    }
                }

                // Print purchase details
                System.out.printf("ID: %d | Purchase: %s | Store: %s | Date: %s | Price: %.2f%n",
                        purchaseId, name, storeName, date.toString(), price);
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
         ResultSet rs = stmtPurchases.executeQuery()) {

        while (rs.next()) {
            int purchaseId = rs.getInt("id");
            String name = rs.getString("name");
            int sellerId = rs.getInt("seller_id");
            int userId = rs.getInt("user_id");
            Date date = rs.getDate("date");
            double price = rs.getDouble("price");

            // Get store name
            String storeName = "Unknown";
            try (PreparedStatement storeStmt = conn.prepareStatement(queryStoreName)) {
                storeStmt.setInt(1, sellerId);
                ResultSet storeRs = storeStmt.executeQuery();
                if (storeRs.next()) {
                    storeName = storeRs.getString("store_name");
                }
            }

            // Get user email
            String email = "Unknown";
            try (PreparedStatement userStmt = conn.prepareStatement(queryUserEmail)) {
                userStmt.setInt(1, userId);
                ResultSet userRs = userStmt.executeQuery();
                if (userRs.next()) {
                    email = userRs.getString("email");
                }
            }

            System.out.printf("Purchase ID: %d | Name: %s | Store: %s | Date: %s | Price: %.2f | User Email: %s%n",
                    purchaseId, name, storeName, date.toString(), price, email);
        }

            } catch (SQLException e) {
                e.printStackTrace();
            }
    }




    public static void printAllSellerPurchases(String agencyCode) {
        String querySellerId = "SELECT id, store_name FROM sellers WHERE agency_code = ?";
        String queryPurchases = "SELECT id, name, user_id, date, price FROM purchases WHERE seller_id = ?";
        String queryUserEmail = "SELECT email FROM users WHERE id = ?";
    
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            
            // Step 1: Get the seller's ID and store name using agency code
            int sellerId = -1;
            String storeName = "Unknown";
            try (PreparedStatement stmt = conn.prepareStatement(querySellerId)) {
                stmt.setString(1, agencyCode);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    sellerId = rs.getInt("id");
                    storeName = rs.getString("store_name");
                } else {
                    System.out.println("Seller not found.");
                    return;
                }
            }
    
            // Step 2: Get purchases by that seller
            try (PreparedStatement stmt = conn.prepareStatement(queryPurchases)) {
                stmt.setInt(1, sellerId);
                ResultSet rs = stmt.executeQuery();
    
                while (rs.next()) {
                    int purchaseId = rs.getInt("id");
                    String name = rs.getString("name");
                    int userId = rs.getInt("user_id");
                    Date date = rs.getDate("date");
                    double price = rs.getDouble("price");
    
                    // Step 3: Get the user's email
                    String email = "Unknown";
                    try (PreparedStatement userStmt = conn.prepareStatement(queryUserEmail)) {
                        userStmt.setInt(1, userId);
                        ResultSet userRs = userStmt.executeQuery();
                        if (userRs.next()) {
                            email = userRs.getString("email");
                        }
                    }
    
                    // Print the result
                    System.out.printf("Purchase ID: %d | Name: %s | Store: %s | Date: %s | Price: %.2f | User Email: %s%n",
                            purchaseId, name, storeName, date.toString(), price, email);
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    





    public static void printAlldetailsOfPurchase(int id) {
        String query1 = "SELECT * FROM purchases WHERE id = ?";
        String query2 = "SELECT email FROM users WHERE id = ?";
        String query3 = "SELECT store_name FROM sellers WHERE id = ?";
    
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt1 = conn.prepareStatement(query1)) {
    
            stmt1.setInt(1, id);
            ResultSet rs1 = stmt1.executeQuery();
    
            if (rs1.next()) {
                String name = rs1.getString("name");
                double price = rs1.getDouble("price");
                String date = rs1.getString("date");
                int userId = rs1.getInt("user_id");
                int sellerId = rs1.getInt("seller_id");
                String info = rs1.getString("information");
    
                String email = "";
                try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                    stmt2.setInt(1, userId);
                    ResultSet rs2 = stmt2.executeQuery();
                    if (rs2.next()) {
                        email = rs2.getString("email");
                    }
                }
    
                String storeName = "";
                try (PreparedStatement stmt3 = conn.prepareStatement(query3)) {
                    stmt3.setInt(1, sellerId);
                    ResultSet rs3 = stmt3.executeQuery();
                    if (rs3.next()) {
                        storeName = rs3.getString("store_name");
                    }
                }
    
                System.out.printf("Name: %s, Price: %.2f, Date: %s, User email: %s, Store name: %s, More information: %s\n",
                        name, price, date, email, storeName, info);
            } else {
                System.out.println("Purchase not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    
}
