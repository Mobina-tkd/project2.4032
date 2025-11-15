package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.helper.ConsoleColors;

public class RequestDAO {

    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS requests ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_id INTEGER NOT NULL,"
                + "title TEXT NOT NULL,"
                + "request_context TEXT,"
                + "status TEXT,"
                + "respond TEXT,"
                + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
                + ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

    public static boolean insertRequest(String email, String title, String requestContext) {
        String queryUserId = "SELECT id FROM users WHERE email = ?";
        String insertSql = "INSERT INTO requests(user_id, title, request_context, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // Find user id
            int userId;
            try (PreparedStatement pstmt = conn.prepareStatement(queryUserId)) {
                pstmt.setString(1, email);
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    if (!resultSet.next()) {
                        return false; // user not found
                    }
                    userId = resultSet.getInt("id");
                }
            }

            // Insert request
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, title);
                insertStmt.setString(3, requestContext);
                insertStmt.setString(4, "Following up");

                insertStmt.executeUpdate();
                System.out.println(ConsoleColors.GREEN + "Your request registered successfully." + ConsoleColors.RESET);
                return true;
            }

        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Insert failed: " + e.getMessage() + ConsoleColors.RESET);
            return false;
        }
    }

    public static void printRequestsByFieldName(String fieldName) {
        String queryRequests = "SELECT id, user_id FROM requests WHERE title = ?";
        String queryEmail = "SELECT email FROM users WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmtRequests = conn.prepareStatement(queryRequests);
                PreparedStatement stmtEmail = conn.prepareStatement(queryEmail)) {

            stmtRequests.setString(1, fieldName);
            try (ResultSet rsRequests = stmtRequests.executeQuery()) {
                while (rsRequests.next()) {
                    int requestId = rsRequests.getInt("id");
                    int userId = rsRequests.getInt("user_id");

                    stmtEmail.setInt(1, userId);
                    try (ResultSet rsEmail = stmtEmail.executeQuery()) {
                        if (rsEmail.next()) {
                            String email = rsEmail.getString("email");
                            System.out.println("Request ID: " + requestId + ", Email: " + email);
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public static void printAllRequestInfoById(int requestId) {
        String queryRequest = "SELECT * FROM requests WHERE id = ?";
        String queryEmail = "SELECT email FROM users WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmtRequest = conn.prepareStatement(queryRequest);
                PreparedStatement stmtEmail = conn.prepareStatement(queryEmail)) {

            stmtRequest.setInt(1, requestId);
            try (ResultSet rsRequest = stmtRequest.executeQuery()) {
                if (rsRequest.next()) {
                    int userId = rsRequest.getInt("user_id");
                    String title = rsRequest.getString("title");
                    String context = rsRequest.getString("request_context");

                    stmtEmail.setInt(1, userId);
                    try (ResultSet rsEmail = stmtEmail.executeQuery()) {
                        if (rsEmail.next()) {
                            String email = rsEmail.getString("email");
                            System.out.println("Request title: " + title);
                            System.out.println("Request id: " + requestId);
                            System.out.println("User email: " + email);
                            System.out.println("Request context: " + context);
                        }
                    }
                } else {
                    System.out.println(ConsoleColors.RED + "Request not found." + ConsoleColors.RESET);
                }
            }

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public static void setMessageAndUpdateStatus(String message, int requestId) {
        String query = "UPDATE requests SET respond = ?, status = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, message);
            pstmt.setString(2, "Responded");
            pstmt.setInt(3, requestId);
            int updated = pstmt.executeUpdate();

            if (updated > 0) {
                System.out.println(ConsoleColors.GREEN + "Request updated successfully." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED + "Request not found." + ConsoleColors.RESET);
            }

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public static void printRequestsByEmail(String email) {
        String queryUserId = "SELECT id FROM users WHERE email = ?";
        String queryRequests = "SELECT id, title, status FROM requests WHERE user_id = ?";

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
                            int requestId = rsRequests.getInt("id");
                            String title = rsRequests.getString("title");
                            String status = rsRequests.getString("status");
                            System.out.println(
                                    "Request title: " + title + ", Request id: " + requestId + ", status: " + status);
                        }
                    }
                } else {
                    System.out.println(ConsoleColors.RED + "User not found." + ConsoleColors.RESET);
                }
            }

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public static void printRespondOfRequest(int requestId) {
        String query = "SELECT request_context, respond FROM requests WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, requestId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    String context = resultSet.getString("request_context");
                    String respond = resultSet.getString("respond");
                    System.out.println("Request context: " + context + ",\nSupporter respond: " + respond);
                } else {

                    System.out.println(ConsoleColors.RED + "Request not found." + ConsoleColors.RESET);
                }
            }

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public static String findUserEmailByRequestId(int requestId) {
        String query1 = "SELECT user_id FROM requests WHERE id = ?";
        String query2 = "SELECT email FROM users WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt1 = conn.prepareStatement(query1)) {

            stmt1.setInt(1, requestId);
            try (ResultSet rs1 = stmt1.executeQuery()) {

                if (rs1.next()) {
                    int userId = rs1.getInt("user_id");

                    try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                        stmt2.setInt(1, userId);
                        try (ResultSet rs2 = stmt2.executeQuery()) {

                            if (rs2.next()) {
                                return rs2.getString("email");
                            }
                        }
                    }
                } else {
                    System.out.println("Request ID not found.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
