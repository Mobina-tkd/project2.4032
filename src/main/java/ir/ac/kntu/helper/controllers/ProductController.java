package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.BookDAO;
import ir.ac.kntu.dao.LaptopDAO;
import ir.ac.kntu.dao.MobileDAO;
import ir.ac.kntu.dao.ProductDAO;
import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.helper.readData.ProductFactory;
import ir.ac.kntu.model.Book;
import ir.ac.kntu.model.Laptop;
import ir.ac.kntu.model.Mobile;
import ir.ac.kntu.model.ShoppingCart;
import ir.ac.kntu.model.User;

public class ProductController {

    public static void handleProduct(String agencyCode) {
        while (true) {
            Menu.productMenu();
            Vendilo.ProductOption productOption = Menu.getProductOption();
            switch (productOption) {
                case INSERT_PRODUCT -> {
                    handleInsertProduct(agencyCode);
                }
                case SET_INVENTORY -> {
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println(ConsoleColors.RED +"Undefined Choice; Try again...\n" + ConsoleColors.RESET);

                }
                default -> throw new AssertionError();
            }
        }
    }

    private static void handleInsertProduct(String agencyCode) {
        while (true) {
            Menu.productCategoryMenu();
            Vendilo.Product insertProduct = Menu.getProductCategory();
            switch (insertProduct) {
                case MOBILE -> {
                    Mobile mobile = ProductFactory.readMobileData();
                    MobileDAO.insertMobile(mobile, agencyCode);
                }
                case LAPTOP -> {
                    Laptop laptop = ProductFactory.readLaptopData();
                    LaptopDAO.insertLaptop(laptop, agencyCode);
                }
                case BOOK -> {
                    Book book = ProductFactory.readBookData();
                    BookDAO.insertBook(book, agencyCode);
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println(ConsoleColors.RED +"Undefined Choice; Try again...\n" + ConsoleColors.RESET);

                }
                default -> throw new AssertionError();
            }
        }
    }

    public static void handleAddProductToList(String productType, User user) {

        while (true) {
            System.out.print("Enter the id of a product to see more information\n (if you want to go back enter \"0\"): ");
            int productId = ScannerWrapper.getInstance().nextInt();
            if(productId == 0) {
                return;
            } 
            int sellerId = ProductDAO.findSellerId(productId, productType);
            ShoppingCart shoppingCart = ProductDAO.makeShoppingCartObject(productId, sellerId, productType);
            System.out.println(shoppingCart.getInformation());
            Menu.addToListMenu();
            Vendilo.AddToList option = Menu.getAddToListOption();
            switch (option) {
                case ADD -> {
                    addProductHandler(productType, productId, user);
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println(ConsoleColors.RED +"Undefined Choice; Try again...\n" + ConsoleColors.RESET);
                }
                default -> throw new AssertionError();
            }
        }
    }

    public static void addProductHandler(String productType, int productId, User user) {
        while (true) {
            int sellerId = ProductDAO.findSellerId(productId, productType);
            boolean added = ShoppingCartController.addProductToShoppingCart(productId, sellerId, productType, user); 
            if (added) {
                break;
            } else {
                System.out.println(ConsoleColors.RED +"The product didnt add to you shopping cart :( please try again..." + ConsoleColors.RESET);

            }

        }
    }

}
