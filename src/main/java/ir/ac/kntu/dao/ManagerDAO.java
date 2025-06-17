package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.helper.ConsoleColors;

public class ManagerDAO {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS managers ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT,"
                + "username TEXT UNIQUE,"
                + "password TEXT NOT NULL,"
                + "isBlock INTEGER"
                + ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

    public static Boolean insertManager(String name, String username, String password) {
        String sql = "INSERT INTO managers(name, username, password, isBlock) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setInt(4, 0);


            pstmt.executeUpdate();
            System.out.println(ConsoleColors.GREEN + "Manager inserted successfully." + ConsoleColors.RESET);
            return true;
        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Insert failed: " + e.getMessage() + ConsoleColors.RESET);
            return false;
        }
    }

    public static boolean userExist(String usertype, String username) {
        String query = "SELECT 1 FROM " + usertype + " WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); 
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void blockManagerAndSupporter(String username, String usertype)  {
        String query = "UPDATE "+ usertype+" SET isBlock = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setDouble(1, 1);
            stmt.setString(2, username);
    
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("No user in "+ usertype +" found.");
            } else {
                System.out.println("User in " + usertype + "has been blocked successfully.");
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
