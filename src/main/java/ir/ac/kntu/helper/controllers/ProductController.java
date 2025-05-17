package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.BookDAO;
import ir.ac.kntu.dao.LaptopDAO;
import ir.ac.kntu.dao.MobileDAO;
import ir.ac.kntu.dao.ProductDAO;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.helper.readData.ProductFactory;
import ir.ac.kntu.model.Book;
import ir.ac.kntu.model.Laptop;
import ir.ac.kntu.model.Mobile;
import ir.ac.kntu.model.User;

public class ProductController {

    public static void handleProduct(String agencyCode) {
        while(true) {
            Menu.productMenu();
            Vendilo.ProductOption productOption = Menu.getProductOption();
            switch (productOption) {
                case INSERT_PRODUCT-> {
                    handleInsertProduct(agencyCode);
                }
                case SET_INVENTORY-> {
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED-> {
                    System.out.println("Undefined Choice; Try again...\n");

                }
            }
        }
    }

    private static void handleInsertProduct(String agencyCode) {
        while(true) {
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
                    System.out.println("Undefined Choice; Try again...\n");

                }
            }
        }
    }


    public static void handleAddProductToList(String productType, User user) {
        while (true) { 
            Menu.addToListMenu();
            Vendilo.AddToList option = Menu.getAddToListOption();
            switch (option) {
                case ADD -> {
                    chooseProduct(productType, user);
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\n");
                }       
            }
        }
    }   
    
    public static void chooseProduct(String productType, User user) {
        
        while (true) { 
            System.out.println("Enter the id of the product you want to add to shopping cart: ");
            int productId = ScannerWrapper.getInstance().nextInt();  
            int sellerId = ProductDAO.findSellerId(productId, productType);     
            boolean added = ShoppingCartController.addProductToShoppingCart(productId, sellerId, productType, user); //might product be soled out
            if(added) {
                break;
            }else {
                System.out.println("The product didnt add to you shopping cart :( please try again...");

            }

        }
    }
    
}
