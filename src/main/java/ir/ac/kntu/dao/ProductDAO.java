package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.model.ShoppingCart;

public class ProductDAO {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void showAllProducts(String tableName) {
        String query = "SELECT id, name, price, inventory FROM " + tableName;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {

            while (resultSet.next()) {
                int productId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int inventory = resultSet.getInt("inventory");

                System.out.printf("Product name: %s, ID: %d, Price: %.2f, Inventory: %d%n",
                        name, productId, price, inventory);
            }

        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED +"Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    public static void searchByPrice(String tableName, double min, double max) {
        String query = "SELECT id, name, price, inventory FROM " + tableName + " WHERE price BETWEEN ? AND ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, min);
            stmt.setDouble(2, max);
            try (ResultSet resultSet = stmt.executeQuery()) {
                boolean found = false;
                while (resultSet.next()) {
                    found = true;
                    int productId = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");
                    int inventory = resultSet.getInt("inventory");

                    System.out.printf("Product name: %s, ID: %d, Price: %.2f, Inventory: %d%n",
                            name, productId, price, inventory);
                }

                if (!found) {
                    System.out.println(ConsoleColors.RED +"No products found in that range."+ ConsoleColors.RESET);
                }
            }

        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED +"Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    public static ShoppingCart makeShoppingCartObject(int productId, int sellerId, String tableName) {
        double price;
        String name;
        String query = "SELECT * FROM " + tableName + " WHERE id = ?";
        StringBuilder information = new StringBuilder();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    price = resultSet.getDouble("price");
                    name = resultSet.getString("name");

                    ResultSetMetaData meta = resultSet.getMetaData();
                    int columnCount = meta.getColumnCount();

                    for (int i = 1; i <= columnCount; i++) {
                        information.append(meta.getColumnName(i))
                                .append(": ")
                                .append(resultSet.getString(i))
                                .append("\n");
                    }
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED +"Error: " + e.getMessage() + ConsoleColors.RESET);
            return null;
        }

        return new ShoppingCart(name, price, information.toString(), sellerId, productId);
    }

    public static double getPriceById(int productId, String tableName) {
        String query = "SELECT price FROM " + tableName + " WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("price");
                }
            }

        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED +"Error: " + e.getMessage() + ConsoleColors.RESET);
        }

        return -1;
    }

    public static int findSellerId(int productId, String productType) {
        String query = "SELECT seller_id FROM " + productType + " WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("seller_id");
                }
            }

        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage()+ ConsoleColors.RESET);
        }

        return -1;
    }
}
