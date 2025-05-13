package ir.ac.kntu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Seller {
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
        String url = "jdbc:sqlite:data.db";  
        while (true) {
            System.out.println("--------Login Page-------");
            System.out.print("Enter your agency code : ");
            String username = ScannerWrapper.getInstance().nextLine(); 
            System.out.print("Enter your password : ");
            String userPassword = ScannerWrapper.getInstance().nextLine(); 
    
            try (Connection conn = DriverManager.getConnection(url)) {
                String sql = "SELECT password FROM sellers WHERE agency_code = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
    
                ResultSet rs = pstmt.executeQuery();
    
                if (rs.next()) {
                    String Password = rs.getString("password");
                    if (userPassword.equals(Password)) {
                        System.out.println("Welcome dear seller");
                        return true;
                    } else {
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
}
