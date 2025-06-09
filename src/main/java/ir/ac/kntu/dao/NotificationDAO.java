package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.controllers.NotificationController;
import ir.ac.kntu.model.User;

public class NotificationDAO {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS notifications ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_id INTEGER NOT NULL,"
                + "type TEXT NOT NULL,"
                + "message TEXT NOT NULL,"
                + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
                + ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Table created or already exists.");

        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

    public static void insertNotification(User user, String type, String message) {
        String query1 = "INSERT INTO discounts(user_id, type, message) "
                + "VALUES (?, ?, ?)";
        int userId = UserDAO.findUserId(user.getEmail());

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement insertStmt = conn.prepareStatement(query1)) {

            insertStmt.setInt(1, userId);
            insertStmt.setString(2, type);
            insertStmt.setString(3, message);

            insertStmt.executeUpdate();
            System.out.println(ConsoleColors.GREEN + "Notification inserted successfully." + ConsoleColors.RESET);
        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Insert failed: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    public static void printNotifPreview(User user) {

        int userId = UserDAO.findUserId(user.getEmail());
        if (userId == -1) {
            System.out.println(ConsoleColors.RED + "User not found." + ConsoleColors.RESET);
            return;
        }

        String query = "SELECT id, type FROM notifications WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            System.out.println(ConsoleColors.BLUE + "-------------NOTIFICATION-------------:" + ConsoleColors.RESET);
            while (rs.next()) {
                int notifId = rs.getInt("id");
                String type = rs.getString("type");

                System.out.println("ID: " + notifId +
                        " | Type: " + type);
            }
            System.out.println(ConsoleColors.BLUE + "--------------------------------------:" + ConsoleColors.RESET);
        } catch (SQLException e) {
            System.out.println(
                    ConsoleColors.RED + "Error fetching notification: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    public static boolean notifExist(int input) {
        String sql = "SELECT 1 FROM notifications WHERE id = ? LIMIT 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, input);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Sorry. notification with this id dose not exist");
        return false;
    }

    public static void printNotifInfo(int id, User user) {
        String query = "SELECT type, message FROM notifications WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type");
                String message = rs.getString("message");
                NotificationController.handleNotifTypes(type, message, user);
                
            }
        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Error fetching discounts: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

}
