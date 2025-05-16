package ir.ac.kntu.helper.HandleModels;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.AddressDAO;
import ir.ac.kntu.dao.ShoppingCartDAO;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.helper.SearchProduct;
import ir.ac.kntu.helper.readData.PersonFactory;
import ir.ac.kntu.model.Address;
import ir.ac.kntu.model.ShoppingCart;
import ir.ac.kntu.model.User;


public class HandleUserOptions {

    public static void handleSetting(User user) {
        while(true) {
            String newValue;
            Menu.settingMenu();
            Vendilo.SettingMenu settingMenue = Menu.getSettingMenu();
            switch (settingMenue) {
                case EMAIL-> {
                    newValue = PersonFactory.readEmail();
                    HandleUser.setAndUpdateUserData(user, "email", newValue);
                }
                case PHONE_NUMBER-> {
                    newValue = PersonFactory.readPhoneNUmber();
                    HandleUser.setAndUpdateUserData(user, "phone_number", newValue);

                }
                case FIRST_NAME-> {
                    System.out.print("Enter first name: ");
                    newValue = ScannerWrapper.getInstance().nextLine();
                    HandleUser.setAndUpdateUserData(user, "first_name", newValue);

                }
                case LAST_NAME-> {
                    System.out.print("Enter last name: ");
                    newValue = ScannerWrapper.getInstance().nextLine();
                    HandleUser.setAndUpdateUserData(user, "last_name", newValue);

                }
                case PASSWORD-> {
                    newValue = PersonFactory.readPassword();
                    HandleUser.setAndUpdateUserData(user, "password", newValue);

                }
                case BACK-> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\\n");
                }                        
            }
        }
    
    }



    public static void handleAddress(User user) {

        while(true) {
            Menu.addressMenu();
            Vendilo.AddressOption addressOption = Menu.getAddressOption();
            switch (addressOption) {
                case INSERT_NEW_ADDRESS -> {
                    while(true) {
                        Address address = Address.readAddress();
                        boolean inserted = AddressDAO.insertAddress(address, user);
                        if(!inserted) {
                            continue;
                        }
                        break;
                    }
                }
                case VIEW_ADDRESSES -> {
                    HandleAddress.printAllAddresses(user);
                }
                case EDIT_ADDRESSES -> {
                    System.out.print("Enter the location you want to edit : ");
                    String location = ScannerWrapper.getInstance().nextLine();
                    HandleAddress.editAddress(user, location);
                       
                }
                case DELETE_ADDRESSES -> {
                    System.out.print("Enter the location you want to delete : ");
                    String location = ScannerWrapper.getInstance().nextLine();
                    HandleAddress.deleteAddress(user, location);
                }
                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\\n");
                    
                }
            }
        }
    
        }


        public static void searchProduct(User user) {
            while(true) {
                Menu.productCategoryMenu();
                Vendilo.Product insertProduct = Menu.getProductCategory();
                switch (insertProduct) {
                    case MOBILE -> {
                        SearchProduct.searchMobile(user);
                        
                    }
                    case LAPTOP -> {
                        SearchProduct.searchLaptop(user);
                       
                    }
                    case BOOK -> {
                        SearchProduct.searchBook(user);
                        
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



        public static void handleShoppingCart(User user) {
            while (true) {
                HandleCart.printAllOptionsInCart(user);
                Menu.buyOrdeleteFromCartMenu();
                Vendilo.DeleteFromCart option = Menu.getBuyOrDeleteFromListOption();
                
                switch (option) {
                    case DELETE -> {
                        System.out.println("Enter the id of product you want to delete: ");
                        int id = ScannerWrapper.getInstance().nextInt();
                        ShoppingCartDAO.deleteProductFromCart(id);
                    }
                    case BUY ->{
                        ShoppingCart.handleBuying(user);

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


    
}
