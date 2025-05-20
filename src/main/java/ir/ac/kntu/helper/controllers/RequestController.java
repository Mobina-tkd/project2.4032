package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.RequestDAO;
import ir.ac.kntu.helper.ScannerWrapper;

public class RequestController {

    private static void handleRequest() {
        while(true) {
            Menu.printSupportOptions();
            Vendilo.UserSupporterOptions option = Menu.getUserSupportOption();
            switch (option) {
                case REPORT -> {
                    disPlayRequest("Report");
                    break;
                }
                case WRONG_PRODUCT_SENT -> {
                    disPlayRequest("Wrong product sent");
                    break;
                }
                case SETTING-> {
                    disPlayRequest("Setting");
                    break;
                } 
                case ORDER_NOT_RECEIVED-> {
                    disPlayRequest("Order not received");
                    break;
                } 
                case BACK-> {
                    return;
                } 
                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\\n");
                }
                  
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
        System.out.print("Enter the id of request: ");
        int id = ScannerWrapper.getInstance().nextInt();
        RequestDAO.printAllRequestInfoById(id);
        handleSettingMessage(id);
        
    }

    private static void handleSettingMessage(int id) {
        while(true) {
            Menu.setMessageMenu();
            Vendilo.SetMessage option = Menu.getSetMessageOption();

            switch (option) {
                case SET_MESSAGE -> {
                    setMessage(id);
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\\n");
                }
            }
        }
    }

    private static void setMessage(int id) {
        System.out.println("Your message for user: ");
        String message = ScannerWrapper.getInstance().nextLine();
        RequestDAO.setMessageAndUpdateStatus(message, id);
    }


    public static void handlePreviousRequests(String email) {
        while (true) { 
            RequestDAO.printRequestsByEmail(email);
            Menu.watchRespondMenu();
            Vendilo.watchRespond option = Menu.getWatchRespondOPtion();
            switch (option) {
                case SHOW_RESOPND -> {
                    System.out.println("Enter the id of you request: ");
                    int id = ScannerWrapper.getInstance().nextInt();
                    RequestDAO.printRespondOfRequest(id);
                    break;
                }
                case BACK -> {
                    return;
                } 
                case UNDEFINED-> {
                    System.out.println("Undefined Choice; Try again...\\n");
                }     
            }
            
        }

    }
    
}
