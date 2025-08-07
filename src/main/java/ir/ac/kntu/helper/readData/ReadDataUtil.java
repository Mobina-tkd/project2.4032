package ir.ac.kntu.helper.readData;

import ir.ac.kntu.dao.DiscountDAO;
import ir.ac.kntu.dao.UserDAO;
import ir.ac.kntu.dao.VendiloPlusDAO;
import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.helper.ValidationUtil;
import ir.ac.kntu.model.Address;
import ir.ac.kntu.model.User;

public class ReadDataUtil {

    public static Address readAddress() {
        System.out.print("\nEnter the location: ");
        String location = ScannerWrapper.getInstance().nextLine();
        System.out.print("Enter the state: ");
        String state = ScannerWrapper.getInstance().nextLine();
        System.out.print("Enter the street: ");
        String street = ScannerWrapper.getInstance().nextLine();
        System.out.print("Enter the houseNumber: ");
        String houseNumber = ScannerWrapper.getInstance().nextLine();

        return new Address(location, state, street, houseNumber);
    }

    public static void handleCreatingDiscountCode() {
        System.out.println("Enter discount type(percent/amount): ");
        String type = getDiscountType();
        System.out.println("Enter discount code: ");
        String code = ScannerWrapper.getInstance().nextLine();
        System.out.println("Enter percent or amount of discount: ");
        String amount = "";
        while (true) {
            amount = ScannerWrapper.getInstance().nextLine();
            if (ValidationUtil.isDouble(amount)) {
                break;
            }
        }
        UserDAO.setDiscountForAllUsers(type, code, Double.valueOf(amount));
    }

    public static void createDiscountForOneUser() {
        int userId = getUserId();
        String type = getDiscountType();
        String code = getDiscountCode();
        int usedTime = getUsedTime();
        double amount = getDiscountAmount();

        String email = UserDAO.findEmailById(userId);
        DiscountDAO.insertDiscount(email, type, code, amount, usedTime);
    }

    private static int getUserId() {
        String userId;
        while (true) {
            System.out.print("Enter user id: ");
            userId = ScannerWrapper.getInstance().nextLine();
            if (ValidationUtil.isInteger(userId)) {
                return Integer.parseInt(userId);
            }
            System.out.println("Invalid input. Please enter a valid integer.");
        }
    }

    private static String getDiscountType() {
        while (true) {
            System.out.print("Enter discount type (percent/amount): ");
            String type = ScannerWrapper.getInstance().nextLine().toLowerCase();
            if ("percent".equals(type) || "amount".equals(type)) {
                return type;
            }
            System.out.println("Invalid type. Please enter 'percent' or 'amount'.");
        }
    }

    private static String getDiscountCode() {
        System.out.print("Enter discount code: ");
        return ScannerWrapper.getInstance().nextLine();
    }

    private static int getUsedTime() {
        String usedTime;
        while (true) {
            System.out.print("Enter used time: ");
            usedTime = ScannerWrapper.getInstance().nextLine();
            if (ValidationUtil.isInteger(usedTime)) {
                return Integer.parseInt(usedTime);
            }
            System.out.println("Invalid input. Please enter a valid integer.");
        }
    }

    private static double getDiscountAmount() {
        String amount;
        while (true) {
            System.out.print("Enter percent or amount of discount: ");
            amount = ScannerWrapper.getInstance().nextLine();
            if (ValidationUtil.isDouble(amount)) {
                return Double.parseDouble(amount);
            }
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }

    public static int readId() {
        while (true) {
            System.out.print("Enter supporter id to modify access(press 0 to return): ");
            String stringId = ScannerWrapper.getInstance().nextLine();
            if ("0".equals(stringId)) {
                return 0;
            }
            if (ValidationUtil.isInteger(stringId)) {
                return Integer.parseInt(stringId);
            }
            System.out.println(ConsoleColors.RED + "Invalid input, please try again" + ConsoleColors.RESET);
        }
    }

    public static double getReward() {
        while (true) {
            System.out.println("Enter amount to reward seller(press 0 to return)");
            String reward = ScannerWrapper.getInstance().nextLine();
            if ("0".equals(reward)) {
                return 0;
            }
            if (ValidationUtil.isDouble(reward)) {
                return Double.parseDouble(reward);
            }
            System.out.println(ConsoleColors.RED + "Invalid input, please try again" + ConsoleColors.RESET);
        }
    }

    public static void readDataForVendilo() {
        String userId = "";
        while (true) {
            System.out.print("Enter user id:");
            userId = ScannerWrapper.getInstance().nextLine();
            if (ValidationUtil.isInteger(userId)) {
                break;
            }
        }
        String duration = "";
        while (true) {
            System.out.print("Enter subscription duration:");
            duration = ScannerWrapper.getInstance().nextLine();
            if (ValidationUtil.isInteger(duration)) {
                break;
            }
        }

        String email = UserDAO.findEmailById(Integer.parseInt(userId));
        User user = UserDAO.findUser(email);
        VendiloPlusDAO.insertToClub(user, Integer.parseInt(duration));

    }

}
