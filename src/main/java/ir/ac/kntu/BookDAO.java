package ir.ac.kntu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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

    
    public static Book readData() {

        String name = "Book";
        
        System.out.print("Enter book title: ");
        String title = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter price: ");
        double price = ScannerWrapper.getInstance().nextDouble();

        System.out.print("Enter inventory count: ");
        int inventory = ScannerWrapper.getInstance().nextInt();

        System.out.print("Enter writer's name: ");
        String writerName = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter page number: ");
        int pageNumber = ScannerWrapper.getInstance().nextInt();

        System.out.print("Enter genre: ");
        String genre = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter age group: ");
        String ageGroup = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter ISBN: ");
        String isbn = ScannerWrapper.getInstance().nextLine();

        return new Book(title, name, price, inventory, writerName, pageNumber, genre, ageGroup, isbn);
        
    }
    
}
