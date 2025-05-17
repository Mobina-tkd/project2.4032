package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.AddressDAO;
import ir.ac.kntu.dao.ProductDAO;
import ir.ac.kntu.dao.ShoppingCartDAO;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.helper.readData.ReadAddress;
import ir.ac.kntu.model.Address;
import ir.ac.kntu.model.ShoppingCart;
import ir.ac.kntu.model.User;

public class ShoppingCartController {

    public static void handleShoppingCart(User user) {
        while (true) {
            ShoppingCartDAO.printAllOptionsInCart(user);
            Menu.buyOrdeleteFromCartMenu();
            Vendilo.DeleteFromCart option = Menu.getBuyOrDeleteFromListOption();
            
            switch (option) {
                case DELETE -> {
                    System.out.println("Enter the id of product you want to delete: ");
                    int id = ScannerWrapper.getInstance().nextInt();
                    ShoppingCartDAO.deleteProductFromCart(id);
                }
                case BUY ->{
                    ShoppingCartController.handleBuying(user);

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

    public static void handleBuying(User user) {
        AddressDAO.printAllAddresses(user);
        Menu.chooseAddressMenu();
        Vendilo.ChooseAddress option = Menu.getChooseOrAddAddress();
        switch (option) {
            case CHOOSE -> {
                System.out.print("Enter the number of the address: ");
                int id = ScannerWrapper.getInstance().nextInt();
                String state = AddressDAO.findState(id);
                double totalCost = countTotalCost(user, state);
            }
            case NEW -> {
                Address address = ReadAddress.readAddress();
                AddressDAO.insertAddress(address, user);
                String state = address.getState();
                double totalCost = countTotalCost(user, state);
            }
            case BACK -> {
            }
            case UNDEFINED -> {
                System.out.println("Undefined Choice; Try again...\n");
                break;
            }      
        }

    }

    
    public static boolean addProductToShoppingCart(int productId, int sellerId, String productType, User user) {
        String information = ProductDAO.getProductInfoById(productId, productType);
        double price = ProductDAO.getPriceById(productId, productType);
        ShoppingCart shoppingCart = new ShoppingCart(price, information, sellerId);
        boolean inserted = ShoppingCartDAO.insertToShoppingCart(shoppingCart, user);
        return inserted;
    }

    public static double  countTotalCost(User user, String state) {
        return 0;
    }
}
