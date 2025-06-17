package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.ManagerDAO;
import ir.ac.kntu.dao.SupporterDAO;
import ir.ac.kntu.dao.UserDAO;
import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.helper.readData.PersonFactory;
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
                }
                case ANALYSE_USER_FUNCTION -> {
                }
                case CREATING_DISCOUNT_CODE -> {
                }
                case GENERAL_MESSAGE -> {
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
                }
                case BLOCK_SUPPORTER -> {
                }
                case BLOCK_MANAGER -> {
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

    private static void handleEditingUser() {
        while (true) {
            System.out.print("Enter username to change data(press 0 to return): ");
            String username = ScannerWrapper.getInstance().nextLine();
            if (username.equals("0")) {
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
            String newValue;
            Menu.edditingMenu();
            Vendilo.EdditingMenu option = Menu.getEdditingOption();
            switch (option) {

                case NAME -> {
                    System.out.print("Enter name: ");
                    newValue = ScannerWrapper.getInstance().nextLine();
                    SupporterDAO.updateSupporterAndManagerData(username, userType, "name", newValue);

                }
                case USERNAME -> {
                    System.out.print("Enter username: ");
                    newValue = ScannerWrapper.getInstance().nextLine();
                    SupporterDAO.updateSupporterAndManagerData(username, userType, "username", newValue);

                }
                case PASSWORD -> {
                    newValue = PersonFactory.readPassword();
                    SupporterDAO.updateSupporterAndManagerData(username, userType, "password", newValue);

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

    private static String getUsername(String userType) {
        while (true) {
            System.out.print("Enter username to change data(press 0 to return): ");
            String username = ScannerWrapper.getInstance().nextLine();
            if (username.equals("0")) {
                return "";
            }
            if (!ManagerDAO.userExist(userType, username)) {
                continue;
            }
            return username;
        }
    }

}
