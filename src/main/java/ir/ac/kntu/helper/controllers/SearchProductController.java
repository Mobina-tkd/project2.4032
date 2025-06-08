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
import ir.ac.kntu.dao.UserDAO;
import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.model.User;

public class SearchProductController {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void handleSearchProduct(User user) {
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
                    System.out.println(ConsoleColors.RED + "Undefined Choice; Try again...\n" + ConsoleColors.RESET);

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
                    ProductDAO.showAllProducts("Book", user);
                    ProductController.handleAddProductToList("Book", user);
                }

                case SEARCH_BY_PRICE -> {
                    System.out.print("Enter the min price: ");
                    double min = ScannerWrapper.getInstance().nextDouble();
                    System.out.print("Enter the max price: ");
                    double max = ScannerWrapper.getInstance().nextDouble();
                    ProductDAO.searchByPrice("Book", min, max, user);
                    ProductController.handleAddProductToList("Book", user);
                }

                case SEARCH_BY_TITLE -> {
                    System.out.println("Enter book title: ");
                    String title = ScannerWrapper.getInstance().nextLine();
                    BookDAO.searchBookByTitle("Book", title, user);
                    ProductController.handleAddProductToList("Book", user);
                }

                case BACK -> {
                    return;
                }

                case UNDEFINED -> {
                    System.out.println(ConsoleColors.RED + "Undefined Choice; Try again...\n" + ConsoleColors.RESET);
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
                ProductDAO.showAllProducts("Laptop", user);
                ProductController.handleAddProductToList("Laptop", user);
            }
            case SEARCH_BY_PRICE -> {
                System.out.print("Enter the min price: ");
                double min = ScannerWrapper.getInstance().nextDouble();
                System.out.print("Enter the max price: ");
                double max = ScannerWrapper.getInstance().nextDouble();
                ProductDAO.searchByPrice("Laptop", min, max, user);
                ProductController.handleAddProductToList("Laptop", user);
            }
            case BACK -> {
                return;
            }
            case UNDEFINED -> {
                System.out.println(ConsoleColors.RED + "Undefined Choice; Try again...\n" + ConsoleColors.RESET);
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
                ProductDAO.showAllProducts("Mobile", user);
                ProductController.handleAddProductToList("Mobile", user);
            }
            case SEARCH_BY_PRICE -> {
                System.out.print("Enter the min price: ");
                double min = ScannerWrapper.getInstance().nextDouble();
                System.out.print("Enter the max price: ");
                double max = ScannerWrapper.getInstance().nextDouble();
                ProductDAO.searchByPrice("Mobile", min, max, user);
                ProductController.handleAddProductToList("Mobile", user);
            }
            case BACK -> {
                return;
            }
            case UNDEFINED -> {
                System.out.println(ConsoleColors.RED + "Undefined Choice; Try again...\n" + ConsoleColors.RESET);
                break;
            }
            default -> throw new AssertionError();
        }
    }

    public static void reduceInventory(User user) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(false);

            int userId = UserDAO.findUserId(user.getEmail());
            if (userId == -1) {
                System.out.println(ConsoleColors.RED + "User not found." + ConsoleColors.RESET);
                return;
            }

            processCartAndReduceInventory(conn, userId);

            conn.commit(); // Finish transaction
        } catch (SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.out.println("Rollback failed: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Cleanup error: " + e.getMessage());
            }
        }
    }


    private static void processCartAndReduceInventory(Connection conn, int userId) throws SQLException {
        PreparedStatement getCartStmt = null;
        ResultSet cartRs = null;

        try {
            String getCartQuery = "SELECT product_id, name FROM shoppingCart WHERE user_id = ?";
            getCartStmt = conn.prepareStatement(getCartQuery);
            getCartStmt.setInt(1, userId);
            cartRs = getCartStmt.executeQuery();

            while (cartRs.next()) {
                int productId = cartRs.getInt("product_id");
                String tableName = cartRs.getString("name");
                reduceInventoryForProduct(conn, productId, tableName);
            }
        } finally {
            if (cartRs != null) {
                cartRs.close();
            }
            if (getCartStmt != null) {
                getCartStmt.close();
            }
        }
    }

    private static void reduceInventoryForProduct(Connection conn, int productId, String tableName)
            throws SQLException {
        PreparedStatement getInvStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet invRs = null;

        try {
            String getInventoryQuery = "SELECT inventory FROM " + tableName + " WHERE id = ?";
            getInvStmt = conn.prepareStatement(getInventoryQuery);
            getInvStmt.setInt(1, productId);
            invRs = getInvStmt.executeQuery();

            if (invRs.next()) {
                int inventory = invRs.getInt("inventory");

                if (inventory > 0) {
                    String query = "UPDATE " + tableName + " SET inventory = ? WHERE id = ?";
                    updateStmt = conn.prepareStatement(query);
                    updateStmt.setInt(1, inventory - 1);
                    updateStmt.setInt(2, productId);
                    updateStmt.executeUpdate();
                } else {
                    System.out.println("Product ID " + productId + " is out of stock.");
                }
            }
        } finally {
            if (invRs != null) {
                invRs.close();
            }
            if (getInvStmt != null) {
                getInvStmt.close();
            }
            if (updateStmt != null) {
                updateStmt.close();
            }
        }
    }

}
