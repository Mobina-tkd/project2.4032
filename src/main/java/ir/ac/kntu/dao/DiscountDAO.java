package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.model.User;

public class DiscountDAO {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS discounts ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_id INTEGER NOT NULL,"
                + "type TEXT NOT NULL,"
                + "code TEXT NOT NULL,"
                + "amount REAL NOT NULL,"
                + "used_time INTEGER NOT NULL,"
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

    public static void insertDiscount(User user, String type, String code, double amount, int usedTime) {
        String query1 = "INSERT INTO discounts(user_id, type, code, amount, used_time) "
                + "VALUES (?, ?, ?, ?, ?)";
        int userId = UserDAO.findUserId(user.getEmail());

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement insertStmt = conn.prepareStatement(query1)) {

            insertStmt.setInt(1, userId);
            insertStmt.setString(2, type);
            insertStmt.setString(3, code);
            insertStmt.setDouble(4, amount);
            insertStmt.setInt(5, usedTime);

            insertStmt.executeUpdate();
            System.out.println(ConsoleColors.GREEN + "Discount inserted successfully." + ConsoleColors.RESET);
        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Insert failed: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    public static void printDiscountPreview(User user) {
        if (user == null || user.getEmail() == null) {
            System.out.println(ConsoleColors.RED + "User or user email is null." + ConsoleColors.RESET);
            return;
        }

        int userId = UserDAO.findUserId(user.getEmail());
        if (userId == -1) {
            System.out.println(ConsoleColors.RED + "User not found." + ConsoleColors.RESET);
            return;
        }

        String query = "SELECT id, type, code FROM discounts WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            System.out.println(ConsoleColors.BLUE + "User Discount Preview:" + ConsoleColors.RESET);
            while (rs.next()) {
                int discountId = rs.getInt("id");
                String type = rs.getString("type");
                String code = rs.getString("code");
                
                System.out.println("ID: " + discountId +
                        " | Code: " + code +
                        " | Type: " + type );
            }
        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Error fetching discounts: " + e.getMessage() + ConsoleColors.RESET);
        }
    }


    public static void printDiscountInformation(int id) {
    String query = "SELECT type, code, amount, used_time FROM discounts WHERE id = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        System.out.println(ConsoleColors.BLUE + "User Discount information:" + ConsoleColors.RESET);
        while (rs.next()) {
            String type = rs.getString("type");
            String code = rs.getString("code");
            double amount = rs.getDouble("amount");
            int usedTime = rs.getInt("used_time");

            System.out.println(" | Code: " + code +
                               " | Type: " + type +
                               " | Amount of Discount: " + amount +
                               " | Used Time: " + usedTime + "\n");
        }
    } catch (SQLException e) {
        System.out.println(ConsoleColors.RED + "Error fetching discounts: " + e.getMessage() + ConsoleColors.RESET);
    }
}
 

}
