package ir.ac.kntu.helper.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.AddressDAO;
import ir.ac.kntu.dao.ProductDAO;
import ir.ac.kntu.dao.PurchasesDAO;
import ir.ac.kntu.dao.SellerDAO;
import ir.ac.kntu.dao.ShoppingCartDAO;
import ir.ac.kntu.dao.TransactionDAO;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.helper.readData.ReadAddress;
import ir.ac.kntu.model.Address;
import ir.ac.kntu.model.ShoppingCart;
import ir.ac.kntu.model.Transaction;
import ir.ac.kntu.model.User;

public class ShoppingCartController {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void handleShoppingCart(User user) {
        while (true) {
            ShoppingCartDAO.printAllOptionsInCart(user);
            Menu.buyOrdeleteFromCartMenu();
            Vendilo.DeleteFromCart option = Menu.getBuyOrDeleteFromListOption();

            switch (option) {
                case DELETE -> {
                    handleDeleting();
                }
                case BUY -> {
                    handleBuying(user);
                    return;
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

    private static void handleDeleting() {
        int productId;
        while (true) {
            System.out.print("Enter the id of product you want to delete: ");
            productId = ScannerWrapper.getInstance().nextInt();
            ShoppingCartDAO.printInfoById(productId);
            System.out.print("Are you sure you want to delete this product? Y/N: ");
            String delete = ScannerWrapper.getInstance().nextLine();
            if ("y".equalsIgnoreCase(delete)) {
                ShoppingCartDAO.deleteProductFromCart(productId);
            } else if ("n".equalsIgnoreCase(delete)) {
                return;
            } else {
                System.out.println("Invalid choise :( please try again...");
            }
        }
    }

    private static void handleBuying(User user) {
        while (true) { 
            AddressDAO.printAllAddresses(user);
            Menu.chooseAddressMenu();
            Vendilo.ChooseAddress option = Menu.getChooseOrAddAddress();
            switch (option) {
                case CHOOSE -> {
                    System.out.print("Enter the number of the address: ");
                    int addressId = ScannerWrapper.getInstance().nextInt();
                    Address address = AddressDAO.findAddress(addressId);
                    double totalCost = countTotalCost(user, address.getState());
                    if (address == null) {
                        continue;
                    }
                    System.out.println("Total cost including shipping cost:  " + totalCost);
                    handlePaying(user, totalCost, address);


                }

                case NEW -> {
                    Address address = ReadAddress.readAddress();
                    AddressDAO.insertAddress(address, user);
                    String state = address.getState();
                    double totalCost = countTotalCost(user, state);
                    System.out.println("Total cost including shipping cost:  " + totalCost);
                    handlePaying(user, totalCost, address);
                    return;
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

    private static void handlePaying(User user, double balance, Address address) {
        while (true) {
            Menu.payMenu();
            Vendilo.PayMenu choise = Menu.getPayOption();
            switch (choise) {
                case PAY -> {
                    boolean bought = user.getWallet().purchase(balance);
                    if (bought) {
                        System.out.println("Thanks for buying <3");
                        String date = ir.ac.kntu.helper.Calendar.now().toString();
                        Transaction transaction = new Transaction(balance, date, "withdraw");
                        TransactionDAO.insertTransaction(user.getEmail(), transaction);
                        PurchasesDAO.insertToPurchases(user, date, address.toString());
                        SearchProductController.reduceInventory(user);
                        ShoppingCartDAO.clearShoppingCart(user);
                        SellerDAO.chargeWallet(user, address.getState());
                        return;
                    } else {
                        System.out.println("There is not enough money in your wallet :(");
                        WalletController.handelChargeWallet(user);
                    }
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

    public static boolean addProductToShoppingCart(int productId, int sellerId, String productType, User user) {

        ShoppingCart shoppingCart = ProductDAO.makeShoppingCartObject(productId, sellerId, productType);
        boolean inserted = ShoppingCartDAO.insertToShoppingCart(shoppingCart, user, productId);
        return inserted;
    }

    private static double countTotalCost(User user, String state) {
        String sql1 = "SELECT id FROM users WHERE email = ?";
        String sql2 = "SELECT price, seller_id FROM shoppingCart WHERE user_id = ?";
        String sql3 = "SELECT state FROM sellers WHERE id = ?";

        double totalPrice = 0;
        double shippingCost = 0;
        Set<Integer> countedSellers = new HashSet<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement ps1 = conn.prepareStatement(sql1);
                PreparedStatement ps2 = conn.prepareStatement(sql2);
                PreparedStatement ps3 = conn.prepareStatement(sql3)) {

            ps1.setString(1, user.getEmail());
            ResultSet rs1 = ps1.executeQuery();

            if (!rs1.next()) {
                return 0; 
            }
            int userId = rs1.getInt("id");

            ps2.setInt(1, userId);
            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {
                double price = rs2.getDouble("price");
                int sellerId = rs2.getInt("seller_id");
                totalPrice += price;

                if (!countedSellers.contains(sellerId)) {
                    ps3.setInt(1, sellerId);
                    ResultSet rs3 = ps3.executeQuery();
                    if (rs3.next()) {
                        String sellerState = rs3.getString("state");
                        if (state.equalsIgnoreCase(sellerState)) {
                            shippingCost += 10;
                        } else {
                            shippingCost += 30;
                        }
                    }
                    countedSellers.add(sellerId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalPrice + shippingCost;
    }

}
