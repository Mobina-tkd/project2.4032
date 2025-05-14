package ir.ac.kntu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Supporter {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    private Map<String, Map<String, String>> admin;

    public Supporter() {
        admin = new HashMap<>();
        admin.put("admin1", Map.of(
                "firstName", "Sara",
                "lastName", "Hasani",
                "userName", "Sara_H82",
                "password", "S1382ara_"
        ));

        admin.put("admin2", Map.of(
                "firstName", "Mohammad",
                "lastName", "Lotfi",
                "userName", "mmd_L80",
                "password", "M1380md_"
        ));

    }




    public static boolean  loginPage() {
        System.out.print("Enter your username: ");
        String username = ScannerWrapper.getInstance().nextLine();
        System.out.print("Enter your password: ");
        String password = ScannerWrapper.getInstance().nextLine();

        if(username.equals("Sara_H82") && password.equals("S1382ara_")) {
            System.out.println("Welcome dear sara");
            return true;
        }else if(username.equals("mmd_L80") && password.equals("M1380md_")) {
            System.out.println("Welcome dear mohammad");
            return true;
        }else {
            System.out.println("Wrong username or password :( please try again...");
            return false;
        }

    }

    public static void chooseOption() {
        while(true) {
            Menu.supporterMenu();
            Vendilo.SupporterOption option = Menu.getSupporterOption();
            switch (option) {
                case FOLLOW_UP_REQUEST-> {
                }
                case IDENTITY_VERIFICATION-> {
                    handleIdentityVerification();
                }
                case RECENT_PURCHASES-> {
                }
                case BACK-> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\n");
                    break;
                }
            }
        }
    }


    public static void handleIdentityVerification() {
        while(true) {
        boolean printed = Seller.printSellersData();
        if(printed) {
            break;
        }   
        }
        System.out.println("Enter the agency code to confirm or deny: ");
        String agencyCode = ScannerWrapper.getInstance().nextLine();
        while(true) {
            boolean printed = Seller.printByAgencyCode(agencyCode);
            if(printed) {
                break;
            }   
        }
        Menu.varifiacationMenu();
        Vendilo.VarificationMenu option = Menu.getVarificationOption();
        switch (option) {
            case CONFIRM-> {
                handleConfirm(agencyCode);
            }
            case DENY-> {
                handleDeny(agencyCode);
            }
            case BACK-> {
                return;
            }
            case UNDEFINED-> {
                System.out.println("Undefined Choice; Try again...\n");
                    break;
            }
        }

    }

    private static void handleConfirm(String agencyCode) {
        setIdentityVarified(1, agencyCode);
        System.out.println("The seller confirmed");
    }

    private static void handleDeny(String agencyCode) {
        System.out.println("Enter your message: ");
        String message = ScannerWrapper.getInstance().nextLine();
        setIdentityVarified(2, agencyCode);
        setMessage(message, agencyCode);
    }

    private static void setIdentityVarified(int value, String agencyCode) {
        String sql = "UPDATE sellers SET identity_varified = ? WHERE agency_code = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, value);
        stmt.setString(2, agencyCode);

        int rowsUpdated = stmt.executeUpdate();
        
        if (rowsUpdated > 0) {
            System.out.println("Seller data updated successfully.");
        } else {
            System.out.println("No update was made.");
        }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setMessage(String message, String agencyCode) {
        String sql = "UPDATE sellers SET message = ? WHERE agency_code = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, message);
        stmt.setString(2, agencyCode);

        int rowsUpdated = stmt.executeUpdate();
        
        if (rowsUpdated > 0) {
            System.out.println("Seller data updated successfully.");
        } else {
            System.out.println("No update was made.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    }
    
}
