package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.AddressDAO;
import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.helper.readData.ReadDataUtil;
import ir.ac.kntu.model.Address;
import ir.ac.kntu.model.User;

public class AddressController {

    public static void handleAddress(User user) {
        while (true) {
            Menu.addressMenu();
            Vendilo.AddressOption addressOption = Menu.getAddressOption();
            switch (addressOption) {
                case INSERT_NEW_ADDRESS -> {
                    handleInsert(user);
                }
                case VIEW_ADDRESSES -> {
                    AddressDAO.printAllAddresses(user);
                }
                case EDIT_ADDRESSES -> {
                    handleEditting(user);

                }
                case DELETE_ADDRESSES -> {
                    handleDeleting(user);
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println(ConsoleColors.RED + "Undefined Choice; Try again...\\n" + ConsoleColors.RESET);

                }
                default -> throw new AssertionError();
            }
        }
    }

    private static void handleInsert(User user) {
        while (true) {
            Address address = ReadDataUtil.readAddress();
            boolean inserted = AddressDAO.insertAddress(address, user);
            if (!inserted) {
                continue;
            }
            break;
        }
    }

    private static void handleEditting(User user) {
        System.out.print("Enter the location you want to edit : ");
        String location = ScannerWrapper.getInstance().nextLine();
        AddressDAO.editAddress(user, location);
    }

    private static void handleDeleting(User user) {
        System.out.print("Enter the location you want to delete : ");
        String location = ScannerWrapper.getInstance().nextLine();
        AddressDAO.deleteAddress(user, location);
    }
}
