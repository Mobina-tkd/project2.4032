package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.PurchasesDAO;
import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.model.User;

public class PurchaseController {

    public static void handleUserPurchases(User user) {
        while (true) {
            PurchasesDAO.printUserPurchases(user);
            Menu.purchaseMenu();
            Vendilo.PurchaseMenu option = Menu.getPurchaseOption();

            switch (option) {
                case INFO -> {
                    System.out.println("Enter the id of your purchase to see more information");
                    int purchaseId = ScannerWrapper.getInstance().nextInt();
                    PurchasesDAO.printAlldetailsOfPurchase(purchaseId);
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println(ConsoleColors.RED +"Undefined Choice; Try again...\n" + ConsoleColors.RESET);
                }
                default -> throw new AssertionError();
            }
        }
    }

    public static void handleSupporterPurchase() {
        while (true) {
            PurchasesDAO.printAllPurchases();
            Menu.purchaseMenu();
            Vendilo.PurchaseMenu option = Menu.getPurchaseOption();

            switch (option) {
                case INFO -> {
                    System.out.println("Enter the id of your purchase to see more information");
                    int purchaseId = ScannerWrapper.getInstance().nextInt();
                    PurchasesDAO.printAlldetailsOfPurchase(purchaseId);
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println(ConsoleColors.RED +"Undefined Choice; Try again...\n" + ConsoleColors.RESET);
                }
                default -> throw new AssertionError();
            }
        }
    }

    public static void handleSellerPurchase(String agencyCode) {
        while (true) {
            PurchasesDAO.printAllSellerPurchases(agencyCode);
            Menu.purchaseMenu();
            Vendilo.PurchaseMenu option = Menu.getPurchaseOption();

            switch (option) {
                case INFO -> {
                    System.out.println("Enter the id of your purchase to see more information");
                    int purchaseId = ScannerWrapper.getInstance().nextInt();
                    PurchasesDAO.printAlldetailsOfPurchase(purchaseId);
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println(ConsoleColors.RED +"Undefined Choice; Try again...\n" + ConsoleColors.RESET);
                }
                default -> throw new AssertionError();
            }
        }
    }
}
