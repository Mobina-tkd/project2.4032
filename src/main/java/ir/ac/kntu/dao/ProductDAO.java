package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductDAO {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void showAllProducts(String tableName) {
        String query = "SELECT * FROM " + tableName;
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

    public static void searchByPrice(String tableName, double min, double max) {
        String query = "SELECT * FROM " + tableName + " WHERE price BETWEEN ? AND ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, min);
            stmt.setDouble(2, max);
            ResultSet rs = stmt.executeQuery();

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
                System.out.println("No products found in that range.");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static String getProductInfoById(int id, String tableName) {
        String query = "SELECT * FROM " + tableName + " WHERE id = ?";
        StringBuilder result = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            if (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    result.append(meta.getColumnName(i))
                          .append(": ")
                          .append(rs.getString(i))
                          .append("\n");
                }
            } else {
                return null;
            }

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }

        return result.toString();
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
}
