package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.TransactionDAO;
import ir.ac.kntu.dao.UserDAO;
import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.model.Transaction;
import ir.ac.kntu.model.User;

public class VendiloPlusController {

    public static void handleVendiloPlus(User user) {
        while (true) {
            Menu.vendiloPlusMenu();
            Vendilo.VendiloPlus option = Menu.getVendiloPlus();
            switch (option) {
                case BUY_SUBSCRIPTION -> {
                    handleBuyingSub(user);
                }
                case MY_SUBSCRIPTION -> {
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println(ConsoleColors.RED + "Undefined Choice" + ConsoleColors.RESET);
                    break;
                }
                default -> throw new AssertionError();
            }
        }
    }

    public static void handleBuyingSub(User user) {
        while (true) {
            Menu.subscriptionMenu();
            Vendilo.Subscription option = Menu.getSubscription();
            switch (option) {
                case ONE_MONTH -> {
                    handleBuyingSub(40, user);
                }
                case THREE_MONTH -> {
                    handleBuyingSub(100, user);
                }
                case ONE_YEAR -> {
                    handleBuyingSub(350, user);
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println(ConsoleColors.RED + "Undefined Choice" + ConsoleColors.RESET);
                }
                default -> throw new AssertionError();
            }
        }
    }

    private static void handleBuyingSub(int price, User user) {
        while (true) {
            boolean bought = UserDAO.getBalance(user) > price;
            if (bought) {
                UserDAO.updateBalance(price, user, "-");
                String date = ir.ac.kntu.helper.Calendar.now().toString();
                Transaction transaction = new Transaction(price, date, "withdraw");
                TransactionDAO.insertTransaction(user.getEmail(), transaction);
                System.err.println(ConsoleColors.BLUE + "Welcome to vendilo plus club dear user" + ConsoleColors.RESET);
                return;
            } else {
                System.out.println(ConsoleColors.RED + "There is not enough money in your wallet "
                        + ConsoleColors.RESET + ":(");
                WalletController.handelChargeWallet(user);
                return;
            }
        }
    }
}
