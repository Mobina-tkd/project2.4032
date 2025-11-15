package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.ManagerDAO;
import ir.ac.kntu.dao.PurchasesDAO;
import ir.ac.kntu.dao.SellerDAO;
import ir.ac.kntu.dao.SupporterDAO;
import ir.ac.kntu.dao.UserDAO;
import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.helper.ValidationUtil;
import ir.ac.kntu.helper.readData.PersonFactory;
import ir.ac.kntu.helper.readData.ReadDataUtil;
import ir.ac.kntu.model.User;

public class ManagerController {
    public static void handleManagerOption() {
        while (true) {
            Menu.managerMenu();
            Vendilo.ManagerOptions option = Menu.getManagerOption();
            switch (option) {
                case MANAGING_USERS -> {
                    handleManagingUser();
                }
                case ANALYSIS_SELLER_FUNCTION -> {
                    handleSellerFunction();
                }
                case ANALYSE_USER_FUNCTION -> {
                    handleUserFunction();
                }
                case CREATING_DISCOUNT_CODE -> {
                    ReadDataUtil.handleCreatingDiscountCode();
                }
                case GENERAL_MESSAGE -> {
                    handleWrittigMessage();
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

    public static void handleManagingUser() {
        while (true) {
            Menu.managingUserMenu();
            Vendilo.ManagingUserOption option = Menu.getManagingUserOption();
            switch (option) {

                case CREATE_MANAGER -> {
                    PersonFactory.readSupporterAndManagerData("managers");
                }
                case CREATE_SUPPORTER -> {
                    PersonFactory.readSupporterAndManagerData("supporters");
                }
                case EDIT_USER -> {
                    handleEditingUser();
                }
                case EDIT_MANAGER -> {
                    handleEditingSupporterAndManager("managers");
                }
                case EDIT_SUPPORTER -> {
                    handleEditingSupporterAndManager("supporters");
                }
                case BLOCK_USER -> {
                    handleBlockingUser();
                }
                case BLOCK_SUPPORTER -> {
                    handleBlockingManagerAndSupporter("supporters");
                }
                case BLOCK_MANAGER -> {
                    handleBlockingManagerAndSupporter("supporters");
                }
                case SHOW_USERS -> {
                    handleDisplyingUsers();
                }
                case SUPPORTER_LIMITATION -> {
                    handleSupporterLimitation();
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

    private static void handleBlockingUser() {
        while (true) {
            System.out.print("Enter email or phone number to block(press 0 to return): ");
            String username = ScannerWrapper.getInstance().nextLine();
            if ("0".equals(username)) {
                return;
            }
            User user = UserDAO.findUser(username);
            if (user == null) {
                continue;
            }
            UserDAO.blockUser(user.getEmail());
            System.out.println("here");
            break;
        }
    }

    private static void handleBlockingManagerAndSupporter(String usertype) {
        while (true) {
            System.out.print("Enter username to block(press 0 to return): ");
            String username = ScannerWrapper.getInstance().nextLine();
            if ("0".equals(username)) {
                return;
            }
            if (!ManagerDAO.userExist(usertype, username)) {
                continue;
            }
            ManagerDAO.blockManagerAndSupporter(username, usertype);
        }
    }

    private static void handleEditingUser() {
        while (true) {
            System.out.print("Enter username to change data(press 0 to return): ");
            String username = ScannerWrapper.getInstance().nextLine();
            if ("0".equals(username)) {
                return;
            }
            User user = UserDAO.findUser(username);
            if (user == null) {
                continue;
            }
            SettingController.handleSetting(user);
        }
    }

    private static void handleEditingSupporterAndManager(String userType) {

        while (true) {
            String username = getUsername(userType);
            if (username == null) {
                return;
            }
            Menu.edditingMenu();
            Vendilo.EdditingMenu option = Menu.getEdditingOption();
            switch (option) {

                case NAME -> {
                    handleName(username, userType);
                }
                case USERNAME -> {
                    handleUsername(username, userType);
                }
                case PASSWORD -> {
                    handlePassword(username, userType);

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

    private static void handleName(String username, String userType) {
        System.out.print("Enter name: ");
        String newValue = ScannerWrapper.getInstance().nextLine();
        SupporterDAO.updateSupporterAndManagerData(username, userType, "name", newValue);

    }

    private static void handleUsername(String username, String userType) {
        System.out.print("Enter username: ");
        String newValue = ScannerWrapper.getInstance().nextLine();
        SupporterDAO.updateSupporterAndManagerData(username, userType, "username", newValue);

    }

    private static void handlePassword(String username, String userType) {
        String newValue = PersonFactory.readPassword();
        SupporterDAO.updateSupporterAndManagerData(username, userType, "password", newValue);

    }

    private static String getUsername(String userType) {
        while (true) {
            System.out.print("Enter username to change data(press 0 to return): ");
            String username = ScannerWrapper.getInstance().nextLine();
            if ("0".equals(username)) {
                return "";
            }
            if (!ManagerDAO.userExist(userType, username)) {
                continue;
            }
            return username;
        }
    }

    private static void handleDisplyingUsers() {
        while (true) {
            Menu.showUserMenu();
            Vendilo.ShowUsersOption option = Menu.getUserType();
            switch (option) {
                case NORMAL_USER -> {
                    UserController.handleShowingUsers();
                }
                case SUPPORTER -> {
                    handleShowingSupporterAndManager("supporters");
                }
                case MANAGER -> {
                    handleShowingSupporterAndManager("managers");
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

    private static void handleShowingSupporterAndManager(String userType) {
        while (true) {
            Menu.displayUserOption();
            Vendilo.DisplayingUserOption option = Menu.getDisplayingTypeOption();
            switch (option) {
                case SHOW_ALL -> {
                    SupporterDAO.displayAllManagerAndSupporter(userType);
                }
                case SEARCH_BY_USERNAME -> {
                    handleShowinByUsername(userType);
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

    private static void handleShowinByUsername(String userType) {
        while (true) {
            System.out.print("Enter username (press 0 to return): ");
            String username = ScannerWrapper.getInstance().nextLine();
            if ("0".equals(username)) {
                return;
            }
            if (!ManagerDAO.userExist(userType, username)) {
                continue;
            }
            ManagerDAO.printByUsername(userType, username);
        }
    }

    private static void handleWrittigMessage() {
        System.out.println("Write you message for users(press 0 to return): ");
        String message = ScannerWrapper.getInstance().nextLine();
        if ("0".equals(message)) {
            return;
        }
        UserDAO.setMessageForAllUsers(message);

    }

    private static void handleSupporterLimitation() {
        while (true) {
            SupporterDAO.printSupporterLimitation();
            int supporterId = ReadDataUtil.readId();
            if (supporterId == 0) {
                return;
            }
            Menu.supporterMenu();
            Vendilo.SupporterOption option = Menu.getSupporterOption();
            switch (option) {
                case FOLLOW_UP_REQUEST -> {
                    handleChangingAccess("Follow_up_request", supporterId);
                }
                case RECENT_PURCHASES -> {
                    handleChangingAccess("Recent_purchases", supporterId);
                }
                case IDENTITY_VERIFICATION -> {
                    handleChangingAccess("Identity_verification", supporterId);
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

    private static void handleChangingAccess(String accessType, int supporterId) {
        while (true) {
            Menu.accessMenu();
            Vendilo.AccessOption option = Menu.getAccessOption();
            switch (option) {
                case GIVE_ACCESS -> {
                    SupporterDAO.mofifyAccess(accessType, supporterId, 1);
                }
                case REVOKE -> {
                    SupporterDAO.mofifyAccess(accessType, supporterId, 0);
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
    

    private static void handleSellerFunction() {
        while (true) {
            Menu.sellerFunctionMenu();
            Vendilo.SellerFunction option = Menu.getSellerFunction();
            switch (option) {
                case SHOW_SELLERS_FUNCTION -> {
                    SellerDAO.printSellerFunction();
                }
                case REWARD_SELLER -> {
                    handleRewardingSeller();
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

    private static void handleUserFunction() {
        while (true) {
            Menu.userFunctionMenu();
            Vendilo.UserFunction option = Menu.getUserFunction();
            switch (option) {
                case SHOW_USERS_FUNCTION -> {
                    UserDAO.printUserFunction();
                }
                case GIVE_DISCOUNT_CODE -> {
                    ReadDataUtil.createDiscountForOneUser();
                }
                case ADD_TO_VENDILO_PLUS -> {
                    ReadDataUtil.readDataForVendilo();
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

    private static void handleRewardingSeller() {
        String sellerId = null;
        while (true) {
            System.out.print("Enter seller id (press 0 to return): ");
            sellerId = ScannerWrapper.getInstance().nextLine();
            if (!ValidationUtil.isInteger(sellerId)) {
                continue;
            }
            if ("0".equals(sellerId)) {
                return;
            }
            break;
        }

        System.out.printf("Last month income: %f%n",
                PurchasesDAO.findLastMonthTransaction(Integer.parseInt(sellerId), "seller")); // do this one
        double reward = ReadDataUtil.getReward();
        if (reward == 0) {
            return;
        }
        SellerDAO.rewardSeller(Integer.parseInt(sellerId), reward);

    }

}
