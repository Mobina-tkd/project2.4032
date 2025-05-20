package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.UserDAO;
import ir.ac.kntu.helper.readData.PersonFactory;
import ir.ac.kntu.model.User;
import main.java.ir.ac.kntu.helper.controllers.PurchaseController;
import main.java.ir.ac.kntu.helper.controllers.RequestController;

public class UserController {

    public static void chooseUserOption(User user){
        while(true){
            Menu.userMenu();
            Vendilo.UserOption option = Menu.getUserOption();
            switch (option) {
                case SEARCH_FOR_PRODUCTS -> {
                    SearchProductController.HandleSearchProduct(user);
                }
                case SHOPPING_CART-> {
                    ShoppingCartController.handleShoppingCart(user);
                }
                case SETTING-> {
                    SettingController.handleSetting(user);
                }
                case RECENT_PURCHASES-> {
                    PurchaseController.handlePurchases(user);
                    
                    break;
                }
                case ADRESSES -> {
                    AddressController.handleAddress(user);    
                }
                case WALLET -> {
                    break;
                }
                case CUSTOMER_SUPPORT -> {
                    handleCostumerSupport(user);
                    break;
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\\n");
                }
                
            }
        }
    }  

    private static void handleCostumerSupport(User user) {
        while (true) { 
            Menu.printSupportOptions();
            Vendilo.UserSupporterOptions option = Menu.getUserSupportOption();
            switch (option) {
                case PRREVIOUS_REQUESTS -> {
                    RequestController.handlePreviousRequests(user.getEmail());
                }
                case REPORT -> {
                    RequestController.sendRequest(user.getEmail(), "Report");
                    break;
                }
                case WRONG_PRODUCT_SENT -> {
                    RequestController.sendRequest(user.getEmail(), "Wrong product sent");
                    break;
                }
                case SETTING-> {
                    RequestController.sendRequest(user.getEmail(), "Setting");
                    break;
                } 
                case ORDER_NOT_RECEIVED-> {
                   RequestController.sendRequest(user.getEmail(), "Order not received");
                    break;
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

    


    public static User handleNewUser() {
        User user = new User();
        while(true){ 
            user = PersonFactory.readUserData();
            Boolean inserted = UserDAO.insertUser(user);
            if(!inserted){
            continue;
            }
            break;
        }
            while(true){
            String username = loginPageController.userLoginPage();
            if(username == null){
                continue;
            }
            return user;
        }
        
    }


    public static void handleUserChoise(){  
        while(true){
            Menu.chooseStatementMenu();
            Vendilo.Statement statement = Menu.getStatementOption();
            User user = new User();
            switch (statement) {
                case NEW_USER -> {
                    user = handleNewUser(); 
                    UserController.chooseUserOption(user);
                }

                case ALREADY_HAS_ACCOUNT -> {
                    String  username ="";
                    while(true){
                        username = loginPageController.userLoginPage();
                        if(username == null){
                            continue;
                        }else if(username.equals("Back")) {
                            return;
                        }
                        break;
                    }
                    user = UserDAO.findUser(username);
                    UserController.chooseUserOption(user);
                } 
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println("Undefined Choice");
                    break;
                }        
            }
        }
    }
    
}
