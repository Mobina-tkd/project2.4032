package ir.ac.kntu.helper.HandleModels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.model.Seller;

public class HandleSupporter {
    private static final String DB_URL = "jdbc:sqlite:data.db";

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


    public static void handleIdentityVerification() { //fix back option here
        String agencyCode;
        
        while(true) {
            boolean printed = Seller.printSellersData();
            if(!printed) {
                return;
            }
            System.out.println("Enter the agency code to confirm or deny: ");
            agencyCode = ScannerWrapper.getInstance().nextLine();
                boolean canPrint = Seller.printByAgencyCode(agencyCode);
                if(!canPrint) {
                    return;
                }   
                break;
        }
        Menu.verificationMenu();
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
