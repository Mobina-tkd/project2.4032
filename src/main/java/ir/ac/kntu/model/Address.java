package ir.ac.kntu.model;

import ir.ac.kntu.helper.ScannerWrapper;

public class Address {

    private String location;
    private String state;
    private String street;
    private String houseNumber;

    
    public Address(String location, String state, String street, String houseNumber) {
        this.location = location;
        this.state = state;
        this.street = street;
        this.houseNumber = houseNumber;
    }

    public String getLocation() {
        return location;
    }
    public String getState() {
        return state;
    }
    public String getStreet() {
        return street;
    }
    public String getHouseNumber() {
        return houseNumber;
    }

    public static Address readAddress() {
        System.out.print("Enter the location: ");
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
