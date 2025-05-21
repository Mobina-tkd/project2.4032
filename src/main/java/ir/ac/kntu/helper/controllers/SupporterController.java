package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.PurchasesDAO;
import ir.ac.kntu.dao.SellerDAO;
import ir.ac.kntu.helper.ScannerWrapper;

public class SupporterController {

    public static void chooseSupporterOption() {
        while(true) {
            Menu.supporterMenu();
            Vendilo.SupporterOption option = Menu.getSupporterOption();
            switch (option) {
                case FOLLOW_UP_REQUEST-> {
                    RequestController.handleRequest();
                }
                case IDENTITY_VERIFICATION-> {
                    handleIdentityVerification();
                }
                case RECENT_PURCHASES-> {
                    PurchaseController.handleSupporterPurchase();
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
            boolean printed = SellerDAO.printSellersData();
            if(!printed) {
                return;
            }
            System.out.println("Enter the agency code to confirm or deny: ");
            agencyCode = ScannerWrapper.getInstance().nextLine();
                boolean canPrint = SellerDAO.printByAgencyCode(agencyCode);
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
        SellerDAO.setIdentityVarified(1, agencyCode);
        System.out.println("The seller confirmed");
    }

    private static void handleDeny(String agencyCode) {
        System.out.println("Enter your message: ");
        String message = ScannerWrapper.getInstance().nextLine();
        SellerDAO.setIdentityVarified(2, agencyCode);
        SellerDAO.setMessage(message, agencyCode);
    }

    
}
