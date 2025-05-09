package ir.ac.kntu;

import java.sql.*;

public class UserDAO {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                   + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + "first_name TEXT,"
                   + "last_name TEXT,"
                   + "email TEXT UNIQUE NOT NULL,"
                   + "phone_number TEXT UNIQUE ,"
                   + "password TEXT NOT NULL"
                   + ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

    public static Boolean insertUser(User user) {
        String sql = "INSERT INTO users(first_name, last_name, email, phone_number, password) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPhoneNumber());
            pstmt.setString(5, user.getPassword());

            pstmt.executeUpdate();
            System.out.println("User inserted successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
    }
}

