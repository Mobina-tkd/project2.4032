package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ir.ac.kntu.model.ShoppingCart;
import ir.ac.kntu.model.User;



public class ShoppingCartDAO {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS shoppingCart ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "user_id INTEGER NOT NULL,"
                        + "seller_id INTEGER NOT NULL,"
                        + "name TEXT NOT NULL,"
                        + "information TEXT NOT NULL,"  
                        + "price REAL NOT NULL,"
                        + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,"
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

    public static boolean insertToShoppingCart(ShoppingCart shoppingCart, User user) {
        String sqlSelectUserId = "SELECT id FROM users WHERE email = ?";
        String sqlInsertAddress = "INSERT INTO shoppingCart(user_id, seller_id,name, information, price) "
                                + "VALUES (?, ?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement selectStmt = conn.prepareStatement(sqlSelectUserId);
             ) {
    
            selectStmt.setString(1, user.getEmail());

            var rs = selectStmt.executeQuery();
    
            if (rs.next()) {
                int userId = rs.getInt("id");
    
                try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsertAddress)) {
                    insertStmt.setInt(1, userId);
                    insertStmt.setInt(2, shoppingCart.getSellerId());
                    insertStmt.setString(3, shoppingCart.getName());
                    insertStmt.setString(3, shoppingCart.getInformation());
                    insertStmt.setDouble(4, shoppingCart.getPrice());

                    insertStmt.executeUpdate();
                    System.out.println("Product inserted successfully.");
                    return true;
                }
    
            } else {
                System.out.println("User not found with email: " + user.getEmail());
                return false;
            }
    
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
            return false;
        }
    }


    public static void deleteProductFromCart(int id) {
        String query = "DELETE FROM shoppingCart WHERE id = ?";
    
        try (
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
    
            if (affectedRows > 0) {
                System.out.println("Product with ID " + id + " was deleted from the cart.");
            } else {
                System.out.println("No product found in the cart with ID " + id + ".");
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void printAllOptionsInCart(User user) {
        String query1 = "SELECT id FROM users WHERE email = ?";
        String query2 = "SELECT name, id, price FROM shoppingCart WHERE user_id = ?";
        double sum = 0;
    
        try (
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement stmt1 = conn.prepareStatement(query1);
            PreparedStatement stmt2 = conn.prepareStatement(query2)
        ) {
            // Get user ID from email
            stmt1.setString(1, user.getEmail());
            ResultSet rs1 = stmt1.executeQuery();
    
            if (rs1.next()) {
                int userId = rs1.getInt("id");
    
                // Get shopping cart items
                stmt2.setInt(1, userId);
                ResultSet rs2 = stmt2.executeQuery();
    
                System.out.println("Items in shopping cart for user: " + user.getEmail());
                System.out.println("--------------------------------------------------");
    
                while (rs2.next()) {
                    String name = rs2.getString("name");
                    int id = rs2.getInt("id");
                    double price = rs2.getDouble("price");
    
                    System.out.printf("Name: %s | ID: %d | Price: $%.2f%n", name, id, price);
                    sum += price;
                }
    
                System.out.println("--------------------------------------------------");
                System.out.printf("Total Price: $%.2f%n", sum);
            } else {
                System.out.println("User not found.");
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

        public static void clearShoppingCart(User user) {
            String sql1 = "SELECT id FROM users WHERE email = ?";
            String sql2 = "DELETE FROM shoppingCart WHERE user_id = ?";
        
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement ps1 = conn.prepareStatement(sql1);
                 PreparedStatement ps2 = conn.prepareStatement(sql2)) {
        
                ps1.setString(1, user.getEmail());
                ResultSet rs = ps1.executeQuery();
        
                if (rs.next()) {
                    int userId = rs.getInt("id");
        
                    ps2.setInt(1, userId);
                    ps2.executeUpdate();
        
                    System.out.println("Shopping cart cleared for user.");
                } else {
                    System.out.println("User not found.");
                }
        
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    public static void printInfoById(int id) {
        String query = "SELECT name, price, information FROM shoppingCart WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String information = rs.getString("information");
                System.out.println("Name: " + name);
                System.out.println("Price: " + price);
                System.out.println("Information: " + information);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
