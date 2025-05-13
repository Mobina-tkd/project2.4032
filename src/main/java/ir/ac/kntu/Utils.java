package ir.ac.kntu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {
    private static final String DB_URL = "jdbc:sqlite:data.db";
    public static User readUserData(){
    
    String email = readEmail();
    String password = readPassword();
    String phoneNumber = readPhoneNUmber();
    System.out.print("Enter first name: ");
    String firstName = ScannerWrapper.getInstance().nextLine();
       
    System.out.print("Enter last name: ");
    String lastName = ScannerWrapper.getInstance().nextLine();
    
    User user = new User(firstName, lastName, email, phoneNumber, password);
    return user;
    }



    public static Seller readSellerData(){

        String firstName;
        String lastName;
        String storName;
        String IDNumber;
        String phoneNumber = readPhoneNUmber();
        String password = readPassword();
        String state;


        
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
        
       
        
        
    
        Seller seller = new Seller(firstName, lastName, IDNumber, storName, state, phoneNumber, password);
        return seller;
        }

        public static String readPassword() {
            while(true){
                System.out.print("Enter password: ");
                String password = ScannerWrapper.getInstance().nextLine();
                if(!matchPassword(password)){
                    System.out.println("Weak password"); 
                    continue;   
                }

                return password;
        
            }
        }

        public static String readEmail() {
            while(true){
                System.out.print("Enter email: ");
                String email = ScannerWrapper.getInstance().nextLine();
                if(!matchEmail(email)){
                    System.out.println("Invalid email address");
                    continue;
                }
                return email;
            }
        
        }

    
        public static String readPhoneNUmber() {
            while(true){
                System.out.print("Enter phone number: ");
                String phoneNumber = ScannerWrapper.getInstance().nextLine();
                if(!matchNumber(phoneNumber)){
                    System.out.println("Invalid phone number");
                    continue;
                }
                return phoneNumber;
            }
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

    public static void searchProduct() {
        Menu.productCategoryMenu();
        Vendilo.Product product = Menu.getProductCategory();

        switch (product) {
            case LAPTOP -> {
                Laptop.search();
            }
            case MOBILE -> {
                Mobile.search();
            }
            case BOOK -> {
                Book.search();
            }
            case UNDEFINED -> {
                System.out.println("Undefined Choice; Try again...\n");
                break;            }        
        }

    }

    public static void showAllProducts(String name) {
        String tableName = name;

        String query = "SELECT * FROM " + tableName;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Get number of columns
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            // skip printing name
            while (rs.next()) {
                for (int i = 2; i <= columnCount; i++) {
                    System.out.print(meta.getColumnName(i) + ": " + rs.getString(i) + "\t");
                }
                System.out.println(); 
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public static void searchByPrice(String name, double min, double max) {
        String query = "SELECT * FROM " + name + " WHERE price BETWEEN ? AND ?";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setDouble(1, min);
        pstmt.setDouble(2, max);

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
            System.out.println("No products found in the price range " + min + " to " + max);
        }

    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }

    }


    public static void setAndUpdate(User user, String field, String newValue) {
        
        String sql = "UPDATE users SET " + field + " = ? WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, newValue);
        stmt.setString(2, user.getEmail()); 

        int rowsUpdated = stmt.executeUpdate();

        if (rowsUpdated > 0) {
            System.out.println("User data updated successfully.");
        } else {
            System.out.println("No update was made.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    switch (field) {
        case "email" -> {
            user.setEmail(newValue);;  
        }
        case "phone_number" -> {
            user.setPhoneNumber(newValue);;
        }
        case "first_name"-> {
            user.setFirstName(newValue);;
        }
        case "last_name" -> {
            user.setLastName(newValue);;
        }
        case "password" -> {
            user.setPassword(newValue);
        }
        default -> throw new AssertionError();
    }

    }

    
}
