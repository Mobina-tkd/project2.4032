package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.model.User;

public class UserDAO {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "first_name TEXT,"
                + "last_name TEXT,"
                + "email TEXT UNIQUE NOT NULL,"
                + "phone_number TEXT UNIQUE,"
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
            System.out.println(ConsoleColors.GREEN +"User inserted successfully." + ConsoleColors.RESET);
            return true;
        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED +"Insert failed: " + e.getMessage() + ConsoleColors.RESET);
            return false;
        }
    }

    public static User findUser(String username) {
        String sql = "SELECT first_name, last_name, email, phone_number, password FROM users WHERE email = ? OR phone_number = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, username);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String email = resultSet.getString("email");
                    String phone = resultSet.getString("phone_number");
                    String password = resultSet.getString("password");

                    return new User(firstName, lastName, email, phone, password);
                } else {
                    System.out.println(ConsoleColors.RED +"No user found with email or phone: " + ConsoleColors.RESET + username);
                    return null;
                }
            }

        } catch (SQLException e) {
            System.err.println(ConsoleColors.RED +"Error finding user: " + e.getMessage() + ConsoleColors.RESET);
            return null;
        }
    }

    public static void setAndUpdateUserData(User user, String field, String newValue) {

        String sql = "UPDATE users SET " + field + " = ? WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newValue);
            stmt.setString(2, user.getEmail());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println(ConsoleColors.GREEN +"User data updated successfully." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED +"No update was made." + ConsoleColors.RESET);
            }

        } catch (SQLException e) {
            e.getMessage();
        }

        switch (field) {
            case "email" -> user.setEmail(newValue);
            case "phone_number" -> user.setPhoneNumber(newValue);
            case "first_name" -> user.setFirstName(newValue);
            case "last_name" -> user.setLastName(newValue);
            case "password" -> user.setPassword(newValue);
            default -> throw new AssertionError("Unknown field: " + field);
        }
    }
}
