package ir.ac.kntu.helper.readData;

import ir.ac.kntu.dao.ManagerDAO;
import ir.ac.kntu.dao.SupporterDAO;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.helper.ValidationUtil;
import ir.ac.kntu.model.Seller;
import ir.ac.kntu.model.User;

public class PersonFactory {
    public static User readUserData() {

        System.out.print("\nEnter first name: ");
        String firstName = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter last name: ");
        String lastName = ScannerWrapper.getInstance().nextLine();

        String email = readEmail();
        String password = readPassword();
        String phoneNumber = readPhoneNUmber();

        return new User(firstName, lastName, email, phoneNumber, password);
    }

    public static Seller readSellerData() {

        System.out.print("\nEnter first name: ");
        String firstName = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter last name: ");
        String lastName = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter store name: ");
        String storName = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter state: ");
        String state = ScannerWrapper.getInstance().nextLine();

        String idNumber = readIdNumber();

        String phoneNumber = readPhoneNUmber();
        String password = readPassword();

        return new Seller(firstName, lastName, idNumber, storName, state, phoneNumber, password);
    }

    public static void readSupporterAndManagerData(String userType) {
        System.out.print("\nEnter your name: ");
        String name = ScannerWrapper.getInstance().nextLine();

        System.out.print("Enter username: ");
        String username = ScannerWrapper.getInstance().nextLine();

        String password = readPassword();

        if(userType.equals("supporter")) {
            SupporterDAO.insertSopporter(name, username, password);
        }
        if(userType.equals("manager")) {
            ManagerDAO.insertManager(name, username, password);
        }

    }

    public static String readPassword() {
        while (true) {
            System.out.print("Enter password: ");
            String password = ScannerWrapper.getInstance().nextLine();
            if (!ValidationUtil.matchPassword(password)) {
                System.out.println("Weak password");
                continue;
            }

            return password;

        }
    }

    public static String readEmail() {
        while (true) {
            System.out.print("Enter email: ");
            String email = ScannerWrapper.getInstance().nextLine();
            if (!ValidationUtil.matchEmail(email)) {
                System.out.println("Invalid email address");
                continue;
            }
            return email;
        }

    }

    public static String readPhoneNUmber() {
        while (true) {
            System.out.print("Enter phone number: ");
            String phoneNumber = ScannerWrapper.getInstance().nextLine();
            if (!ValidationUtil.matchNumber(phoneNumber)) {
                System.out.println("Invalid phone number");
                continue;
            }
            return phoneNumber;
        }
    }

    private static String readIdNumber() {
        while(true) {
            System.err.println("Enter your id number: "); 
            String idNumber = ScannerWrapper.getInstance().nextLine();
            if (!ValidationUtil.matchIdNumber(idNumber)) {
                System.out.println("Invalid phone id number");
                continue;
            }
            return idNumber;
        }


    }

    

}
