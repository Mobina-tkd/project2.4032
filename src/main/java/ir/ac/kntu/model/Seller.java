package ir.ac.kntu.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


public class Seller {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    private String firstName;
    private String lastName;
    private String IDNumber;
    private String storeName;
    private String state;
    private String phoneNumber;
    private String password;
    private boolean identityVerified = false;
    private String agencyCode;

    

    public Seller(String firstName, String lastName, String IDNumber, String storeName,
        String state, String phoneNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.IDNumber = IDNumber;
        this.storeName = storeName;
        this.state = state;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getIDNumber() {
        return IDNumber;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getState() {
        return state;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public boolean isIdentityVerified() {
        return identityVerified;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getAgencyCode() {
        return agencyCode;
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
