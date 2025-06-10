package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.controllers.NotificationController;
import ir.ac.kntu.model.ShoppingCart;
import ir.ac.kntu.model.User;

public class ProductDAO {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void showAllProducts(String tableName, User user) {
        String query = "SELECT id, name, price, inventory FROM " + tableName;

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet resultSet = stmt.executeQuery(query)) {

            while (resultSet.next()) {
                int productId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int inventory = resultSet.getInt("inventory");
                System.out.println("");
                if (VendiloPlusDAO.vendiloPlusUser(user)) {
                    price = 0.95 * price;
                }
                System.out.printf("Product name: %s, ID: %d, Price: %.2f, Inventory: %d%n",
                        name, productId, price, inventory);
            }

        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    public static void searchByPrice(String tableName, double min, double max, User user) {
        String query = "SELECT id, name, price, inventory FROM " + tableName + " WHERE price BETWEEN ? AND ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {
            if (VendiloPlusDAO.vendiloPlusUser(user)) {
                min = (100 / 95) * min;
                max = (100 / 95) * max;
            }
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
                    System.out.println("");
                    if (VendiloPlusDAO.vendiloPlusUser(user)) {
                        price = 0.95 * price;
                    }
                    System.out.printf("Product name: %s, ID: %d, Price: %.2f, Inventory: %d%n",
                            name, productId, price, inventory);
                }

                if (!found) {
                    System.out.println(ConsoleColors.RED + "No products found in that range." + ConsoleColors.RESET);
                }
            }

        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
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
                    information.append("\n");
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
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
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
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
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
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        }

        return -1;
    }

    public static void printSellerProducts(String agencyCode) {
        String query = "SELECT id, name, inventory FROM Book WHERE agency_code = ? " +
                "UNION ALL " +
                "SELECT id, name, inventory FROM Mobile WHERE agency_code = ? " +
                "UNION ALL " +
                "SELECT id, name, inventory FROM Laptop WHERE agency_code = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, agencyCode);
            stmt.setString(2, agencyCode);
            stmt.setString(3, agencyCode);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int inventory = rs.getInt("inventory");

                System.out.println("ID: " + id + ", Name: " + name + ", Inventory: " + inventory);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setInventory(String tableName, int productId, int inventory) {
        String query = "UPDATE " + tableName + " SET inventory = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, inventory);
            stmt.setInt(2, productId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Inventory updated successfully.");
                NotificationController.handleSendingNOtif(tableName, productId, inventory);
            } else {
                System.out.println("No product found with the given ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int findInventory(int productId, String productName) {
        int inventory = -1;
        String query = "SELECT inventory FROM " + productName + " WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                inventory = rs.getInt("inventory");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inventory;
    }

}
