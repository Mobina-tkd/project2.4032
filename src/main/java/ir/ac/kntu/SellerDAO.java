package ir.ac.kntu;

import java.sql.*;


public class SellerDAO {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS sellers ("
                   + "agency_code INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + "first_name TEXT,"
                   + "last_name TEXT,"
                   + "ID_Number TEXT UNIQUE,"
                   + "stor_name TEXT UNIQUE,"
                   + "state TEXT,"
                   + "phone_number TEXT UNIQUE,"
                   + "password TEXT NOT NULL,"
                   + "identity_varified BOOLEAN"
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
        String sql = "INSERT INTO sellers(first_name, last_name, ID_Number, stor_name, state, phone_number, password, identity_varified) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, seller.getFirstName());
            pstmt.setString(2, seller.getLastName());
            pstmt.setString(3, seller.getIDNumber());
            pstmt.setString(4, seller.getStoreName());
            pstmt.setString(5, seller.getState());
            pstmt.setString(6, seller.getPhoneNumber());
            pstmt.setString(7, seller.getPassword());
            pstmt.setBoolean(8, seller.isIdentityVerified());
    
            pstmt.executeUpdate();
            System.out.println("Seller inserted successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
    }
    
    
}
