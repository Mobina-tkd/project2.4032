package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.DiscountDAO;
import ir.ac.kntu.dao.NotificationDAO;
import ir.ac.kntu.dao.ProductDAO;
import ir.ac.kntu.dao.RequestDAO;
import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.helper.ValidationUtil;
import ir.ac.kntu.model.ShoppingCart;
import ir.ac.kntu.model.User;

public class NotificationController {

    public static void handleNotification(User user) {
        while (true) {
            NotificationDAO.printNotifPreview(user);
            int notifId = getNotifId();
            if (notifId == 0) {
                return;
            }
            NotificationDAO.printNotifInfo(notifId, user);
        }
    }

    public static int getNotifId() {
        String input = "";
        while (true) {
            System.out.println("Enter id of the notification to see more info (press 0 to return)");
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

    public static void handleNotifTypes(String type, String message, User user) {
        switch (type) {
            case "Request checked" -> {
                handleCheckedRequest(Integer.parseInt(message));
            }
            case "Broadcast message" -> {
                handleBroadcastMessage(message);
            }
            case "Discount code" -> {
                handleDiscountCode(message);
            }
            case "Product is available" -> {
                handleNewProduct(message, user);
            }
            default -> {
                System.out.println("this type of notification dose not exist");
            }
        }
    }

    private static void handleBroadcastMessage(String message) {
        System.out.println(ConsoleColors.BLUE + "-----Manager message for you dear user-----" + ConsoleColors.RESET);
        System.out.println(message);
        System.out.println(ConsoleColors.BLUE + "-------------------------------------------\n" + ConsoleColors.RESET);
    }

    private static void handleDiscountCode(String code) {
        System.out.println(ConsoleColors.BLUE + "-----You have received a discount code-----" + ConsoleColors.RESET);
        int codeId = DiscountDAO.findIdByCode(code);
        DiscountDAO.printDiscountInformation(codeId);
        System.out.println(ConsoleColors.BLUE + "-------------------------------------------\n" + ConsoleColors.RESET);

    }

    private static void handleCheckedRequest(int requestId) {
        System.out.println(
                ConsoleColors.BLUE + "-----Your request has been checked by supporter-----" + ConsoleColors.RESET);
        RequestDAO.printRespondOfRequest(requestId);
        System.out.println(ConsoleColors.BLUE + "----------------------------------------------------\n" + ConsoleColors.RESET);
    }

    private static void handleNewProduct(String productInfo, User user) {
        System.out.println(ConsoleColors.BLUE + "-----We have got more of your favorite product in stock!-----\n" + ConsoleColors.RESET);

        String[] parts = productInfo.split(" ");
        String productType = parts[0];
        int productId = Integer.parseInt(parts[1]);
        int sellerId = ProductDAO.findSellerId(productId, productType);
        ShoppingCart shoppingCart = ProductDAO.makeShoppingCartObject(productId, sellerId, productType);
        System.out.println(shoppingCart.getInformation());
        Menu.addToListMenu();
        Vendilo.AddToList option = Menu.getAddToListOption();
        switch (option) {
            case ADD -> {
                ProductController.addProductHandler(productType, productId, user);
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
