package ir.ac.kntu.helper.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.BookDAO;
import ir.ac.kntu.dao.ProductDAO;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.model.User;

public class SearchProductController {

    public static void HandleSearchProduct(User user) {
        while (true) {
            Menu.productCategoryMenu();
            Vendilo.Product insertProduct = Menu.getProductCategory();
            switch (insertProduct) {
                case MOBILE -> {
                    searchMobile(user);

                }
                case LAPTOP -> {
                    searchLaptop(user);

                }
                case BOOK -> {
                    searchBook(user);

                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\n");

                }
                default -> throw new AssertionError();
            }
        }
    }

    public static void searchBook(User user) {
        while (true) {
            Menu.searchBookMenu();
            Vendilo.SearchBookOption option = Menu.getSearchBookOption();
            switch (option) {
                case SHOW_ALL -> {
                    ProductDAO.showAllProducts("Books");
                    ProductController.handleAddProductToList("Books", user);
                }

                case SEARCH_BY_PRICE -> {
                    System.out.print("Enter the min price: ");
                    double min = ScannerWrapper.getInstance().nextDouble();
                    System.out.print("Enter the max price: ");
                    double max = ScannerWrapper.getInstance().nextDouble();
                    ProductDAO.searchByPrice("Books", min, max);
                    ProductController.handleAddProductToList("Books", user);
                }

                case SEARCH_BY_TITLE -> {
                    System.out.println("Enter book title: ");
                    String title = ScannerWrapper.getInstance().nextLine();
                    BookDAO.searchBookByTitle("Books", title);
                    ProductController.handleAddProductToList("Books", user);
                }

                case BACK -> {
                    return;
                }

                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\n");
                    break;
                }
                default -> throw new AssertionError();
            }
        }
    }

    public static void searchLaptop(User user) {
        Menu.searchMenu();
        Vendilo.SearchMenu option = Menu.getSearchMenuOption();

        switch (option) {
            case SHOW_ALL -> {
                ProductDAO.showAllProducts("Laptop");
                ProductController.handleAddProductToList("Laptop", user);
            }
            case SEARCH_BY_PRICE -> {
                System.out.print("Enter the min price: ");
                double min = ScannerWrapper.getInstance().nextDouble();
                System.out.print("Enter the max price: ");
                double max = ScannerWrapper.getInstance().nextDouble();
                ProductDAO.searchByPrice("Laptop", min, max);
                ProductController.handleAddProductToList("Laptop", user);
            }
            case BACK -> {
                return;
            }
            case UNDEFINED -> {
                System.out.println("Undefined Choice; Try again...\n");
                break;
            }
            default -> throw new AssertionError();
        }
    }

    public static void searchMobile(User user) {
        Menu.searchMenu();
        Vendilo.SearchMenu option = Menu.getSearchMenuOption();

        switch (option) {
            case SHOW_ALL -> {
                ProductDAO.showAllProducts("Mobile");
                ProductController.handleAddProductToList("Mobile", user);
            }
            case SEARCH_BY_PRICE -> {
                System.out.print("Enter the min price: ");
                double min = ScannerWrapper.getInstance().nextDouble();
                System.out.print("Enter the max price: ");
                double max = ScannerWrapper.getInstance().nextDouble();
                ProductDAO.searchByPrice("Mobile", min, max);
                ProductController.handleAddProductToList("Mobile", user);
            }
            case BACK -> {
                return;
            }
            case UNDEFINED -> {
                System.out.println("Undefined Choice; Try again...\n");
                break;
            }
            default -> throw new AssertionError();
        }
    }

    public static void reduceInventory(User user) {
        Connection conn = null;
        PreparedStatement getUserIdStmt = null;
        PreparedStatement getCartStmt = null;
        PreparedStatement getInvStmt = null;
        PreparedStatement updateStmt = null;
    
        ResultSet userRs = null;
        ResultSet cartRs = null;
        ResultSet invRs = null;
    
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:your_database_file.db");
            conn.setAutoCommit(false); // Start transaction
    
            // Get user ID from email
            String getUserIdQuery = "SELECT id FROM users WHERE email = ?";
            getUserIdStmt = conn.prepareStatement(getUserIdQuery);
            getUserIdStmt.setString(1, user.getEmail());
            userRs = getUserIdStmt.executeQuery();
    
            if (!userRs.next()) {
                System.out.println("User not found.");
                return;
            }
    
            int userId = userRs.getInt("id");
    
            // Get products from shopping cart
            String getCartQuery = "SELECT product_id, name FROM shoppingCart WHERE user_id = ?";
            getCartStmt = conn.prepareStatement(getCartQuery);
            getCartStmt.setInt(1, userId);
            cartRs = getCartStmt.executeQuery();
    
            while (cartRs.next()) {
                int productId = cartRs.getInt("product_id");
                String tableName = cartRs.getString("name");
    
               
    
                String getInventoryQuery = "SELECT inventory FROM " + tableName + " WHERE product_id = ?";
                getInvStmt = conn.prepareStatement(getInventoryQuery);
                getInvStmt.setInt(1, productId);
                invRs = getInvStmt.executeQuery();
    
                if (invRs.next()) {
                    int inventory = invRs.getInt("inventory");
    
                    if (inventory > 0) {
                        // Reduce inventory by 1
                        String updateInventoryQuery = "UPDATE " + tableName + " SET inventory = ? WHERE product_id = ?";
                        updateStmt = conn.prepareStatement(updateInventoryQuery);
                        updateStmt.setInt(1, inventory - 1);
                        updateStmt.setInt(2, productId);
                        updateStmt.executeUpdate();
                    } else {
                        System.out.println("Product ID " + productId + " is out of stock.");
                    }
                }
    
                if (invRs != null) invRs.close();
                if (getInvStmt != null) getInvStmt.close();
                if (updateStmt != null) updateStmt.close();
            }
    
            conn.commit(); // Finish transaction
    
        } catch (SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
            try {
                if (conn != null) conn.rollback(); // Undo changes
            } catch (SQLException rollbackEx) {
                System.out.println("Rollback failed: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                if (userRs != null) userRs.close();
                if (cartRs != null) cartRs.close();
                if (getUserIdStmt != null) getUserIdStmt.close();
                if (getCartStmt != null) getCartStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Cleanup error: " + e.getMessage());
            }
        }
    }
}
  