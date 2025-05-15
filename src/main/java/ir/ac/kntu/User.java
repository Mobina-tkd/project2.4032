package ir.ac.kntu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;

    public User(String firstName, String lastName, String email, String phoneNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
    public User() {

    }
    
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getPassword() { return password; }
    


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    public static String  loginPage(){
        String url = "jdbc:sqlite:data.db";  
        while (true){
            System.out.println("--------Login Page-------");
            System.out.print("Enter your username(Phone Number or Email Address) :  ");
            String username = ScannerWrapper.getInstance().nextLine(); 
            System.out.print("Enter you password : ");
            String password = ScannerWrapper.getInstance().nextLine(); 

            String sql = "SELECT password FROM users WHERE email = ? OR phone_number = ?";

            try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, username);
                pstmt.setString(2, username);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    if (storedPassword.equals(password)) {
                        System.out.println("Login successful! Welcome.");
                        return username;
                    } else {
                        System.out.println("Incorrect password.");
                        return null;
                    }
                } else {
                    System.out.println("No account found with that email or phone number.");
                    return null;
                }

            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }

        }
    }

    public static User handleNewUser() {
        User user = new User();
        while(true){ 
            user = Utils.readUserData();
            Boolean inserted = UserDAO.insertUser(user);
            if(!inserted){
            continue;
            }
            break;
        }
            while(true){
            String username = loginPage();
            if(username == null){
                continue;
            }
            return user;
        }
        
    }


    public static void handleUser(){ 
        
        while(true){
            Menu.chooseStatementMenu();
            Vendilo.Statement statement = Menu.getStatementOption();
            User user = new User();
            switch (statement) {
                case NEW_USER -> {
                    user = handleNewUser(); 
                    chooseOption(user);
                }

                case ALREADY_HAS_ACCOUNT -> {
                    String  username ="";
                    while(true){
                        username = loginPage();
                        if(username == null){
                            continue;
                        }
                        break;
                    }
                    user = Utils.findUser(username);
                    chooseOption(user);
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

   

    public static void chooseOption(User user){
        while(true){
            Menu.userMenu();
            Vendilo.UserOption option = Menu.getUserOption();
            switch (option) {
                case SEARCH_FOR_PRODUCTS -> {
                    HandleUserOptions.searchProduct(user);
                    break;
                }
                case SHOPPING_CART-> {
                    //HandleUserOptions.handleShoppingCart(user);

                    break;
                }
                case SETTING-> {
                    HandleUserOptions.handleSetting(user);
                }
                case RECENT_PURCHASES-> {
                    break;
                }
                case ADRESSES -> {
                    HandleUserOptions.handleAddress(user);    
                }
                case WALLET -> {
                    break;
                }
                case CUSTOMER_SUPPORT -> {
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


    
}
