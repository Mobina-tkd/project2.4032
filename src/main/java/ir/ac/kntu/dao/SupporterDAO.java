package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.helper.ConsoleColors;

public class SupporterDAO {

    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS supporters ("
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

    public static Boolean insertSopporter(String name, String username, String password) {
        String sql = "INSERT INTO supporters(name, username, password, isBlock) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setInt(4, 0);

            

            pstmt.executeUpdate();
            System.out.println(ConsoleColors.GREEN + "Supporter inserted successfully." + ConsoleColors.RESET);
            return true;
        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Insert failed: " + e.getMessage() + ConsoleColors.RESET);
            return false;
        }
    }

    public static void updateSupporterAndManagerData(String username, String usertype, String field, String newValue) {

        String sql = "UPDATE "+ usertype +" SET " + field + " = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newValue);
            stmt.setString(2, username);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println(ConsoleColors.GREEN + usertype +" data updated successfully." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED +"No update was made." + ConsoleColors.RESET);
            }

        } catch (SQLException e) {
            e.getMessage();
        }

    }

}
