package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.BookDAO;
import ir.ac.kntu.dao.InformProductDAO;
import ir.ac.kntu.dao.LaptopDAO;
import ir.ac.kntu.dao.MobileDAO;
import ir.ac.kntu.dao.ProductDAO;
import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.helper.ValidationUtil;
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
                    handleSettingInventory(agencyCode);
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
                    System.out.println(ConsoleColors.RED + "Undefined Choice; Try again...\n" + ConsoleColors.RESET);

                }
                default -> throw new AssertionError();
            }
        }
    }

    public static void handleAddProductToList(String productType, User user) {

        while (true) {
            System.out.print(
                    "Enter the id of a product to see more information\n (if you want to go back enter \"0\"): ");
            int productId = ScannerWrapper.getInstance().nextInt();
            if (productId == 0) {
                return;
            }
            int sellerId = ProductDAO.findSellerId(productId, productType);
            ShoppingCart shoppingCart = ProductDAO.makeShoppingCartObject(productId, sellerId, productType);
            System.out.println(shoppingCart.getInformation());
            handleInforming(productId, productType, user.getEmail());
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
                    System.out.println(ConsoleColors.RED + "Undefined Choice; Try again...\n" + ConsoleColors.RESET);
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
                System.out.println(ConsoleColors.RED
                        + "The product didnt add to you shopping cart :( please try again..." + ConsoleColors.RESET);

            }

        }
    }

    public static void handleSettingInventory(String agencyCode) {
        ProductDAO.printSellerProducts(agencyCode);
        String productType = getProductType();
        int productId = getProductId();
        int inventory = getInventory();
        if (productType == null || productId == 0 || inventory == -1) {
            return;
        }
        ProductDAO.setInventory(productType, productId, inventory);
    }

    private static int getProductId() {
        String productId = "";
        while (true) {
            System.out.println("Enter the id of the product(press 0 to return)");
            productId = ScannerWrapper.getInstance().nextLine();
            if (ValidationUtil.isInteger(productId)) {
                return Integer.parseInt(productId);
            }
            System.out.println("Invalid input, please try again");
        }
    }

    private static int getInventory() {
        String inventory = "";
        while (true) {
            System.out.println("Enter the inventory(press -1 to return)");
            inventory = ScannerWrapper.getInstance().nextLine();
            if (ValidationUtil.isInteger(inventory)) {
                return Integer.parseInt(inventory);
            }
            System.out.println("Invalid input, please try again");
        }
    }

    private static String getProductType() {
        String productType = "";
        while (true) {
            System.out.println("Enter the name of product you want to add(press 0 to return)");
            productType = ScannerWrapper.getInstance().nextLine();
            if (productType.equals("Book") || productType.equals("Mobile") || productType.equals("Laptop")) {
                return productType;
            }
            if (productType.equals("0")) {
                return "";
            }
            System.out.println("Invalid type, please try again");
        }
    }

    private static void handleInforming(int productId, String productName, String email) {
        int inventory = ProductDAO.findInventory(productId, productName);
        if (inventory == 0) {
            while (true) {
                ScannerWrapper.getInstance().nextLine();
                System.out.print("Inform me when its available(press 1 or press 0 to return): ");
                String input = ScannerWrapper.getInstance().nextLine();
                if (input.equals("0")) {
                    return;
                } else if (input.equals("1")) {
                    InformProductDAO.insertInformUser(productId, productName, email);
                    return;
                } else {
                    System.out.println("Invalid input, please try again");
                }
            }
        }
    }

}
