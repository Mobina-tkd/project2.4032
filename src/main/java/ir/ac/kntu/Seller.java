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
    private boolean identityVerified;

    // Constructor
    public Seller(String firstName, String lastName, String IDNumber, String storeName,
                  String state, String phoneNumber, String password, boolean identityVerified) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.IDNumber = IDNumber;
        this.storeName = storeName;
        this.state = state;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.identityVerified = identityVerified;
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


    


    public static boolean loginPage(){
        String url = "jdbc:sqlite:data.db";  
        while (true){
            System.out.println("--------Login Page-------");
            System.out.print("Enter your agency code  :  ");
            String username = ScannerWrapper.getInstance().nextLine(); 
            


            try (Connection conn = DriverManager.getConnection(url)) {
                String sql = "SELECT * FROM agencies WHERE agency_number = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
    
                ResultSet rs = pstmt.executeQuery();
    
                if (rs.next()) {
                    System.out.println("Welcom dear seller");
                    return true;
                } else {
                    System.out.println("We could not find this agency code");
                    return false;
                }
    
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
    

        }
    }
    




    public static void handleSeller(Vendilo.Statement statement){ 
        while(true){
            switch (statement) {
                case NEW_USER -> {
                    while(true){ 
                    Seller seller = Utils.readSellerData();
                    Boolean inserted = SellerDAO.insertSeller(seller);
                    if(!inserted){
                    continue;
                    }
                    break;
                }
                    while(true){
                    boolean canEnter = loginPage();
                    if(!canEnter){
                        continue;
                    }
                    break;
                }
                chooseOption();//im here

                    break;
                }
                case ALREADY_HAS_ACCOUNT -> {
                    Seller.loginPage();
                    break;
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
                    break;
                }
                case WALLET-> {
                    break;
                }
                case RECENT_PURCHASES-> {
                    break;
                }
                case BACK-> {
                    break;
                }
                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\\n");
                }
                
            }
        }
    }  


    private boolean handleProduct() {
        Menu.productMenu();
        Vendilo.ProductOption productOption = Menu.getProductOption();

        switch (productOption) {
            case INSERT_PRODUCT-> {
                Menu.insertProductMenu();
                Vendilo.InsertProduct insertProduct = Menu.getInserProductOption();
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
                    case UNDEFINED -> {
                    }
                }
            }
            case SET_INVENTORY-> {
            }
            case UNDEFINED-> {
            }
        }


    }
}
