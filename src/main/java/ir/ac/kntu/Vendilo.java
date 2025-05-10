package  ir.ac.kntu;

public class Vendilo{
    enum MenuOption{USER, SELLER, SUPPORTER, UNDEFINED}
    enum UserOption{SEARCH_FOR_PRODUCTS, SHOPPING_CART, SETTING, RECENT_PURCHASES,
    ADRESSES, WALLET, CUSTOMER_SUPPORT, BACK, UNDEFINED }
    enum SellerOption{PRODUCTS, WALLET, RECENT_PURCHASES, BACK, UNDEFINED}
    enum SupporterOption{FOLLOW_UP_REQUEST, IDENTITY_VERIFICATION, RECENT_PURCHASES, BACK, UNDEFINED}
    enum Statement{NEW_USER, ALREADY_HAS_ACCOUNT,BACK, UNDEFINED}
    enum AddressOption{INSERT_NEW_ADDRESS, VIEW_ADDRESSES, EDIT_ADDRESSES, DELETE_ADDRESSES, UNDEFINED}
    enum ProductOption{INSERT_PRODUCT, SET_INVENTORY, UNDEFINED}
    enum InsertProduct{MOBILE, LAPTOP, BOOK, UNDEFINED}
    
    public static void main(String[] args) {
        UserDAO.createTable();
        AddressDAO.createTable();
        SellerDAO.createTable();
        
        
        
        while(true){
            Menu.choosingRoleMenu();
            MenuOption menuOption = Menu.getMenuOption();
            switch (menuOption) {
                case USER -> {
                    Menu.chooseStatementMenu();
                    Statement statement = Menu.getStatementOption();
                    if(statement.equals(Statement.BACK)){
                        continue;
                    }
                    User.handleUser(statement);
                    break;   
                } 
            
                case SELLER -> { 
                    Menu.chooseStatementMenu();
                    Statement statement = Menu.getStatementOption();
                    if(statement.equals(Statement.BACK)){
                        continue;
                    }
                    Seller.handleSeller(statement);
                    //you need a if clause
                    break;  
                }
            
                case SUPPORTER -> {
                    
                    break;
                }
            
                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\n");
                    break;
                }
            }
            
        }
        


    } 
}