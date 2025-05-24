package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;

import ir.ac.kntu.model.Transaction;

public class TransactionDAO {

    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS transactions ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_id INTEGER NOT NULL,"
                + "type TEXT NOT NULL,"
                + "date TEXT NOT NULL,"
                + "amount REAL NOT NULL"
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


    public static boolean insertTransaction(String email,Transaction transaction) {
        String query = "SELECT id FROM users WHERE email = ?";
        String sql = "INSERT INTO transactions(user_id, type, date, amount) "
                   + "VALUES (?, ?, ?, ?)";    
        try (
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement selectStmt = conn.prepareStatement(query)
        ) {
            selectStmt.setString(1, email);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("id");
    
                    try (PreparedStatement insertStmt = conn.prepareStatement(sql)) {
                        insertStmt.setInt(1, userId);
                        insertStmt.setString(2, transaction.getType());
                        insertStmt.setString(3, transaction.getDate());
                        insertStmt.setDouble(4, transaction.getAmount());
                        
                        insertStmt.executeUpdate();
                        System.out.println("transaction inserted successfully.");
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
    
        return false;
    }

    public static void showAllTransactions(String email) {
        String queryUserId = "SELECT id FROM users WHERE email = ?";
        String queryRequests = "SELECT * FROM requests WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmtUserId = conn.prepareStatement(queryUserId);
                PreparedStatement stmtRequests = conn.prepareStatement(queryRequests)) {

            stmtUserId.setString(1, email);
            try (ResultSet rsUserId = stmtUserId.executeQuery()) {
                if (rsUserId.next()) {
                    int userId = rsUserId.getInt("id");

                    stmtRequests.setInt(1, userId);
                    try (ResultSet rsRequests = stmtRequests.executeQuery()) {
                        while (rsRequests.next()) {
                            String type = rsRequests.getString("type");
                            String date = rsRequests.getString("date");
                            double amount = rsRequests.getDouble("amount");
                            System.out.println(
                                    "Transaction: " + type + ", Date: " + date + ", Amount: " + amount);
                        }
                    }
                } else {
                    System.out.println("Transaction not found.");
                }
            }

        } catch (SQLException e) {
            e.getMessage();
        } 
    }


    public static void printByDate(Instant start, Instant end, String email) {
        String queryUserId = "SELECT id FROM users WHERE email = ?";
        String queryRequests = "SELECT * FROM requests WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement stmtUserId = conn.prepareStatement(queryUserId);
            PreparedStatement stmtRequests = conn.prepareStatement(queryRequests)) {

            stmtUserId.setString(1, email);
            try (ResultSet rsUserId = stmtUserId.executeQuery()) {
                if (rsUserId.next()) {
                    int userId = rsUserId.getInt("id");

                    stmtRequests.setInt(1, userId);
                    try (ResultSet rsRequests = stmtRequests.executeQuery()) {
                        boolean found = false;
                        while (rsRequests.next()) {
                            String type = rsRequests.getString("type");
                            String dateString = rsRequests.getString("date");
                            double amount = rsRequests.getDouble("amount");

                            Instant dateInstant = Instant.parse(dateString); 

                            if (!dateInstant.isBefore(start) && !dateInstant.isAfter(end)) {
                                found = true;
                                System.out.println("Transaction: " + type + ", Date: " + dateString + ", Amount: " + amount);
                            }
                        }
                        if (!found) {
                            System.out.println("No transactions found in the given date range.");
                        }
                    }
                } else {
                    System.out.println("User not found.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
