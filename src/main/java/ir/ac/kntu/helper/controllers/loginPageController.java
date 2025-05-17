package ir.ac.kntu.helper.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ir.ac.kntu.helper.ScannerWrapper;

public class loginPageController {
    private static final String DB_URL = "jdbc:sqlite:data.db";


    public static boolean sellerLoginPage() {
        while (true) {
            System.out.println("--------Login Page-------");
            System.out.print("Enter your agency code : ");
            String username = ScannerWrapper.getInstance().nextLine(); 
            System.out.print("Enter your password : ");
            String userPassword = ScannerWrapper.getInstance().nextLine(); 
    
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "SELECT password, identity_varified, message FROM sellers WHERE agency_code = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
    
                ResultSet rs = pstmt.executeQuery();
    
                if (rs.next()) {
                    String password = rs.getString("password");
                    int identityVarified = rs.getInt("identity_varified");
                    String message = rs.getString("message");
                    if (userPassword.equals(password) && identityVarified == 1) {
                        System.out.println("Welcome dear seller");
                        return true;
                    }else if(identityVarified == 2) {
                        System.out.println(message);
                        return true;

                    }else if(identityVarified == 0) {
                        System.out.println("your identity is not varified yet");
                        return false;

                    }
                    else {
                        System.out.println("Incorrect password. Try again.");
                    }
                } else {
                    System.out.println("We could not find this agency code.");
                    return false;
                }
    
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
                return false;
            }
        }
    }
    

    public static String  userLoginPage(){
        String url = "jdbc:sqlite:data.db";  
        while (true){
            System.out.println("--------Login Page-------");
            System.out.print("Enter your username(Phone Number or Email Address) :  ");
            String username = ScannerWrapper.getInstance().nextLine(); 
            System.out.print("Enter you password : ");
            String password = ScannerWrapper.getInstance().nextLine(); 

            String sql = "SELECT password FROM users WHERE email = ? OR phone_number = ?";

            try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, username);
                pstmt.setString(2, username);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    if (storedPassword.equals(password)) {
                        System.out.println("Login successful! Welcome.");
                        return username;
                    } else {
                        System.out.println("Incorrect password.");
                        return null;
                    }
                } else {
                    System.out.println("No account found with that email or phone number.");
                    return null;
                }

            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }

        }
    }


    public static boolean  supporterLoginPage() {
        System.out.print("Enter your username: ");
        String username = ScannerWrapper.getInstance().nextLine();
        System.out.print("Enter your password: ");
        String password = ScannerWrapper.getInstance().nextLine();

        if(username.equals("Sara_H82") && password.equals("S1382ara_")) {
            System.out.println("Welcome dear sara");
            return true;
        }else if(username.equals("mmd_L80") && password.equals("M1380md_")) {
            System.out.println("Welcome dear mohammad");
            return true;
        }else {
            System.out.println("Wrong username or password :( please try again...");
            return false;
        }

    }

}
