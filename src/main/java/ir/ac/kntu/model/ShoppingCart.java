package ir.ac.kntu.model;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.AddressDAO;
import ir.ac.kntu.helper.HandleModels.HandleAddress;
import ir.ac.kntu.helper.ScannerWrapper;

public class ShoppingCart {
    private double  price;
    private String  information;

    public ShoppingCart(double price, String informaton) {
        this.price = price;
        this.information = informaton;
    }

    public double  getPrice() {
        return price;
    }

    public String  getInformation() {
        return information;
    }

    public static void handleBuying(User user) {
        HandleAddress.printAllAddresses(user);
        Menu.chooseAddressMenu();
        Vendilo.ChooseAddress option = Menu.getChooseOrAddAddress();
        switch (option) {
            case CHOOSE -> {
                System.out.print("Enter the number of the address: ");
                int id = ScannerWrapper.getInstance().nextInt();
                String state = AddressDAO.findState(id);
            }
            case NEW -> {
                Address address = Address.readAddress();
                AddressDAO.insertAddress(address, user);
                String state = address.getState();
            }
            case BACK -> {
            }
            case UNDEFINED -> {
                System.out.println("Undefined Choice; Try again...\n");
                break;
            }      
        }

    }
    
}
