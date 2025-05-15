package ir.ac.kntu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


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
                    + "identity_varified INTEGER,"
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
        String sql = "INSERT INTO sellers(agency_code, first_name, last_name, ID_Number, store_name, state, phone_number, password, identity_varified, message) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, seller.getAgencyCode());
            pstmt.setString(2, seller.getFirstName());
            pstmt.setString(3, seller.getLastName());
            pstmt.setString(4, seller.getIDNumber());
            pstmt.setString(5, seller.getStoreName());
            pstmt.setString(6, seller.getState());
            pstmt.setString(7, seller.getPhoneNumber());
            pstmt.setString(8, seller.getPassword());
            pstmt.setInt(9, seller.isIdentityVerified() ? 1 : 0);
            pstmt.setString(10,"");
            

    
            pstmt.executeUpdate();
            System.out.println("Seller inserted successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
    }
    
    
}
