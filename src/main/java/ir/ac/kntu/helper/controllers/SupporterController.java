package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.SellerDAO;
import ir.ac.kntu.dao.SupporterDAO;
import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.ScannerWrapper;

public class SupporterController {

    public static void chooseSupporterOption(String username) {
        while (true) {
            Menu.supporterMenu();
            Vendilo.SupporterOption option = Menu.getSupporterOption();
            switch (option) {
                case FOLLOW_UP_REQUEST -> {
                    RequestController.handleRequest(username);
                }
                case IDENTITY_VERIFICATION -> {
                    handleIdentityVerification(username);
                }
                case RECENT_PURCHASES -> {
                    PurchaseController.handleSupporterPurchase(username);
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println(ConsoleColors.RED + "Undefined Choice; Try again...\n" + ConsoleColors.RESET);
                    break;
                }
                default -> throw new AssertionError();
            }
        }
    }

    public static void handleIdentityVerification(String username) { // fix back option here
        String agencyCode;
        if (!SupporterDAO.hasAccess("Identity_verification", username)) {
            System.out.println(ConsoleColors.RED + "You dont hava access" + ConsoleColors.RESET);
            return;
        }

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
                    System.out.println(ConsoleColors.RED + "Undefined Choice; Try again...\n" + ConsoleColors.RESET);
                    break;
                }
                default -> throw new AssertionError();
            }
        }
    }

    private static void handleConfirm(String agencyCode) {
        SellerDAO.setIdentityVerified(1, agencyCode);
        System.out.println(ConsoleColors.GREEN + "The seller confirmed" + ConsoleColors.RESET);
    }

    private static void handleDeny(String agencyCode) {
        System.out.print("Enter your message: ");
        String message = "Your identity denied :(  Supporter message: " + ScannerWrapper.getInstance().nextLine();
        SellerDAO.setIdentityVerified(2, agencyCode);
        SellerDAO.setMessage(message, agencyCode);
    }

}
