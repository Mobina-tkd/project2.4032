package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.DiscountDAO;
import ir.ac.kntu.dao.UserDAO;
import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.helper.ValidationUtil;
import ir.ac.kntu.helper.readData.PersonFactory;
import ir.ac.kntu.model.User;

public class UserController {

    public static void chooseUserOption(User user) {
        while (true) {
            Menu.userMenu();
            Vendilo.UserOption option = Menu.getUserOption();
            switch (option) {
                case SEARCH_FOR_PRODUCTS -> {
                    SearchProductController.handleSearchProduct(user);
                }
                case SHOPPING_CART -> {
                    ShoppingCartController.handleShoppingCart(user);
                }
                case SETTING -> {
                    SettingController.handleSetting(user);
                }
                case RECENT_PURCHASES -> {
                    PurchaseController.handleUserPurchases(user);
                    break;
                }
                case ADRESSES -> {
                    AddressController.handleAddress(user);
                }
                case WALLET -> {
                    WalletController.handleWallet(user);
                    break;
                }
                case CUSTOMER_SUPPORT -> {
                    handleCostumerSupport(user);
                    break;
                }
                case DISCOUNTS -> {
                    handleDiscount(user);
                    break;
                }
                case VENDILO_PLUS -> {
                    VendiloPlusController.handleVendiloPlus(user);
                }
                case NOTIFICATION -> {
                    NotificationController.handleNotification(user);

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

    private static void handleCostumerSupport(User user) {
        while (true) {
            Menu.printSupportOptions();
            Vendilo.UserSupportOptions option = Menu.getUserSupportOption();
            switch (option) {
                case PRREVIOUS_REQUESTS -> {
                    RequestController.handlePreviousRequests(user.getEmail());
                }
                case REPORT -> {
                    RequestController.sendRequest(user.getEmail(), "Report");
                    break;
                }
                case WRONG_PRODUCT_SENT -> {
                    RequestController.sendRequest(user.getEmail(), "Wrong product sent");
                    break;
                }
                case SETTING -> {
                    RequestController.sendRequest(user.getEmail(), "Setting");
                    break;
                }
                case ORDER_NOT_RECEIVED -> {
                    RequestController.sendRequest(user.getEmail(), "Order not received");
                    break;
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

    public static User handleNewUser() {
        User user = new User();
        while (true) {
            user = PersonFactory.readUserData();
            Boolean inserted = UserDAO.insertUser(user);
            if (!inserted) {
                continue;
            }
            break;
        }
        while (true) {
            String username = LoginPageController.userLoginPage();
            if (username.equals("Back")) {
                return null;
            }
            if (username == null) {
                continue;
            }
            return user;
        }

    }

    public static void handleUserChoise() {
        while (true) {
            Menu.chooseStatementMenu();
            Vendilo.Statement statement = Menu.getStatementOption();
            User user = new User();
            switch (statement) {
                case NEW_USER -> {
                    user = handleNewUser();
                    if (user == null) {
                        continue;
                    }
                    UserController.chooseUserOption(user);
                }

                case ALREADY_HAS_ACCOUNT -> {
                    String username = "";
                    while (true) {
                        username = LoginPageController.userLoginPage();
                        if (username == null) {
                            continue;
                        } else if (username.equals("Back")) {
                            return;
                        }
                        break;
                    }
                    user = UserDAO.findUser(username);
                    UserController.chooseUserOption(user);
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

    private static void handleDiscount(User user) {
        while (true) {
            DiscountDAO.printDiscountPreview(user);
            System.out.println("Enter discount id to see more information\n press 0 to return : ");
            String option = ScannerWrapper.getInstance().nextLine();
            boolean isInt = ValidationUtil.isInteger(option);
            if (!isInt) {
                System.out.println("Invalid id");
                continue;
            }
            int intOption = Integer.parseInt(option);
            if (intOption == 0) {
                return;
            }
            DiscountDAO.printDiscountInformation(intOption);
        }

    }

}
