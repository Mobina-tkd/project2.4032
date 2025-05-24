package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.AddressDAO;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.helper.readData.ReadAddress;
import ir.ac.kntu.model.Address;
import ir.ac.kntu.model.User;

public class AddressController {

    public static void handleAddress(User user) {
        while (true) {
            Menu.addressMenu();
            Vendilo.AddressOption addressOption = Menu.getAddressOption();
            switch (addressOption) {
                case INSERT_NEW_ADDRESS -> {
                    while (true) {
                        Address address = ReadAddress.readAddress();
                        boolean inserted = AddressDAO.insertAddress(address, user);
                        if (!inserted) {
                            continue;
                        }
                        break;
                    }
                }
                case VIEW_ADDRESSES -> {
                    AddressDAO.printAllAddresses(user);
                }
                case EDIT_ADDRESSES -> {
                    System.out.print("Enter the location you want to edit : ");
                    String location = ScannerWrapper.getInstance().nextLine();
                    AddressDAO.editAddress(user, location);

                }
                case DELETE_ADDRESSES -> {
                    System.out.print("Enter the location you want to delete : ");
                    String location = ScannerWrapper.getInstance().nextLine();
                    AddressDAO.deleteAddress(user, location);
                }
                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\\n");

                }
                default -> throw new AssertionError();
            }
        }
    }
}
