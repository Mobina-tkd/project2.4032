package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.SellerDAO;
import ir.ac.kntu.helper.ScannerWrapper;

public class SupporterController {

    public static void chooseSupporterOption() {
        while (true) {
            Menu.supporterMenu();
            Vendilo.SupporterOption option = Menu.getSupporterOption();
            switch (option) {
                case FOLLOW_UP_REQUEST -> {
                    RequestController.handleRequest();
                }
                case IDENTITY_VERIFICATION -> {
                    handleIdentityVerification();
                }
                case RECENT_PURCHASES -> {
                    PurchaseController.handleSupporterPurchase();
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\n");
                    break;
                }
                default -> throw new AssertionError();
            }
        }
    }

    public static void handleIdentityVerification() { // fix back option here
        String agencyCode;

        while (true) {
            boolean printed = SellerDAO.printSellersData();
            if (!printed) {
                return;
            }
            System.out.print("Enter the agency code to confirm or deny: ");
            agencyCode = ScannerWrapper.getInstance().nextLine();
            boolean canPrint = SellerDAO.printByAgencyCode(agencyCode);
            if (!canPrint) {
                return;
            }
            
        
            Menu.verificationMenu();
            Vendilo.VarificationMenu option = Menu.getVarificationOption();
            switch (option) {
                case CONFIRM -> {
                    handleConfirm(agencyCode);
                }
                case DENY -> {
                    handleDeny(agencyCode);
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\n");
                    break;
                }
                default -> throw new AssertionError();
            }
        }
    }

    private static void handleConfirm(String agencyCode) {
        SellerDAO.setIdentityVerified(1, agencyCode);
        System.out.println("The seller confirmed");
    }

    private static void handleDeny(String agencyCode) {
        System.out.print("Enter your message: ");
        String message = "Your identity denied :(  Supporter message: " + ScannerWrapper.getInstance().nextLine();
        SellerDAO.setIdentityVerified(2, agencyCode);
        SellerDAO.setMessage(message, agencyCode);
    }

}
