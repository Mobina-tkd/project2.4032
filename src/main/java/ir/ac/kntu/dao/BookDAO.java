package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.model.Book;

public class BookDAO {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Book ("
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
        String sql = "INSERT INTO Book(seller_id, name, title, price, inventory, writerName, pageNumber, genre, ageGroup, ISBN) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement selectStmt = conn.prepareStatement(query)
        ) {
            selectStmt.setString(1, agencyCode);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                if (resultSet.next()) {
                    int sellerId = resultSet.getInt("id");
    
                    try (PreparedStatement insertStmt = conn.prepareStatement(sql)) {
                        insertStmt.setInt(1, sellerId);
                        insertStmt.setString(2, "Book");
                        insertStmt.setString(3, book.getTitle());
                        insertStmt.setDouble(4, book.getPrice());
                        insertStmt.setInt(5, book.getInventory());
                        insertStmt.setString(6, book.getWriterName());
                        insertStmt.setInt(7, book.getPageNumber());
                        insertStmt.setString(8, book.getGenre());
                        insertStmt.setString(9, book.getAgeGroup());
                        insertStmt.setString(10, book.getIsbn());
    
                        insertStmt.executeUpdate();
                        System.out.println(ConsoleColors.GREEN +"Book inserted successfully." + ConsoleColors.RESET);
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED +"Insert failed: " + e.getMessage() + ConsoleColors.RESET);
        }
    
        return false;
    }
    

    public static void searchBookByTitle(String tableName, String title) {
        String query = "SELECT * FROM " + tableName + " WHERE title = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, title);
            try (ResultSet resultSet = stmt.executeQuery()) {
                ResultSetMetaData meta = resultSet.getMetaData();
                int columnCount = meta.getColumnCount();

                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(meta.getColumnName(i) + ": " + resultSet.getString(i) + "\t");
                    }
                    System.out.println();
                }
            }

        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED +"Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }
}
