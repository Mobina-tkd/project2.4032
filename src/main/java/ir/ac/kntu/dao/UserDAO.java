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
                + "password TEXT NOT NULL,"
                + "balance REAL,"
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

    public static Boolean insertUser(User user) {
        String sql = "INSERT INTO users(first_name, last_name, email, phone_number, password, balance, isBlock) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPhoneNumber());
            pstmt.setString(5, user.getPassword());
            pstmt.setDouble(6, 0);
            pstmt.setInt(7, 0);

            pstmt.executeUpdate();
            System.out.println(ConsoleColors.GREEN + "User inserted successfully." + ConsoleColors.RESET);
            return true;
        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Insert failed: " + e.getMessage() + ConsoleColors.RESET);
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
                    System.out.println(
                            ConsoleColors.RED + "No user found with email or phone: " + ConsoleColors.RESET + username);
                    System.out.println("");
                    return null;
                }
            }

        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Error finding user: " + e.getMessage() + ConsoleColors.RESET);
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
                System.out.println(ConsoleColors.GREEN + "User data updated successfully." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED + "No update was made." + ConsoleColors.RESET);
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

    public static double getBalance(User user) {
        String query = "SELECT balance FROM users WHERE email = ?";
        String email = user.getEmail();

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("balance");
                } else {
                    System.out.println("User not found.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0; // Default balance if user not found or error occurs
    }

    public static void updateBalance(double balance, User user, String operation) {
        String sqlUpdateWallet = "UPDATE users SET balance = balance " + operation + " ? WHERE email = ?";
        String email = user.getEmail();

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(sqlUpdateWallet)) {

            stmt.setDouble(1, balance);
            stmt.setString(2, email);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("No user found with that email.");
            } else {
                System.out.println("Balance updated successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int findUserId(String email) {
        String query = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            try (ResultSet resultSet = stmt.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void blockUser(String email) {
        String query = "UPDATE users SET isBlock = ? WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, 1);
            stmt.setString(2, email);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("No user found.");
            } else {
                System.out.println("User has been blocked successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showAllUsers() {
        String query = "SELECT first_name, last_name, email, phone_number FROM users";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet resultSet = stmt.executeQuery(query)) {

            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String phomeNumber = resultSet.getString("phone_number");
                System.out.println("");

                System.out.printf("First name: %s | Last name: %s | Email: %s | Phone number: %s%n",
                        firstName, lastName, email, phomeNumber);
            }

        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    public static void setMessageForAllUsers(String message) {
        String query = "SELECT email FROM users";
        String email = "";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet resultSet = stmt.executeQuery(query)) {

            while (resultSet.next()) {
                email = resultSet.getString("email");
                NotificationDAO.insertNotification(email, "Broadcast message", message);
            }

        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    public static void setDiscountForAllUsers(String type, String code, Double amount) {
        String query = "SELECT email FROM users";
        String email = "";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet resultSet = stmt.executeQuery(query)) {

            while (resultSet.next()) {
                email = resultSet.getString("email");
                DiscountDAO.insertDiscount(email, type, code, amount, 1);
            }

        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        }

    }

    public static void printUserFunction() {
        String query = "SELECT id FROM users";
        int userId;
        double lastMonthIncome = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet resultSet = stmt.executeQuery(query)) {

            while (resultSet.next()) {
                userId = resultSet.getInt("id");
                lastMonthIncome = PurchasesDAO.findLastMonthTransaction(userId, "user");
                System.out.printf("User id : %d| Last month transaction: %f%n", userId, lastMonthIncome);
            }

        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        }

    }

    public static String findEmailById(int userId) {
        String query = "SELECT email FROM users WHERE id = ? ";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            try (ResultSet resultSet = stmt.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getString("email");
                } else {
                    return "";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

}
