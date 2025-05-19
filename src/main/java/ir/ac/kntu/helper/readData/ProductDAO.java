package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.model.ShoppingCart;

public class ProductDAO {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void showAllProducts(String tableName) {
        String query = "SELECT id, name, price, inventory FROM " + tableName;
    
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int inventory = rs.getInt("inventory");
    
                System.out.printf("Product name: %s, ID: %d, Price: %.2f, Inventory: %d%n",
                        name, id, price, inventory);
            }
    
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    

    public static void searchByPrice(String tableName, double min, double max) {
        String query = "SELECT id, name, price, inventory FROM " + tableName + " WHERE price BETWEEN ? AND ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, min);
            stmt.setDouble(2, max);
            ResultSet rs = stmt.executeQuery();

            

            boolean found = false;
            while (rs.next()) {
                found = true;
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int inventory = rs.getInt("inventory");
    
                System.out.printf("Product name: %s, ID: %d, Price: %.2f, Inventory: %d%n",
                        name, id, price, inventory);
                
            }

            if (!found) {
                System.out.println("No products found in that range.");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static ShoppingCart makeShoppingCartObject(int productId,int sellerId, String tableName) {
        double price;
        String query = "SELECT * FROM " + tableName + " WHERE id = ?";
        StringBuilder information = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            if (rs.next()) {
                
                price = rs.getDouble("price");

                for (int i = 1; i <= columnCount; i++) {
                    information.append(meta.getColumnName(i))
                          .append(": ")
                          .append(rs.getString(i))
                          .append("\n");
                }
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null ;
        }

        return new ShoppingCart(price, information.toString(), sellerId, productId);
    }

    public static double getPriceById(int id, String tableName) {
        String query = "SELECT price FROM " + tableName + " WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("price");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return -1;
    }

    public static int findSellerId(int productId, String productType) {
        String query = "SELECT seller_id FROM " + productType + " WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("seller_id");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return -1;

    }

   
}
