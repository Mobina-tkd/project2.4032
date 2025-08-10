package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.NotificationDAO;
import ir.ac.kntu.dao.RequestDAO;
import ir.ac.kntu.dao.SupporterDAO;
import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.ScannerWrapper;

public class RequestController {

    public static void handleRequest(String username) {
        if (!SupporterDAO.hasAccess("Follow_up_request", username)) {
            System.out.println(ConsoleColors.RED + "You dont hava access" + ConsoleColors.RESET);
            return;
        }
        while (true) {
            Menu.printSupportOptionsS();
            Vendilo.SupporterSupporterOptions option = Menu.getSupporterSupportOption();
            switch (option) {
                case REPORT -> {
                    disPlayRequest("Report");
                    break;
                }
                case WRONG_PRODUCT_SENT -> {
                    disPlayRequest("Wrong product sent");
                    break;
                }
                case SETTING -> {
                    disPlayRequest("Setting");
                    break;
                }
                case ORDER_NOT_RECEIVED -> {
                    disPlayRequest("Order not received");
                    break;
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

    public static void sendRequest(String email, String title) {
        System.out.print("Your request: ");
        String message = ScannerWrapper.getInstance().nextLine();
        RequestDAO.insertRequest(email, title, message);
    }

    private static void disPlayRequest(String field) {
        RequestDAO.printRequestsByFieldName(field);
        System.out.print("Enter the id of request(press 0 to back): ");
        int requestId = ScannerWrapper.getInstance().nextInt();
        if (requestId == 0) {
            return;
        }
        RequestDAO.printAllRequestInfoById(requestId);
        handleSettingMessage(requestId);
    }

    private static void handleSettingMessage(int requestId) {
        while (true) {
            Menu.setMessageMenu();
            Vendilo.SetMessage option = Menu.getSetMessageOption();

            switch (option) {
                case SET_MESSAGE -> {
                    setMessage(requestId);
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

    private static void setMessage(int requestId) {
        System.out.print("Your message for user: ");
        String message = ScannerWrapper.getInstance().nextLine();
        RequestDAO.setMessageAndUpdateStatus(message, requestId);
        String email = RequestDAO.findUserEmailByRequestId(requestId);
        NotificationDAO.insertNotification(email, "Request checked", String.valueOf(requestId));
    }

    public static void handlePreviousRequests(String email) {
        while (true) {
            RequestDAO.printRequestsByEmail(email);
            Menu.watchRespondMenu();
            Vendilo.WatchRespond option = Menu.getWatchRespondOPtion();
            switch (option) {
                case SHOW_RESOPND -> {
                    System.out.print("Enter the id of you request: ");
                    int requestId = ScannerWrapper.getInstance().nextInt();
                    System.out.println("");
                    RequestDAO.printRespondOfRequest(requestId);
                    System.out.println("");
                    break;
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
