package ir.ac.kntu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class BookDAO {
    private static final String DB_URL = "jdbc:sqlite:data.db";


    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS books ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "name TEXT NOT NULL,"
            + "price REAL NOT NULL,"
            + "inventory INTEGER NOT NULL,"
            + "writerName TEXT NOT NULL,"
            + "pageNumber INTEGER NOT NULL,"
            + "genre TEXT NOT NULL,"
            + "ageGroup TEXT NOT NULL,"
            + "ISBN TEXT NOT NULL"
            + ");";
    
        try (Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

    public static boolean insertBook(Book book) {
        String sql = "INSERT INTO books(name, price, inventory, writerName, pageNumber, genre, ageGroup, ISBN)"
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, book.getName());
            pstmt.setDouble(2, book.getPrice());
            pstmt.setInt(3, book.getInventory());
            pstmt.setString(4, book.getWriterName());
            pstmt.setInt(5, book.getPageNumber());
            pstmt.setString(6, book.getGenre());
            pstmt.setString(7, book.getAgeGroup());
            pstmt.setString(8, book.getISBN());
    
            pstmt.executeUpdate();
            System.out.println("book inserted successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
    }
    
}
