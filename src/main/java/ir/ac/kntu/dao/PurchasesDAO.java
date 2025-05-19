package ir.ac.kntu.dao;

import java.sql.Connection;
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
            int productId = rs2.getInt("product_id");
            String name = rs2.getString("name");
            String info = rs2.getString("information");
            double price = rs2.getDouble("price");

            pstmt3.setInt(1, userId);
            pstmt3.setInt(2, sellerId);
            pstmt3.setInt(3, productId);
            pstmt3.setString(4, name);
            pstmt3.setString(5, info);
            pstmt3.setDouble(6, price);
            pstmt3.setString(7, date);
            pstmt3.setString(8, address);

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

   
}
