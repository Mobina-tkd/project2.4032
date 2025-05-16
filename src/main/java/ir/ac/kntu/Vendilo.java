package  ir.ac.kntu;

import java.sql.ResultSet;

import ir.ac.kntu.dao.AddressDAO;
import ir.ac.kntu.dao.BookDAO;
import ir.ac.kntu.dao.LaptopDAO;
import ir.ac.kntu.dao.MobileDAO;
import ir.ac.kntu.dao.SellerDAO;
import ir.ac.kntu.dao.ShoppingCartDAO;
import ir.ac.kntu.dao.UserDAO;
import ir.ac.kntu.model.Seller;
import ir.ac.kntu.model.Supporter;
import ir.ac.kntu.model.User;

public class Vendilo{
    public enum MenuOption{USER, SELLER, SUPPORTER, UNDEFINED}
    public enum UserOption{SEARCH_FOR_PRODUCTS, SHOPPING_CART, SETTING, RECENT_PURCHASES,
    ADRESSES, WALLET, CUSTOMER_SUPPORT, BACK, UNDEFINED }
    public enum SellerOption{PRODUCTS, WALLET, RECENT_PURCHASES, BACK, UNDEFINED}
    public enum SupporterOption{FOLLOW_UP_REQUEST, IDENTITY_VERIFICATION, RECENT_PURCHASES, BACK, UNDEFINED}
    public enum Statement{NEW_USER, ALREADY_HAS_ACCOUNT,BACK, UNDEFINED;

        public ResultSet executeQuery(String query) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    public enum AddressOption{INSERT_NEW_ADDRESS, VIEW_ADDRESSES, EDIT_ADDRESSES, DELETE_ADDRESSES, BACK, UNDEFINED}
    public enum ProductOption{INSERT_PRODUCT, SET_INVENTORY, BACK, UNDEFINED}
    public enum Product{MOBILE, LAPTOP, BOOK, BACK, UNDEFINED}
    public enum SearchMenu{SHOW_ALL, SEARCH_BY_PRICE, BACK, UNDEFINED}
    public enum SearchBookOption{SHOW_ALL, SEARCH_BY_PRICE, SEARCH_BY_TITLE, BACK, UNDEFINED}
    public enum SettingMenu{EMAIL, PHONE_NUMBER, FIRST_NAME, LAST_NAME, PASSWORD, BACK, UNDEFINED}
    public enum VarificationMenu{CONFIRM, DENY, BACK, UNDEFINED}
    public enum AddToList{ADD, BACK, UNDEFINED}
    public enum DeleteFromCart{DELETE,BUY, BACK, UNDEFINED}
    public enum ChooseAddress{CHOOSE, NEW, BACK, UNDEFINED}

    public static void main(String[] args) {
        UserDAO.createTable();
        AddressDAO.createTable();
        SellerDAO.createTable();
        LaptopDAO.createTable();
        MobileDAO.createTable();
        BookDAO.createTable();
        ShoppingCartDAO.createTable();
        
        
        while(true){
            Menu.choosingRoleMenu();
            MenuOption menuOption = Menu.getMenuOption();
            switch (menuOption) {
                case USER -> {
                    User.handleUser();
                } 
            
                case SELLER -> { 
                    Seller.handleSeller();
                }
            
                case SUPPORTER -> {
                    while(true){
                        boolean canEnter = Supporter.loginPage();
                        if(!canEnter){
                            continue;
                        }
                        break;
                    }
                    Supporter.chooseOption();
                }
            
                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\n");
                    break;
                }
            }
            
        }
        


    } 
}