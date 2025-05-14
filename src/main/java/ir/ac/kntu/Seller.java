package ir.ac.kntu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


public class Seller {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    private String firstName;
    private String lastName;
    private String IDNumber;
    private String storeName;
    private String state;
    private String phoneNumber;
    private String password;
    private boolean identityVerified = false;
    private String agencyCode;

    

    // Constructor
    public Seller(String firstName, String lastName, String IDNumber, String storeName,
        String state, String phoneNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.IDNumber = IDNumber;
        this.storeName = storeName;
        this.state = state;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getIDNumber() {
        return IDNumber;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getState() {
        return state;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public boolean isIdentityVerified() {
        return identityVerified;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getAgencyCode() {
        return agencyCode;
    }


    


    public static boolean loginPage() {
        while (true) {
            System.out.println("--------Login Page-------");
            System.out.print("Enter your agency code : ");
            String username = ScannerWrapper.getInstance().nextLine(); 
            System.out.print("Enter your password : ");
            String userPassword = ScannerWrapper.getInstance().nextLine(); 
    
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "SELECT password AND identity_varified AND massage FROM sellers WHERE agency_code = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
    
                ResultSet rs = pstmt.executeQuery();
    
                if (rs.next()) {
                    String Password = rs.getString("password");
                    int identityVarified = rs.getInt("identity_varified");
                    String message = rs.getString("message");
                    if (userPassword.equals(Password) && identityVarified == 1) {
                        System.out.println("Welcome dear seller");
                        return true;
                    }else if(identityVarified == 2) {
                        System.out.println(message);

                    }else {
                        System.out.println("Incorrect password. Try again.");
                    }
                } else {
                    System.out.println("We could not find this agency code.");
                    return false;
                }
    
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
                return false;
            }
        }
    }
    
    public static void handleNewSeller() {
        while(true){ 
            Seller seller = Utils.readSellerData();
            String agencyCode = DealerCodeGenerator.generateUniqueCode();
            seller.setAgencyCode(agencyCode);
            Boolean inserted = SellerDAO.insertSeller(seller);
            if(inserted){
                System.out.println("Here is your agency code : " + agencyCode);
                break;
            }
        }
        while(true){
            boolean canEnter = loginPage();
            if(!canEnter){
                continue;
            }
            break;
        }
    }



    public static void handleSeller(){
        while(true){
            Menu.chooseStatementMenu();
            Vendilo.Statement statement = Menu.getStatementOption(); 
            switch (statement) {
                case NEW_USER -> {
                    handleNewSeller();
                    chooseOption();
                }
                case ALREADY_HAS_ACCOUNT -> {
                    while(true){
                        boolean canEnter = loginPage();
                        if(!canEnter){
                            continue;
                        }
                        break;
                    }
                    chooseOption();
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

 

    public static void chooseOption(){
        while(true){
            Menu.sellerMenu();
            Vendilo.SellerOption option = Menu.getSellerOption();
            switch (option) {
                case PRODUCTS -> {
                    handleProduct();
                }
                case WALLET-> {
                    break;
                }
                case RECENT_PURCHASES-> {
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


    private static void handleProduct() {
        while(true) {
            Menu.productMenu();
            Vendilo.ProductOption productOption = Menu.getProductOption();
            switch (productOption) {
                case INSERT_PRODUCT-> {
                    handleInsertProduct();
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

    public static void handleInsertProduct() {
        while(true) {
            Menu.productCategoryMenu();
            Vendilo.Product insertProduct = Menu.getProductCategory();
            switch (insertProduct) {
                case MOBILE -> {
                    Mobile mobile = new Mobile();
                    mobile = mobile.readData();
                    MobileDAO.insertMobile(mobile);
                }
                case LAPTOP -> {
                    Laptop laptop = new Laptop();
                    laptop = laptop.readData();
                    LaptopDAO.insertLaptop(laptop);
                }
                case BOOK -> {
                    Book book = new Book();
                    book = book.readData();
                    BookDAO.insertBook(book);
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

    public static boolean  printSellersData() { //modify print format
        String query = "SELECT * FROM WHERE identity_varified = 0";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        ResultSet rs = pstmt.executeQuery();

        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        boolean found = false;
        while (rs.next()) {
            found = true;
            for (int i = 2; i <= columnCount; i++) {
                System.out.print(meta.getColumnName(i) + ": " + rs.getString(i) + "\t");
            }
            System.out.println();
        }

        if (!found) {
            System.out.println("No products found with identity_varified 0");
            return false;
        }

    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
        return false;
    }
    return true;

    }

    public static boolean printByAgencyCode(String agencyCode) { //modify print format
        String query = "SELECT * FROM WHERE agency_code = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, agencyCode);


        ResultSet rs = pstmt.executeQuery();

        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        boolean found = false;
        while (rs.next()) {
            found = true;
            for (int i = 2; i <= columnCount; i++) {
                System.out.print(meta.getColumnName(i) + ": " + rs.getString(i) + "\t");
            }
            System.out.println();
        }

        if (!found) {
            System.out.println("No products found with agency_code " + agencyCode );
            return false;
        }

    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
        return false;
    }
    return true;

    }


}
