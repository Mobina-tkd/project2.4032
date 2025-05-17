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
            + "name TEXT NOT NULL,"
            + "title TEXT NOT NULL,"
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
        String sql = "INSERT INTO books(name, title, price, inventory, writerName, pageNumber, genre, ageGroup, ISBN)"
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, "Book");
            pstmt.setString(2, book.getTitle());
            pstmt.setDouble(3, book.getPrice());
            pstmt.setInt(4, book.getInventory());
            pstmt.setString(5, book.getWriterName());
            pstmt.setInt(6, book.getPageNumber());
            pstmt.setString(7, book.getGenre());
            pstmt.setString(8, book.getAgeGroup());
            pstmt.setString(9, book.getISBN());
    
            pstmt.executeUpdate();
            System.out.println("Book inserted successfully.");
            return true;
        } catch (SQLException e) {
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
