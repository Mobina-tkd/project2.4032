package ir.ac.kntu.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.model.User;

public class SearchProduct {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    
    public static void searchBook(User user) {
        while (true) { 
            Menu.searchBookMenu();
            Vendilo.SearchBookOption option = Menu.getSearchBookOption();
            switch (option) {
                case SHOW_ALL-> {
                    ProductUtils.showAllProducts("Books");
                    ProductUtils.addProductToList("Books", user);
                }


                case SEARCH_BY_PRICE-> {
                    System.out.println("Enter the min price: ");
                    double min = ScannerWrapper.getInstance().nextDouble();
                    System.out.println("Enter the max price: ");
                    double max = ScannerWrapper.getInstance().nextDouble();
                    ProductUtils.searchByPrice("Books", min, max);
                    ProductUtils.addProductToList("Books", user);                
                }

                case SEARCH_BY_TITLE -> {
                    System.out.println("Enter book title: ");
                    String title = ScannerWrapper.getInstance().nextLine();
                    searchBookByTitle("Books", title);
                    ProductUtils.addProductToList("Books", user);              
                }

                case BACK-> {
                    return;
                }

                case UNDEFINED-> {
                    System.out.println("Undefined Choice; Try again...\n");
                    break;
                }
            }
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

    public static void searchLaptop(User user) {
        Menu.searchMenu();
        Vendilo.SearchMenu option = Menu.getSearchMenuOption();

        switch (option) {
            case SHOW_ALL-> {
                ProductUtils.showAllProducts("Laptop");
                ProductUtils.addProductToList("Laptop", user);              
            }
            case SEARCH_BY_PRICE-> {
                System.out.println("Enter the min price: ");
                double min = ScannerWrapper.getInstance().nextDouble();
                System.out.println("Enter the max price: ");
                double max = ScannerWrapper.getInstance().nextDouble();
                ProductUtils.searchByPrice("Laptop", min, max);
                ProductUtils.addProductToList("Laptop", user);              
            }
            case BACK-> {
                return;
            }
            case UNDEFINED-> {
                System.out.println("Undefined Choice; Try again...\n");
                break;
            }
        }
        
    }


    public static void searchMobile(User user) {
        Menu.searchMenu();
        Vendilo.SearchMenu option = Menu.getSearchMenuOption();

        switch (option) {
            case SHOW_ALL-> {
                ProductUtils.showAllProducts("Mobile");
                ProductUtils.addProductToList("Mobile",user);              
            }
            case SEARCH_BY_PRICE-> {
                System.out.println("Enter the min price: ");
                double min = ScannerWrapper.getInstance().nextDouble();
                System.out.println("Enter the max price: ");
                double max = ScannerWrapper.getInstance().nextDouble();
                ProductUtils.searchByPrice( "Mobile", min, max);
                ProductUtils.addProductToList("Mobile", user);              
            }
            case BACK -> {
                return ;
            }
            case UNDEFINED-> {
                System.out.println("Undefined Choice; Try again...\n");
                break;
            }
        }
    }

}
