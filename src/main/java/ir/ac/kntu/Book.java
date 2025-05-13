package ir.ac.kntu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Book extends Product {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    String title;
    String writerName;
    int pageNumber;
    String genre;
    String ageGroup;
    String ISBN;

    public Book(String name, String title, double price, int inventory, String writerName, int pageNumber, String genre, String ageGroup, String ISBN) {
        this.name = name;
        this.title = title;
        this.price = price;
        this.inventory = inventory;
        this.writerName = writerName;
        this.pageNumber = pageNumber;
        this.genre = genre;
        this.ageGroup = ageGroup;
        this.ISBN = ISBN;
    }

    public Book() {}

    public String getTitle() {
        return title;
    }

    public String getWriterName() {
        return writerName;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getGenre() {
        return genre;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public String getISBN() {
        return ISBN;
    }


    public Book readData() {
        String name = "Book";

        System.out.print("Enter title: ");
        String title = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter price: ");
        double price = ScannerWrapper.getInstance().nextDouble();

        System.out.print("Enter inventory: ");
        int inventory = ScannerWrapper.getInstance().nextInt();
        ScannerWrapper.getInstance().nextLine(); // Consume leftover newline

        System.out.print("Enter writer name: ");
        String writerName = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter page number: ");
        int pageNumber = ScannerWrapper.getInstance().nextInt();
        ScannerWrapper.getInstance().nextLine(); // Consume leftover newline

        System.out.print("Enter genre: ");
        String genre = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter age group (ADULT, TEENAGER, CHILDREN): ");
        String ageGroup = ScannerWrapper.getInstance().nextLine();
        System.out.print("Enter ISBN: ");
        String ISBN = ScannerWrapper.getInstance().nextLine();

        return new Book(name, title, price, inventory, writerName, pageNumber, genre, ageGroup, ISBN);
    }

    public static void search() {
        Menu.searchMenu();
        Vendilo.SearchBookOption option = Menu.getSearchBookOption();

        switch (option) {
            case SHOW_ALL-> {
                Utils.showAllProducts("Books");
            }


            case SEARCH_BY_PRICE-> {
                System.out.println("Enter the min price: ");
                double min = ScannerWrapper.getInstance().nextDouble();
                System.out.println("Enter the max price: ");
                double max = ScannerWrapper.getInstance().nextDouble();
                Utils.searchByPrice("Laptop", min, max);
            }

            case SEARCH_BY_TITLE -> {
                System.out.println("Enter book title: ");
                String title = ScannerWrapper.getInstance().nextLine();
                searchByTitle("Books", title);

            }

            case UNDEFINED-> {
                System.out.println("Undefined Choice; Try again...\n");
                break;
            }
        }
        
    }

    public static void searchByTitle(String tableName, String title) {

        String query = "SELECT * FROM " + tableName + " WHERE title = " + "\'" + title + "\'";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()) {
                for (int i = 2; i <= columnCount; i++) {
                    System.out.print(meta.getColumnName(i) + ": " + rs.getString(i) + "\t");
                }
                System.out.println(); 
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
