package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.SellerDAO;
import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.DealerCodeGenerator;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.helper.readData.PersonFactory;
import ir.ac.kntu.model.Seller;

public class SellerController {

    public static void chooseSellerOption(String agencyCode) {
        while (true) {
            Menu.sellerMenu();
            Vendilo.SellerOption option = Menu.getSellerOption();
            switch (option) {
                case PRODUCTS -> {
                    ProductController.handleProduct(agencyCode);
                }
                case WALLET -> {
                    handleWallet(agencyCode);
                    break;
                }
                case RECENT_PURCHASES -> {
                    PurchaseController.handleSellerPurchase(agencyCode);
                    break;
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\\n");
                }
                default -> throw new AssertionError();

            }
        }
    }

    public static String handleNewSeller() {
        String agencyCode;
        while (true) {
            Seller seller = PersonFactory.readSellerData();
            agencyCode = DealerCodeGenerator.generateUniqueCode();
            seller.setAgencyCode(agencyCode);
            Boolean inserted = SellerDAO.insertSeller(seller);
            if (inserted) {
                System.out.println("Here is your agency code : " +ConsoleColors.GREEN  + agencyCode + ConsoleColors.RESET);
                break;
            }
        }
        while (true) {
            agencyCode = LoginPageController.sellerLoginPage();
            if (agencyCode == null) {
                continue;
            }
            break;
        }
        return agencyCode;
    }

    public static void handleSellerChoise() {
        while (true) {
            Menu.chooseStatementMenu();
            Vendilo.Statement statement = Menu.getStatementOption();
            switch (statement) {
                case NEW_USER -> {
                    String agencyCode = handleNewSeller();
                    if ("Back".equals(agencyCode)) {
                        continue;
                    }
                    chooseSellerOption(agencyCode);
                }
                case ALREADY_HAS_ACCOUNT -> {
                    String agencyCode;
                    while (true) {
                        agencyCode = LoginPageController.sellerLoginPage();
                        if (agencyCode == null) {
                            continue;
                        } else if ("Back".equals(agencyCode)) {
                            return;
                        }
                        break;
                    }
                    chooseSellerOption(agencyCode);
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println(ConsoleColors.RED +"Undefined Choice; Try again...\n" + ConsoleColors.RESET);
                    break;
                }
                default -> throw new AssertionError();
            }
        }
    }


    private static void handleWallet(String agencyCode) {
        while (true) { 
            Menu.printSellerMenu();
            Vendilo.SellerWalletOption option = Menu.getSellerWalletOption();
            switch (option) {
                case CURRETN_BALANCE-> {
                    double currentBalance = SellerDAO.getBalance(agencyCode);
                    System.out.println("You hava " +ConsoleColors.GREEN + currentBalance + ConsoleColors.RESET+ " dollars in your wallet");
                }
                case WITHDRAW-> {
                    System.out.print("Enter the amount you want to withdraw: ");
                    double amount = ScannerWrapper.getInstance().nextDouble();
                    SellerDAO.withdrawMoney(amount, agencyCode);
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println(ConsoleColors.RED +"Undefined Choice; Try again...\n" + ConsoleColors.RESET);
                    break;
                }
                default -> throw new AssertionError();
            }
        }

    }

}
