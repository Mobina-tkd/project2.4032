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
import ir.ac.kntu.dao.DiscountDAO;
import ir.ac.kntu.dao.ProductDAO;
import ir.ac.kntu.dao.PurchasesDAO;
import ir.ac.kntu.dao.SellerDAO;
import ir.ac.kntu.dao.ShoppingCartDAO;
import ir.ac.kntu.dao.TransactionDAO;
import ir.ac.kntu.dao.UserDAO;
import ir.ac.kntu.dao.VendiloPlusDAO;
import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.helper.readData.ReadDataUtil;
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
                    System.out.println(ConsoleColors.RED + "Undefined Choice; Try again...\n" + ConsoleColors.RESET);
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
            ScannerWrapper.getInstance().nextLine();
            ShoppingCartDAO.printInfoById(productId);
            System.out.print("Are you sure you want to delete this product? Y/N: ");
            String delete = ScannerWrapper.getInstance().nextLine();
            if ("y".equalsIgnoreCase(delete)) {
                ShoppingCartDAO.deleteProductFromCart(productId);
            } else if ("n".equalsIgnoreCase(delete)) {
                return;
            } else {
                System.out.println(ConsoleColors.RED + "Invalid Choice; Try again...\n" + ConsoleColors.RESET);
            }
        }
    }

    private static void handleBuying(User user) {
        while (true) {
            AddressDAO.printAllAddresses(user);
            Menu.chooseAddressMenu();

            Vendilo.ChooseAddress option = Menu.getChooseOrAddAddress();

            switch (option) {
                case CHOOSE -> handleExistingAddressFlow(user);
                case NEW -> handleNewAddressFlow(user);
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

    private static void handleExistingAddressFlow(User user) {
        System.out.print("Enter the number of the address: ");
        int addressId = ScannerWrapper.getInstance().nextInt();
        Address address = AddressDAO.findAddress(addressId);

        if (address == null) {
            System.out.println(ConsoleColors.RED + "Invalid address ID.\n" + ConsoleColors.RESET);
            return;
        }

        processPurchase(user, address);
    }

    private static void handleNewAddressFlow(User user) {
        Address address = ReadDataUtil.readAddress();
        AddressDAO.insertAddress(address, user);
        processPurchase(user, address);
    }

    private static void processPurchase(User user, Address address) {
        double totalCost = countTotalPrice(user);
        double shippingCost = countShippingCost(user, address.getState());
        double finalCost = totalCost + shippingCost;

        System.out.println("Total cost (including shipping cost):  " +
                ConsoleColors.GREEN + finalCost + ConsoleColors.RESET);

        handlePaying(user, totalCost, shippingCost, address);
    }

    private static void handlePaying(User user, double totalCost, double shippingCost, Address address) {
        totalCost = applyDiscount(totalCost, user);
        showTotalCost(totalCost, shippingCost);

        while (true) {
            Menu.payMenu();
            Vendilo.PayMenu choice = Menu.getPayOption();

            switch (choice) {
                case PAY -> processPayment(user, totalCost, shippingCost, address);
                case BACK -> {
                    return;
                }
                case UNDEFINED -> showUndefinedChoiceMessage();
                default -> throw new AssertionError();
            }
        }
    }

    private static void showTotalCost(double totalCost, double shippingCost) {
        System.out.println("Total cost after applying discount (including shipping cost):  "
                + ConsoleColors.GREEN + (totalCost + shippingCost) + ConsoleColors.RESET);
    }

    private static void processPayment(User user, double totalCost, double shippingCost, Address address) {
        double finalCost = totalCost + shippingCost;
        if (UserDAO.getBalance(user) > finalCost) {
            completePurchase(user, finalCost, address);
        } else {
            showInsufficientBalanceMessage();
            WalletController.handelChargeWallet(user);
        }
    }

    private static void completePurchase(User user, double finalCost, Address address) {
        System.out.println("\nThanks for buying " + ConsoleColors.RED + "<3" + ConsoleColors.RESET);

        String date = ir.ac.kntu.helper.Calendar.now().toString();
        Transaction transaction = new Transaction(finalCost, date, "withdraw");

        UserDAO.updateBalance(finalCost, user, "-");
        TransactionDAO.insertTransaction(user.getEmail(), transaction);
        PurchasesDAO.insertToPurchases(user, date, address.toString());
        SearchProductController.reduceInventory(user);
        SellerDAO.chargeWallet(user);
        ShoppingCartDAO.clearShoppingCart(user);
    }

    private static void showInsufficientBalanceMessage() {
        System.out.println(ConsoleColors.RED + "There is not enough money in your wallet "
                + ConsoleColors.RESET + ":(");
    }

    private static void showUndefinedChoiceMessage() {
        System.out.println(ConsoleColors.RED + "Undefined Choice; Try again...\n" + ConsoleColors.RESET);
    }

    public static boolean addProductToShoppingCart(int productId, int sellerId, String productType, User user) {

        ShoppingCart shoppingCart = ProductDAO.makeShoppingCartObject(productId, sellerId, productType);
        return ShoppingCartDAO.insertToShoppingCart(shoppingCart, user, productId);
    }

    private static double countTotalPrice(User user) {
        String sql = "SELECT price FROM shoppingCart WHERE user_id = ?";
        double totalPrice = 0;

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pStatement = conn.prepareStatement(sql)) {

            int userId = UserDAO.findUserId(user.getEmail());
            pStatement.setInt(1, userId);

            try (ResultSet resultSet = pStatement.executeQuery()) {
                while (resultSet.next()) {
                    totalPrice += resultSet.getDouble("price");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (VendiloPlusDAO.vendiloPlusUser(user)) {
            totalPrice = 0.95 * totalPrice;
        }
        return totalPrice;
    }

    private static double countShippingCost(User user, String state) {
        String sql = "SELECT seller_id FROM shoppingCart WHERE user_id = ?";
        String sql2 = "SELECT state FROM sellers WHERE id = ?";
        double shippingCost = 0;
        Set<Integer> countedSellers = new HashSet<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement ps1 = conn.prepareStatement(sql);
                PreparedStatement ps2 = conn.prepareStatement(sql2)) {

            int userId = UserDAO.findUserId(user.getEmail());
            ps1.setInt(1, userId);

            try (ResultSet resultSet = ps1.executeQuery()) {
                while (resultSet.next()) {
                    int sellerId = resultSet.getInt("seller_id");

                    if (!countedSellers.contains(sellerId)) {
                        ps2.setInt(1, sellerId);
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            if (rs2.next()) {
                                String sellerState = rs2.getString("state");
                                if (state.equalsIgnoreCase(sellerState)) {
                                    shippingCost += 10;
                                    if (VendiloPlusDAO.vendiloPlusUser(user)) {
                                        shippingCost = 0;
                                    }
                                } else {
                                    shippingCost += 30;
                                    if (VendiloPlusDAO.vendiloPlusUser(user)) {
                                        shippingCost = 10;
                                    }
                                }
                            }
                        }
                        countedSellers.add(sellerId);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return shippingCost;
    }

    private static double applyDiscount(double totalCost, User user) {
        String code = DiscountDAO.getDiscountCode(user);
        if (code == null || code.trim().isEmpty()) {
            System.out.println("Cant apply this code");
            return totalCost;
        }

        String query = "SELECT type, amount FROM discounts WHERE code = ? AND user_id = ?";

        String type = "";
        double amount = 0;

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(query)) {
            int userId = UserDAO.findUserId(user.getEmail());

            stmt.setString(1, code);
            stmt.setInt(2, userId);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    type = resultSet.getString("type");
                    amount = resultSet.getDouble("amount");
                } else {
                    System.out.println("Discount code not found for this user.");
                    return totalCost;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return totalCost;
        }

        return checkType(type, amount, code, totalCost);
    }

    private static double checkType(String type, double amount, String code, double totalCost) {

        if ("percent".equalsIgnoreCase(type)) {
            DiscountDAO.reduceTimeUsed(code);
            return totalCost * (1 - (amount / 100));
        }

        if ("amount".equalsIgnoreCase(type) && totalCost > (10 * amount)) {
            DiscountDAO.reduceTimeUsed(code);
            return totalCost - amount;
        }

        System.out.println("Sorry. This code cant be applied.");
        return totalCost;
    }

}
