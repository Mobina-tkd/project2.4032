package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.model.Book;


public class BookDAO {
    private static final String DB_URL = "jdbc:sqlite:data.db";


    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Books ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "seller_id INTEGER NOT NULL,"
            + "name TEXT NOT NULL,"
            + "title TEXT NOT NULL,"
            + "price REAL NOT NULL,"
            + "inventory INTEGER NOT NULL,"
            + "writerName TEXT NOT NULL,"
            + "pageNumber INTEGER NOT NULL,"
            + "genre TEXT NOT NULL,"
            + "ageGroup TEXT NOT NULL,"
            + "ISBN TEXT NOT NULL,"
            + "FOREIGN KEY (seller_id) REFERENCES sellers(id) ON DELETE CASCADE"
            + ");";
    
        try (Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

    public static boolean insertBook(Book book, String agencyCode) {
        String query = "SELECT id FROM sellers WHERE agency_code = ?";
        String sql = "INSERT INTO books(seller_id, name, title, price, inventory, writerName, pageNumber, genre, ageGroup, ISBN)"
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, agencyCode);
                var rs = pstmt.executeQuery();
        
                if (rs.next()) {
                    int sellerId = rs.getInt("id");
        
                try (PreparedStatement insertStmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, sellerId);
                    pstmt.setString(2, "Book");
                    pstmt.setString(3, book.getTitle());
                    pstmt.setDouble(4, book.getPrice());
                    pstmt.setInt(5, book.getInventory());
                    pstmt.setString(6, book.getWriterName());
                    pstmt.setInt(7, book.getPageNumber());
                    pstmt.setString(8, book.getGenre());
                    pstmt.setString(9, book.getAgeGroup());
                    pstmt.setString(10, book.getISBN());

                    insertStmt.executeUpdate();
                    System.out.println("book inserted successfully.");
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

    public static void searchBookByTitle(String tableName, String title) {

        String query = "SELECT * FROM " + tableName + " WHERE title = " + "\'" + title + "\'";

        try (Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(meta.getColumnName(i) + ": " + rs.getString(i) + "\t");
                }
                System.out.println(); 
            }

            } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    
}
