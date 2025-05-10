package ir.ac.kntu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final String DB_URL = "jdbc:sqlite:data.db";
    public static User readUserData(){
    
    String email;
    String password;
    String phoneNumber;
    System.out.print("Enter first name: ");
    String firstName = ScannerWrapper.getInstance().nextLine();
       
    System.out.print("Enter last name: ");
    String lastName = ScannerWrapper.getInstance().nextLine();
    
    while(true){
        System.out.print("Enter email: ");
        email = ScannerWrapper.getInstance().nextLine();
        if(!matchEmail(email)){
            System.out.println("Invalid email address");
            continue;
        }
        break;
    }

    while(true){
        System.out.print("Enter phone number: ");
        phoneNumber = ScannerWrapper.getInstance().nextLine();
        if(!matchNumber(phoneNumber)){
            System.out.println("Invalid phone number");
            continue;
        }
        break;
    }
    
    while(true){
        System.out.print("Enter password: ");
        password = ScannerWrapper.getInstance().nextLine();
        if(!matchPassword(password)){
            System.out.println("Weak password"); 
            continue;   
        }
        break;

    }

    User user = new User(firstName, lastName, email, phoneNumber, password);
    return user;
    }



    public static Seller readSellerData(){

        String firstName;
        String lastName;
        String storName;
        String IDNumber;
        String phoneNumber;
        String password;
        String state;
        String agencyCode;
        boolean identityVarified = false;


        
        System.out.print("Enter first name: ");
        firstName = ScannerWrapper.getInstance().nextLine();
           
        System.out.print("Enter last name: ");
        lastName = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter stor name: ");
        storName = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter state: ");
        state = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter ID number: ");
        IDNumber = ScannerWrapper.getInstance().nextLine();
        
       
    
        while(true){
            System.out.print("Enter phone number: ");
            phoneNumber = ScannerWrapper.getInstance().nextLine();
            if(!matchNumber(phoneNumber)){
                System.out.println("Invalid phone number");
                continue;
            }
            break;
        }
        
        while(true){
            System.out.print("Enter password: ");
            password = ScannerWrapper.getInstance().nextLine();
            if(!matchPassword(password)){
                System.out.println("Weak password"); 
                continue;   
            }
            break;
    
        }
    
        Seller seller = new Seller(firstName, lastName, IDNumber, storName, state, phoneNumber, password, identityVarified);
        return seller;
        }






    public static boolean matchEmail(String email){
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(email);
        if(matcher.find()){
            return true;
        }
        return false;


    }

    public static boolean matchPassword(String password){
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d])[A-Za-z\\d\\S]{8,}$");
        Matcher matcher = pattern.matcher(password);
        if(matcher.find()){
            return true;
        }
        return false;
        
    }

    public static boolean matchNumber(String Number){
        Pattern pattern = Pattern.compile("^(09\\d{9}|\\+98\\d{10})$");
        Matcher matcher = pattern.matcher(Number);
        if(matcher.find()){
            return true;
        }
        return false;
        
    }

    public static Address readAddressFromUser() {
        System.out.print("Enter the location: ");
        String location = ScannerWrapper.getInstance().nextLine();
        System.out.print("Enter the state: ");
        String state = ScannerWrapper.getInstance().nextLine();
        System.out.print("Enter the street: ");
        String street = ScannerWrapper.getInstance().nextLine();
        System.out.print("Enter the houseNumber: ");
        String houseNumber = ScannerWrapper.getInstance().nextLine();

        Address address = new Address(location, state, street, houseNumber);
        return address;
    }

    public static User findUser(String username) {
    String sql = "SELECT first_name, last_name, email, phone, password FROM users WHERE email = ? OR phone = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, username);
        stmt.setString(2, username);

        ResultSet resultSet = stmt.executeQuery();

        if (resultSet.next()) {
            // Assuming User has a constructor like: User(int id, String name, String email, String phone)
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String email = resultSet.getString("email");
            String phone = resultSet.getString("phone");
            String password = resultSet.getString("password");

            return new User(firstName, lastName, email, phone, password);
        } else {
            System.out.println("No user found with email or phone: " + username);
            return null;
        }

    } catch (SQLException e) {
        System.err.println("Error finding user: " + e.getMessage());
        return null;
    }
}

    
}
