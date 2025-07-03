package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
                + "isBlock INTEGER,"
                + "Recent_purchases INTEGER,"
                + "Follow_up_request INTEGER,"
                + "Identity_verification INTEGER"
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
        String sql = "INSERT INTO supporters(name, username, password, isBlock, Recent_purchases, Follow_up_request, Identity_verification) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setInt(4, 0);
            pstmt.setInt(5, 0);
            pstmt.setInt(6, 0);
            pstmt.setInt(7, 0);

            pstmt.executeUpdate();
            System.out.println(ConsoleColors.GREEN + "Supporter inserted successfully." + ConsoleColors.RESET);
            return true;
        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Insert failed: " + e.getMessage() + ConsoleColors.RESET);
            return false;
        }
    }

    public static void updateSupporterAndManagerData(String username, String usertype, String field, String newValue) {

        String sql = "UPDATE " + usertype + " SET " + field + " = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newValue);
            stmt.setString(2, username);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out
                        .println(ConsoleColors.GREEN + usertype + " data updated successfully." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED + "No update was made." + ConsoleColors.RESET);
            }

        } catch (SQLException e) {
            e.getMessage();
        }

    }

    public static void displayAllManagerAndSupporter(String userType) {
        String query = "SELECT name, username, password FROM " + userType;

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet resultSet = stmt.executeQuery(query)) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                System.out.println("");

                System.out.printf("Name: %s | username: %s | password: %s%n",
                        name, username, password);
            }

        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    public static void printSupporterLimitation() {
        String query = "SELECT id, username, Recent_purchases, Follow_up_request, Identity_verification FROM supporters";
        int recentPurchases;
        int followUpRequest;
        int identityVerification;
        int id ;
        String username = "";
        String hasFollowUpRequest = "False";
        String hasIdentityVerification = "False";
        String hasRecentPurchases = "False";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet resultSet = stmt.executeQuery(query)) {

            while (resultSet.next()) {
                id = resultSet.getInt("id");
                username = resultSet.getString("username");
                recentPurchases = resultSet.getInt("Recent_purchases");
                followUpRequest = resultSet.getInt("Follow_up_request");
                identityVerification = resultSet.getInt("Identity_verification");
                if (identityVerification == 1) {
                    hasIdentityVerification = "True";
                }
                if (followUpRequest == 1) {
                    hasFollowUpRequest = "True";
                }
                if (recentPurchases == 1) {
                    hasRecentPurchases = "True";
                }

                System.out.printf(
                        "User id: %d| Username: %s| Identity verification: %s| Follow up request: %s| Recent purchases: %s%n",
                        id, username, hasIdentityVerification, hasFollowUpRequest, hasRecentPurchases);

            }
            System.out.println("");

        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    public static void mofifyAccess(String accessType, int id, int value) {

        String sql = "UPDATE supporters SET " + accessType + " = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, value);
            stmt.setInt(2, id);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out
                        .println(ConsoleColors.GREEN + " Supporter access updated successfully." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED + "No update was made." + ConsoleColors.RESET);
            }

        } catch (SQLException e) {
            e.getMessage();
        }

    }

}
