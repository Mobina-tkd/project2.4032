package  ir.ac.kntu;

public class Vendilo{
    enum MenuOption{USER, SELLER, SUPPORTER, UNDEFINED}
    enum UserOption{SEARCH_FOR_PRODUCTS, SHOPPING_CART, SETTING, RECENT_PURCHASES,
    ADRESSES, WALLET, CUSTOMER_SUPPORT, BACK, UNDEFINED }
    enum SellerOption{PRODUCTS, WALLET, RECENT_PURCHASES, BACK, UNDEFINED}
    enum SupporterOption{FOLLOW_UP_REQUEST, IDENTITY_VERIFICATION, RECENT_PURCHASES, BACK, UNDEFINED}
    enum Statement{NEW_USER, ALREADY_HAS_ACCOUNT,BACK, UNDEFINED}
    enum AddressOption{INSERT_NEW_ADDRESS, VIEW_ADDRESSES, EDIT_ADDRESSES, DELETE_ADDRESSES, BACK, UNDEFINED}
    enum ProductOption{INSERT_PRODUCT, SET_INVENTORY, BACK, UNDEFINED}
    enum Product{MOBILE, LAPTOP, BOOK, BACK, UNDEFINED}
    enum SearchMenu{SHOW_ALL, SEARCH_BY_PRICE, BACK, UNDEFINED}
    enum SearchBookOption{SHOW_ALL, SEARCH_BY_PRICE, SEARCH_BY_TITLE, BACK, UNDEFINED}
    enum SettingMenu{EMAIL, PHONE_NUMBER, FIRST_NAME, LAST_NAME, PASSWORD, BACK, UNDEFINED}
    enum VarificationMenu{CONFIRM, DENY, BACK, UNDEFINED}
    enum AddToList{ADD, BACK, UNDEFINED}

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