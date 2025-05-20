package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RequestDAO {

    private static final String DB_URL = "jdbc:sqlite:data.db";


    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS requests ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "user_id INTEGER NOT NULL,"
            + "title TEXT NOT NULL,"
            + "request_context TEXT,"
            + "status TEXT,"
            + "resopnd TEXT"
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

    public static boolean insertRequest(String email, String title, String requestContext) {
        String query = "SELECT id FROM users WHERE email = ?";
        String sql = "INSERT INTO Books(user_id, title, request_context, status)"
        + "VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, email);
                var rs = pstmt.executeQuery();
        
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    
        
                try (PreparedStatement insertStmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, userId);
                    pstmt.setString(2, title);
                    pstmt.setString(3,requestContext);
                    pstmt.setString(4, "Following up");
                    

                    insertStmt.executeUpdate();
                    System.out.println("Your request regestered successfully.");
                    return true;
                    } 
                }else {
                    return false;
                }

            }catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
    }

    public static void printRequestsByFieldName(String fieldName) {
    String query1 = "SELECT id, user_id FROM requests WHERE title = ?";
    String query2 = "SELECT email FROM users WHERE id = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement stmt1 = conn.prepareStatement(query1);
         PreparedStatement stmt2 = conn.prepareStatement(query2)) {
        
        stmt1.setString(1, fieldName);
        ResultSet rs1 = stmt1.executeQuery();

        while (rs1.next()) {
            int requestId = rs1.getInt("id");
            int userId = rs1.getInt("user_id");

            stmt2.setInt(1, userId);
            ResultSet rs2 = stmt2.executeQuery();

            if (rs2.next()) {
                String email = rs2.getString("email");
                System.out.println("Request ID: " + requestId + ", Email: " + email);
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public static void printAllRequestInfoById(int id) {
        String query1 = "SELECT * FROM requests WHERE id = ?";
        String query2 = "SELECT email FROM users WHERE id = ?";


        try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement stmt1 = conn.prepareStatement(query1);
         PreparedStatement stmt2 = conn.prepareStatement(query2)) {
        
        stmt1.setInt(1, id);
        ResultSet rs1 = stmt1.executeQuery();

        while (rs1.next()) {
            int requestId = rs1.getInt("id");
            int userId = rs1.getInt("user_id");
            String title  = rs1.getString("title");
            String context = rs1.getString("request_context");

            stmt2.setInt(1, userId);
            ResultSet rs2 = stmt2.executeQuery();

            if (rs2.next()) {
                String email = rs2.getString("email");
                System.out.println("Request title: " + title);
                System.out.println("Request id: " + requestId);
                System.out.println("User email: " + email);
                System.out.println("User context: " + context);

            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }


    }

    public static void setMessageAndUpdateStatus(String message, int id) {
        String query = "UPDATE requests SET message = ? WHERE id = ?";
    
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
    
            pstmt.setString(1, message);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void printRequestsByEmail(String email) {
        String query1 = "SELECT id FROM users WHERE email = ?";
        String query2 = "SELECT id, title, status FROM requests WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement stmt1 = conn.prepareStatement(query1);
         PreparedStatement stmt2 = conn.prepareStatement(query2)) {
        
        stmt1.setString(1, email);
        ResultSet rs1 = stmt1.executeQuery();

        while (rs1.next()) {
            int userId = rs1.getInt("user_id");

            stmt2.setInt(1, userId);
            ResultSet rs2 = stmt2.executeQuery();

            if (rs2.next()) {
                int id = rs2.getInt("id");
                String title = rs2.getString("title");
                String status = rs2.getString("status");
                System.out.println("Request title: " + title + ", Request id: " + id + ", status: " + status);
            }
        }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void printRespondOfRequest(int id) {
        String query = "SELECT request_context, status, respond FROM requests WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            stmt.setInt(1, id);

            String context = rs.getString("request_context");
            String respond = rs.getString("respond");
            System.out.println("Request context: " + context + ",\nSupporter respond: " + respond);
        
        }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
