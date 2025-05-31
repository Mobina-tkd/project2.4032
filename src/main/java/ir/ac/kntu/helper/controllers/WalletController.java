package ir.ac.kntu.helper.controllers;

import java.time.Instant;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.TransactionDAO;
import ir.ac.kntu.dao.UserDAO;
import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.model.Transaction;
import ir.ac.kntu.model.User;

public class WalletController {

    public static void handleWallet(User user) {
        while (true) {
            Menu.walletMenu();
            Vendilo.WalletOption option = Menu.getUserWalletOption();

            switch (option) {
                case CURRETN_BALANCE -> {
                    double balance = UserDAO.getBalance(user);
                    System.out.println("Your current balance: " + ConsoleColors.GREEN + balance + ConsoleColors.RESET);
                }
                case CHARGE -> {
                    handelChargeWallet(user);
                }
                case RECENT_TRANSACTIONS -> {
                    handleRecentTransaction(user);
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println(ConsoleColors.RED + "Undefined Choice; Try again...\n" + ConsoleColors.RESET);
                }
                default -> throw new AssertionError();
            }
        }
    }

    public static void handleRecentTransaction(User user) {
        while (true) {
            Menu.transactionsMenu();
            Vendilo.ShowTransaction option = Menu.getTransactionOption();

            switch (option) {
                case SHOW_ALL -> {
                    TransactionDAO.showAllTransactions(user.getEmail());
                }
                case FILTER_BY_TIME -> {
                    handleFilterByTime(user.getEmail());
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println(ConsoleColors.RED + "Undefined Choice; Try again...\n" + ConsoleColors.RESET);
                }
                default -> throw new AssertionError();
            }
        }
    }

    public static void handleFilterByTime(String email) {
        System.out.print("Enter first date: ");
        String firstDate = ScannerWrapper.getInstance().nextLine();
        Instant start = Instant.parse(firstDate.trim());
        System.out.print("Enter second date: ");
        String secondDate = ScannerWrapper.getInstance().nextLine();
        Instant end = Instant.parse(secondDate.trim());
        TransactionDAO.printByDate(start, end, email);
    }

    public static void handelChargeWallet(User user) {
        while (true) {
            Menu.chargeWallet();
            Vendilo.ChargeWalletOption option = Menu.getChargeWalletOPtion();
            switch (option) {
                case CHARGE -> {
                    System.out.println("Enter the amount: ");
                    double balance = ScannerWrapper.getInstance().nextDouble();
                    user.getWallet().charge(balance);
                    UserDAO.updateBalance(balance, user, "+");
                    String date = ir.ac.kntu.helper.Calendar.now().toString();
                    Transaction transaction = new Transaction(balance, date, "Charge wallet");
                    TransactionDAO.insertTransaction(user.getEmail(), transaction);
                    return;
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println(ConsoleColors.RED + "Undefined Choice; Try again...\n" + ConsoleColors.RESET);
                }
                default -> throw new AssertionError();
            }
        }
    }
}
