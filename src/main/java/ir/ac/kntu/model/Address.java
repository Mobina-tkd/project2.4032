package ir.ac.kntu.model;

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

    @Override

    public String toString() {
        return "Address(locaion : " + location + " state: " + state +
         " street: " + street + " houseNumber: " + houseNumber;
    }

    
}
