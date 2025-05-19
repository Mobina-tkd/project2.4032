package ir.ac.kntu;
import ir.ac.kntu.helper.ScannerWrapper;


public class Menu {

    public static void choosingRoleMenu() {
        System.out.println("1) User");
        System.out.println("2) Seller");
        System.out.println("3) Supporter");
        System.out.print("Please choose Your Role: ");
    }

    public static void addressMenu() {
        System.out.println("1) Insert new address");
        System.out.println("2) View my addresses");
        System.out.println("3) Edit address");
        System.out.println("4) Delete address");
        System.out.print("Please Enter Your Choice:  ");
    }

    public static void userMenu() {
        System.out.println("1) Search For Products");
        System.out.println("2) Shopping Cart");
        System.out.println("3) Setting");
        System.out.println("4) Recent Purchases");
        System.out.println("5) Addresses");
        System.out.println("6) Wallet");
        System.out.println("7) Customer Support");
        System.out.println("8) Back");
        System.out.print("Please Enter Your Choice:  ");
    }

    public static void sellerMenu() {
        System.out.println("1) Products");
        System.out.println("2) Wallet");
        System.out.println("3) Recent Purchases");
        System.out.println("4) Back");
        System.out.print("Please Enter Your Choice:  ");
    }

    public static void supporterMenu() {
        System.out.println("1) Follow-Up Request");
        System.out.println("2) Identity Verification");
        System.out.println("3) Recent Purchases");
        System.out.println("4) Back");
        System.out.print("Please Enter Your Choice:  ");
    }

    public static void chooseStatementMenu() {
        System.out.println("1) New User");
        System.out.println("2) I Already Have An Account");
        System.out.println("3) Back");
        System.out.print("Please Enter Your Choice:  ");
    }

    public static void productMenu() {
        System.out.println("1) Insert product");
        System.out.println("2) Set inventory");
        System.out.println("3) Back");
        System.out.print("Please Enter Your Choice:  ");
    }

    public static void productCategoryMenu() {
        System.out.println("1) Mobile");
        System.out.println("2) Laptop");
        System.out.println("3) Book");
        System.out.println("4) Back");
        System.out.print("Please Enter Your Choice:  ");
    }

    public static void searchMenu() {
        System.out.println("1) Show all");
        System.out.println("2) Search by price");
        System.out.println("3) Back");
        System.out.print("Please Enter Your Choice:  ");
    }

    public static void searchBookMenu() {
        System.out.println("1) Show all");
        System.out.println("2) Search by price");
        System.out.println("3) Search by title");
        System.out.println("4) Back");
        System.out.print("Please Enter Your Choice:  ");
    }

    public static void settingMenu() {
        System.out.println("1) Show all");
        System.out.println("2) Search by price");
        System.out.println("3) Search by title");
        System.out.println("4) Back");
        System.out.print("Please Enter Your Choice:  ");
    }

    public static void verificationMenu() {
        System.out.println("1) Confirm");
        System.out.println("2) Deny");
        System.out.println("3) Back");
        System.out.print("Please Enter Your Choice:  ");
    }

    public static void addToListMenu() {
        System.out.println("1) Add product to shopping cart");
        System.out.println("2) Back");
        System.out.print("Please Enter Your Choice:  ");
    }

    public static void buyOrdeleteFromCartMenu() {
        System.out.println("1) Delete product from shopping cart");
        System.out.println("2) Buy all");
        System.out.println("3) Back");
        System.out.print("Please Enter Your Choice:  ");
    }

    public static void chooseAddressMenu() {
        System.out.println("1) Choose one address");
        System.out.println("2) Add new address");
        System.out.println("3) Back");
        System.out.print("Please Enter Your Choice:  ");
    }

    public static void payMenu() {
        System.out.println("1) Pay");
        System.out.println("2) Back");
        System.out.print("Please Enter Your Choice:  ");
    }

    public static void chargeWallet() {
        System.out.println("1) Charge");
        System.out.println("2) Back");
        System.out.print("Please Enter Your Choice:  ");
    }

    public static <T extends Enum<T>> T getEnumOption(Class<T> enumType, T undefinedOption) {
        T[] options = enumType.getEnumConstants();
        int userInput = ScannerWrapper.getInstance().nextInt();
        ScannerWrapper.getInstance().nextLine();
        userInput--;
        if (userInput >= 0 && userInput < options.length) {
            return options[userInput];
        }
        return undefinedOption;
    }

    public static Vendilo.MenuOption getMenuOption() {
        return getEnumOption(Vendilo.MenuOption.class, Vendilo.MenuOption.UNDEFINED);
    }

    public static Vendilo.AddressOption getAddressOption() {
        return getEnumOption(Vendilo.AddressOption.class, Vendilo.AddressOption.UNDEFINED);
    }

    public static Vendilo.UserOption getUserOption() {
        return getEnumOption(Vendilo.UserOption.class, Vendilo.UserOption.UNDEFINED);
    }

    public static Vendilo.SellerOption getSellerOption() {
        return getEnumOption(Vendilo.SellerOption.class, Vendilo.SellerOption.UNDEFINED);
    }

    public static Vendilo.SupporterOption getSupporterOption() {
        return getEnumOption(Vendilo.SupporterOption.class, Vendilo.SupporterOption.UNDEFINED);
    }

    public static Vendilo.Statement getStatementOption() {
        return getEnumOption(Vendilo.Statement.class, Vendilo.Statement.UNDEFINED);
    }

    public static Vendilo.ProductOption getProductOption() {
        return getEnumOption(Vendilo.ProductOption.class, Vendilo.ProductOption.UNDEFINED);
    }

    public static Vendilo.Product getProductCategory() {
        return getEnumOption(Vendilo.Product.class, Vendilo.Product.UNDEFINED);
    }

    public static Vendilo.SearchMenu getSearchMenuOption() {
        return getEnumOption(Vendilo.SearchMenu.class, Vendilo.SearchMenu.UNDEFINED);
    }

    public static Vendilo.SearchBookOption getSearchBookOption() {
        return getEnumOption(Vendilo.SearchBookOption.class, Vendilo.SearchBookOption.UNDEFINED);
    }

    public static Vendilo.SettingMenu getSettingMenu() {
        return getEnumOption(Vendilo.SettingMenu.class, Vendilo.SettingMenu.UNDEFINED);
    }

    public static Vendilo.VarificationMenu getVarificationOption() {
        return getEnumOption(Vendilo.VarificationMenu.class, Vendilo.VarificationMenu.UNDEFINED);
    }

    public static Vendilo.AddToList getAddToListOption() {
        return getEnumOption(Vendilo.AddToList.class, Vendilo.AddToList.UNDEFINED);
    }

    public static Vendilo.DeleteFromCart getBuyOrDeleteFromListOption() {
        return getEnumOption(Vendilo.DeleteFromCart.class, Vendilo.DeleteFromCart.UNDEFINED);
    }

    public static Vendilo.ChooseAddress getChooseOrAddAddress() {
        return getEnumOption(Vendilo.ChooseAddress.class, Vendilo.ChooseAddress.UNDEFINED);
    }

    public static Vendilo.PayMenu getPayOption() {
        return getEnumOption(Vendilo.PayMenu.class, Vendilo.PayMenu.UNDEFINED);
    }

    public static Vendilo.chargeWalletOption getChargeWalletOPtion() {
        return getEnumOption(Vendilo.chargeWalletOption.class, Vendilo.chargeWalletOption.UNDEFINED);
    }
} 
