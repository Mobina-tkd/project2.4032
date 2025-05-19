package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.PurchasesDAO;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.model.User;

public class PurchaseController {

    public static void handlePurchases(User user) {
        while(true) {
            PurchasesDAO.printUserPUrchases(user);
            Menu.purchaseMenu();
            Vendilo.PurchaseMenu option = Menu.getPurchaseOption();

            switch (option) {
                case INFO -> {
                    System.out.println("Enter the id of your purchase to see more information");
                    int id = ScannerWrapper.getInstance().nextInt();
                    PurchasesDAO.printAllInfoById(id);
                }
                case RATE -> {
                    System.out.println("Enter the id of your purchase to rate");
                    int id = ScannerWrapper.getInstance().nextInt();
                    ratePurchase(id);
                }       
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\\n");
                }    
             }

        }
    }
    
}
