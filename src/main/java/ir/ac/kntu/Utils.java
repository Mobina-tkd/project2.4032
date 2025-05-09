package ir.ac.kntu;

import java.util.regex.*;

public class Utils {
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
    
}
