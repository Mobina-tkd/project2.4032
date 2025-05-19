package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.model.User;

public class WalletController {

    public static void handelChargeWallet(User user) {
        while (true) { 
            Menu.chargeWallet();
            Vendilo.chargeWalletOption option = Menu.getChargeWalletOPtion();
            switch (option) {
                case CHARGE -> {
                    System.out.println("Enter the amount: ");
                    double balance = ScannerWrapper.getInstance().nextDouble();
                    user.getWallet().charge(balance);
                    return;
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\n");
                }
            }  
        }
    }   
}
