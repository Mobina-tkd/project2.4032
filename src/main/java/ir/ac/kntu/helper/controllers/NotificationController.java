package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.dao.NotificationDAO;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.helper.ValidationUtil;
import ir.ac.kntu.model.User;

public class NotificationController {

    public static void handleNotification(User user) {
        NotificationDAO.printNotifPreview(user);
        int notifId = getNotifId();
        if (notifId == 0) {
            return;
        }
        NotificationDAO.printNotifInfo(notifId);
    }

    public static int getNotifId() {
        String input = "";
        while (true) {
            System.err.println("Enter id of the notification to see more info (press 0 to return)");
            input = ScannerWrapper.getInstance().nextLine();
            if (ValidationUtil.isInteger(input))
                break;
        }
        int notifId = Integer.parseInt(input);

        if (notifId == 0) {
            return 0;
        }
        if (NotificationDAO.notifExist(notifId)) {
            return notifId;
        }
        return 0;
    }

    public static void handleNotifTypes(String type, String message) {
        switch (type) {
            case -> {
            }
            case -> {
            }
            case -> {
            }
            case -> {
            }
            default -> {
                System.out.println("this type of notification dose not exist");
            };
        }
    }
}
