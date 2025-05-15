package ir.ac.kntu;


public class HandleUserOptions {

    public static void handleSetting(User user) {
        while(true) {
            String newValue;
            Menu.SettingMenu();
            Vendilo.SettingMenu settingMenue = Menu.getSettingMenu();
            switch (settingMenue) {
                case EMAIL-> {
                    newValue = Utils.readEmail();
                    Utils.setAndUpdate(user, "email", newValue);
                }
                case PHONE_NUMBER-> {
                    newValue = Utils.readPhoneNUmber();
                    Utils.setAndUpdate(user, "phone_number", newValue);

                }
                case FIRST_NAME-> {
                    System.out.print("Enter first name: ");
                    newValue = ScannerWrapper.getInstance().nextLine();
                    Utils.setAndUpdate(user, "first_name", newValue);

                }
                case LAST_NAME-> {
                    System.out.print("Enter last name: ");
                    newValue = ScannerWrapper.getInstance().nextLine();
                    Utils.setAndUpdate(user, "last_name", newValue);

                }
                case PASSWORD-> {
                    newValue = Utils.readPassword();
                    Utils.setAndUpdate(user, "password", newValue);

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



    public static void handleAddress(User user) {

        while(true) {
            Menu.AddressMenue();
            Vendilo.AddressOption addressOption = Menu.getAddressOption();
            switch (addressOption) {
                case INSERT_NEW_ADDRESS -> {
                    while(true) {
                        Address address = Utils.readAddressFromUser();
                        boolean inserted = AddressDAO.insertAddress(address, user);
                        if(!inserted) {
                            continue;
                        }
                        break;
                    }
                }
                case VIEW_ADDRESSES -> {
                    Address.printAllAddresses(user);
                }
                case EDIT_ADDRESSES -> {
                    System.out.print("Enter the location you want to edit : ");
                    String location = ScannerWrapper.getInstance().nextLine();
                    Address.editAddress(user, location);
                       
                }
                case DELETE_ADDRESSES -> {
                    System.out.print("Enter the location you want to delete : ");
                    String location = ScannerWrapper.getInstance().nextLine();
                    Address.deleteAddress(user, location);
                }
                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\\n");
                    
                }
            }
        }
    
        }


        public static void searchProduct(User user) {
            while(true) {
                Menu.productCategoryMenu();
                Vendilo.Product insertProduct = Menu.getProductCategory();
                switch (insertProduct) {
                    case MOBILE -> {
                        Mobile.search(user);
                        
                    }
                    case LAPTOP -> {
                        Laptop.search(user);
                       
                    }
                    case BOOK -> {
                        Book.search(user);
                        
                    }
                    case BACK -> {
                        return;
                    }
                    case UNDEFINED -> {
                        System.out.println("Undefined Choice; Try again...\n");
    
                    }
                }
            }
        }
    
}
