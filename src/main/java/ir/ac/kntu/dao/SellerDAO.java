package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.model.Seller;


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
            e.printStackTrace();
        }
    }

    public static void setIdentityVarified(int value, String agencyCode) {
        String sql = "UPDATE sellers SET identity_varified = ? WHERE agency_code = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, value);
        stmt.setString(2, agencyCode);

        int rowsUpdated = stmt.executeUpdate();
        
        if (rowsUpdated > 0) {
            System.out.println("Seller data updated successfully.");
        } else {
            System.out.println("No update was made.");
        }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean  printSellersData() { //modify print format
            String query = "SELECT * FROM sellers WHERE identity_varified = 0";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(query)) {

                ResultSet rs = pstmt.executeQuery();

                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();

                boolean found = false;
                while (rs.next()) {
                    found = true;
                    for (int i = 2; i <= columnCount; i++) {
                        System.out.print(meta.getColumnName(i) + ": " + rs.getString(i) + "\t");
                    }
                    System.out.println();
                }

                if (!found) {
                    System.out.println("No products found with identity_varified 0");
                    return false;
                }

            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
                return false;
            }
            return true;
        }

    

    public static boolean printByAgencyCode(String agencyCode) { //modify print format
            String query = "SELECT * FROM sellers WHERE agency_code = ?";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, agencyCode);


                ResultSet rs = pstmt.executeQuery();

                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();

                boolean found = false;
                while (rs.next()) {
                    found = true;
                    for (int i = 2; i <= columnCount; i++) {
                        System.out.print(meta.getColumnName(i) + ": " + rs.getString(i) + "\t");
                    }
                    System.out.println();
                }

                if (!found) {
                    System.out.println("No products found with agency_code " + agencyCode );
                    return false;
                }

            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
                return false;
            }
            return true;

            }
    

            
    
}
