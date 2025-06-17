package ir.ac.kntu;

import ir.ac.kntu.dao.AddressDAO;
import ir.ac.kntu.dao.BookDAO;
import ir.ac.kntu.dao.DiscountDAO;
import ir.ac.kntu.dao.InformProductDAO;
import ir.ac.kntu.dao.LaptopDAO;
import ir.ac.kntu.dao.MobileDAO;
import ir.ac.kntu.dao.NotificationDAO;
import ir.ac.kntu.dao.PurchasesDAO;
import ir.ac.kntu.dao.RequestDAO;
import ir.ac.kntu.dao.SellerDAO;
import ir.ac.kntu.dao.ShoppingCartDAO;
import ir.ac.kntu.dao.TransactionDAO;
import ir.ac.kntu.dao.UserDAO;
import ir.ac.kntu.dao.VendiloPlusDAO;
import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.controllers.LoginPageController;
import ir.ac.kntu.helper.controllers.ManagerController;
import ir.ac.kntu.helper.controllers.SellerController;
import ir.ac.kntu.helper.controllers.SupporterController;
import ir.ac.kntu.helper.controllers.UserController;

public class Vendilo {

    public enum NotificationType {
        CHECK_REQUEST, GENERAL_TEXT, DISCOUNT_CODE, INVENTORY
    }

    public enum VendiloPlus {
        BUY_SUBSCRIPTION, MY_SUBSCRIPTION, BACK, UNDEFINED
    }

    public enum Subscription {
        ONE_MONTH, THREE_MONTH, ONE_YEAR, BACK, UNDEFINED
    }

    public enum MenuOption {
        USER, SELLER, SUPPORTER, MANAGER, UNDEFINED
    }

    public enum UserOption {
        SEARCH_FOR_PRODUCTS, SHOPPING_CART, SETTING, RECENT_PURCHASES,
        ADRESSES, WALLET, CUSTOMER_SUPPORT, DISCOUNTS, VENDILO_PLUS, NOTIFICATION, BACK, UNDEFINED
    }

    public enum SellerOption {
        PRODUCTS, WALLET, RECENT_PURCHASES, BACK, UNDEFINED
    }

    public enum SupporterOption {
        FOLLOW_UP_REQUEST, IDENTITY_VERIFICATION, RECENT_PURCHASES, BACK, UNDEFINED
    }

    public enum Statement {
        NEW_USER, ALREADY_HAS_ACCOUNT, BACK, UNDEFINED
    }

    public enum AddressOption {
        INSERT_NEW_ADDRESS, VIEW_ADDRESSES, EDIT_ADDRESSES, DELETE_ADDRESSES, BACK, UNDEFINED
    }

    public enum ProductOption {
        INSERT_PRODUCT, SET_INVENTORY, BACK, UNDEFINED
    }

    public enum Product {
        MOBILE, LAPTOP, BOOK, BACK, UNDEFINED
    }

    public enum SearchMenu {
        SHOW_ALL, SEARCH_BY_PRICE, BACK, UNDEFINED
    }

    public enum SearchBookOption {
        SHOW_ALL, SEARCH_BY_PRICE, SEARCH_BY_TITLE, BACK, UNDEFINED
    }

    public enum SettingMenu {
        EMAIL, PHONE_NUMBER, FIRST_NAME, LAST_NAME, PASSWORD, BACK, UNDEFINED
    }

    public enum VarificationMenu {
        CONFIRM, DENY, BACK, UNDEFINED
    }

    public enum AddToList {
        ADD, BACK, UNDEFINED
    }

    public enum DeleteFromCart {
        DELETE, BUY, BACK, UNDEFINED
    }

    public enum ChooseAddress {
        CHOOSE, NEW, BACK, UNDEFINED
    }

    public enum PayMenu {
        PAY, BACK, UNDEFINED
    }

    public enum ChargeWalletOption {
        CHARGE, BACK, UNDEFINED
    }

    public enum PurchaseMenu {
        INFO, CHART, BACK, UNDEFINED
    }

    public enum UserSupportOptions {
        PRREVIOUS_REQUESTS, REPORT, WRONG_PRODUCT_SENT, SETTING, ORDER_NOT_RECEIVED, BACK, UNDEFINED
    }

    public enum SupporterSupporterOptions {
        REPORT, WRONG_PRODUCT_SENT, SETTING, ORDER_NOT_RECEIVED, BACK, UNDEFINED
    }

    public enum SetMessage {
        SET_MESSAGE, BACK, UNDEFINED
    }

    public enum WatchRespond {
        SHOW_RESOPND, BACK, UNDEFINED
    }

    public enum WalletOption {
        CURRETN_BALANCE, CHARGE, RECENT_TRANSACTIONS, BACK, UNDEFINED
    }

    public enum ShowTransaction {
        SHOW_ALL, FILTER_BY_TIME, BACK, UNDEFINED
    }

    public enum SellerWalletOption {
        CURRETN_BALANCE, WITHDRAW, BACK, UNDEFINED
    }

    public enum ManagerOptions {
        MANAGING_USERS, ANALYSIS_SELLER_FUNCTION, ANALYSE_USER_FUNCTION, CREATING_DISCOUNT_CODE, GENERAL_MESSAGE, BACK, UNDEFINED
    }

    public enum ManagingUserOption {
        CREATE_MANAGER, CREATE_SUPPORTER, EDIT_USER, EDIT_MANAGER, EDIT_SUPPORTER, BLOCK_USER, BLOCK_SUPPORTER, BLOCK_MANAGER, SHOW_USERS, BACK, UNDEFINED
    }

    public enum EdditingMenu {
        NAME, USERNAME, PASSWORD, BACK, UNDEFINED
    }

    public static void main(String[] args) {
        UserDAO.createTable();
        AddressDAO.createTable();
        SellerDAO.createTable();
        LaptopDAO.createTable();
        MobileDAO.createTable();
        BookDAO.createTable();
        ShoppingCartDAO.createTable();
        PurchasesDAO.createTable();
        TransactionDAO.createTable();
        RequestDAO.createTable();
        VendiloPlusDAO.createTable();
        InformProductDAO.createTable();
        NotificationDAO.createTable();
        DiscountDAO.createTable();

        while (true) {
            Menu.choosingRoleMenu();
            MenuOption menuOption = Menu.getMenuOption();
            switch (menuOption) {
                case USER -> {
                    UserController.handleUserChoise();
                }

                case SELLER -> {
                    SellerController.handleSellerChoise();
                }

                case SUPPORTER -> {
                    String canEnter = LoginPageController.supporterAndManagerLoginPage("supporters");
                    if (canEnter == null) {
                        continue;
                    }
                    // you have to consider which supporter is entering
                    SupporterController.chooseSupporterOption();
                }
                case MANAGER -> {
                    String canEnter = LoginPageController.supporterAndManagerLoginPage("managers");
                    if (canEnter == null) {
                        continue;
                    }
                    ManagerController.handleManagerOption();
                }

                case UNDEFINED -> {
                    System.out.println(ConsoleColors.RED + "Undefined Choice; Try again...\n" + ConsoleColors.RESET);

                }
                default -> throw new AssertionError();
            }

        }

    }
}