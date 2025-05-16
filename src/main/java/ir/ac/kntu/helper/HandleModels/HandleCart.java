package ir.ac.kntu.helper.HandleModels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ir.ac.kntu.model.User;

public class HandleCart {
    private static final String DB_URL = "jdbc:sqlite:data.db";   


    public static void printAllOptionsInCart(User user) {
            String query1 = "SELECT id FROM users WHERE email = ?";
            String query2 = "SELECT * FROM shoppingCart WHERE user_id = ?";
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
        
                    boolean hasItems = false;
                    while (rs2.next()) {
                        double price = rs2.getDouble("price");
                        sum += price;
                        hasItems = true;
                        System.out.println("price: " + price);
                        System.out.println("Product Name: " + rs2.getString("information") + "\n");
                        
                    }
                    System.out.println("You must pay: " + sum);
        
                    if (!hasItems) {
                        System.out.println("Shopping cart is empty.");
                    }
        
                } else {
                    System.out.println("No user found with email: " + user.getEmail());
                }
        
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Database error: " + e.getMessage());
            }
        }

    
}
