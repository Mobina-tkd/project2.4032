package ir.ac.kntu.helper.readData;

import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.model.Address;

public class ReadAddress {

    public static Address readAddress() {
        System.out.print("\nEnter the location: ");
        String location = ScannerWrapper.getInstance().nextLine();
        System.out.print("Enter the state: ");
        String state = ScannerWrapper.getInstance().nextLine();
        System.out.print("Enter the street: ");
        String street = ScannerWrapper.getInstance().nextLine();
        System.out.print("Enter the houseNumber: ");
        String houseNumber = ScannerWrapper.getInstance().nextLine();

        Address address = new Address(location, state, street, houseNumber);
        return address;
    }

}
