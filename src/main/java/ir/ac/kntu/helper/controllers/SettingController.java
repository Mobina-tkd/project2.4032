package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.UserDAO;
import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.helper.readData.PersonFactory;
import ir.ac.kntu.model.User;

public class SettingController {

    public static void handleSetting(User user) {
        while (true) {
            String newValue;
            Menu.settingMenu();
            Vendilo.SettingMenu settingMenue = Menu.getSettingMenu();
            switch (settingMenue) {
                case EMAIL -> {
                    newValue = PersonFactory.readEmail();
                    UserDAO.setAndUpdateUserData(user, "email", newValue);
                }
                case PHONE_NUMBER -> {
                    newValue = PersonFactory.readPhoneNUmber();
                    UserDAO.setAndUpdateUserData(user, "phone_number", newValue);

                }
                case FIRST_NAME -> {
                    System.out.print("Enter first name: ");
                    newValue = ScannerWrapper.getInstance().nextLine();
                    UserDAO.setAndUpdateUserData(user, "first_name", newValue);

                }
                case LAST_NAME -> {
                    System.out.print("Enter last name: ");
                    newValue = ScannerWrapper.getInstance().nextLine();
                    UserDAO.setAndUpdateUserData(user, "last_name", newValue);

                }
                case PASSWORD -> {
                    newValue = PersonFactory.readPassword();
                    UserDAO.setAndUpdateUserData(user, "password", newValue);

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
