package ir.ac.kntu.helper;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.ShoppingCartDAO;
import ir.ac.kntu.model.ShoppingCart;
import ir.ac.kntu.model.User;


public class ProductUtils {
    private static final String DB_URL = "jdbc:sqlite:data.db";   

    
    

    public static void showAllProducts(String name) {
        String tableName = name;

        String query = "SELECT * FROM " + tableName;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Get number of columns
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            // skip printing name
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

    public static void searchByPrice(String name, double min, double max) {
        String query = "SELECT * FROM " + name + " WHERE price BETWEEN ? AND ?";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setDouble(1, min);
        pstmt.setDouble(2, max);

        ResultSet rs = pstmt.executeQuery();

        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        boolean found = false;
        while (rs.next()) {
            found = true;
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(meta.getColumnName(i) + ": " + rs.getString(i) + "\t");
            }
            System.out.println();
        }

        if (!found) {
            System.out.println("No products found in the price range " + min + " to " + max);
        }

    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }

    }


    public static void addProductToList(String productType, User user) {
        while (true) { 
            Menu.addToListMenu();
            Vendilo.AddToList option = Menu.getAddToListOption();
            switch (option) {
                case ADD -> {
                    chooseProduct(productType, user);
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\n");
                }       
            }
        }
    }   
    
    
    public static void chooseProduct(String productType, User user) {
        
        while (true) { 
            System.out.println("Enter the id of the product you want to add to shopping cart: ");
            int id = ScannerWrapper.getInstance().nextInt();         
            boolean added = addProductToShoppingCart(id, productType, user); //might product be soled out
            if(added) {
                break;
            }else {
                System.out.println("The product didnt add to you shopping cart :( please try again...");

            }

        }
    }




    public static boolean addProductToShoppingCart(int id, String productType, User user) {
        String information = makeInformation(id, productType);
        double price = getProductPrice(id, productType);
        ShoppingCart shoppingCart = new ShoppingCart(price, information);
        boolean inserted = ShoppingCartDAO.insertToShoppingCart(shoppingCart, user);
        if(inserted) {
            return true;
        }
        return false;
    }


    private static String makeInformation(int id, String productType) {
        String sql = "SELECT * FROM " + productType + " WHERE id = ?";

      StringBuilder result = new StringBuilder();
    
            try (
                Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(sql)
            ) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
    
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
    
                if (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        String columnValue = rs.getString(i);
                        result.append(columnName).append(": ").append(columnValue).append("\n");
                    }
                } else {
                    return "No record found with id = " + id;
                }
    
            } catch (SQLException e) {
                e.printStackTrace();
                return "Database error: " + e.getMessage();
            }
    
            return result.toString();
        }


        private static double  getProductPrice(int id, String productType) {
            String sql = "SELECT price FROM " + productType + " WHERE id = ?";

            try (
                Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(sql)
            ) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return rs.getDouble("price");
                } else {
                    System.err.println("No record found with id = " + id);
                    return -1;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return -1;
            }
        }


        
        
        







    }

