package ir.ac.kntu;

public class Menu{

    public static void choosingRoleMenu(){
        System.out.println("1) User");
        System.out.println("2) Seller");
        System.out.println("3) Supporter");
        System.out.print("Please choose Your Role: ");
    }

    public static Vendilo.MenuOption getMenuOption() {
		Vendilo.MenuOption[] options = Vendilo.MenuOption.values();
		int userInput = ScannerWrapper.getInstance().nextInt();
		ScannerWrapper.getInstance().nextLine();
		userInput--;
		if (userInput >= 0 && userInput < options.length) {
			return options[userInput];
		}
		return Vendilo.MenuOption.UNDEFINED;
	}

	public static void AddressMenue() {
		System.out.println("1) Insert new address");
		System.out.println("2) View my addresses");
		System.out.println("3) Edite address");
		System.out.println("4) Delete address");
        System.out.print("Please Enter Your Choise:  ");
	}

	public static Vendilo.AddressOption getAddressOption() {
		Vendilo.AddressOption[] options = Vendilo.AddressOption.values();
		int userInput = ScannerWrapper.getInstance().nextInt();
		ScannerWrapper.getInstance().nextLine();
		userInput--;
		if (userInput >= 0 && userInput < options.length) {
			return options[userInput];
		}
		return Vendilo.AddressOption.UNDEFINED;
	}

    public static void userMenu(){
        System.out.println("1) Search For Products ");
        System.out.println("2) Shopping Cart ");
        System.out.println("3) Setting ");
        System.out.println("4) Recent Puchases ");
        System.out.println("5) Adresses ");
        System.out.println("6) Wallet ");   
        System.out.println("7) Customer Support ");
		System.out.println("8) Back");
        System.out.print("Please Enter Your Choise:  ");
    }

    public static Vendilo.UserOption getUserOption() {
		Vendilo.UserOption[] options = Vendilo.UserOption.values();
		int userInput = ScannerWrapper.getInstance().nextInt();
		ScannerWrapper.getInstance().nextLine();
		userInput--;
		if (userInput >= 0 && userInput < options.length) {
			return options[userInput];
		}
		return Vendilo.UserOption.UNDEFINED;
	}

    public static void sellerMenu(){
        System.out.println("1) Products ");
        System.out.println("2) Wallet ");
        System.out.println("3) Recent Purchases ");
		System.out.println("4) Back");
        System.out.print("Please Enter Your Choise:  ");
    }

    public static Vendilo.SellerOption getSellerOption() {
		Vendilo.SellerOption[] options = Vendilo.SellerOption.values();
		int userInput = ScannerWrapper.getInstance().nextInt();
		ScannerWrapper.getInstance().nextLine();
		userInput--;
		if (userInput >= 0 && userInput < options.length) {
			return options[userInput];
		}
		return Vendilo.SellerOption.UNDEFINED;
	}

    public static void supporterMenu(){
        System.out.println("1) Follow-Up Request ");
        System.out.println("2) Identity Verification ");
        System.out.println("3) Recent Purchases ");
		System.out.println("4) Back");
        System.out.print("Please Enter Your Choise:  ");

    }

    public static Vendilo.SupporterOption getSupporterOption() {
		Vendilo.SupporterOption[] options = Vendilo.SupporterOption.values();
		int userInput = ScannerWrapper.getInstance().nextInt();
		ScannerWrapper.getInstance().nextLine();
		userInput--;
		if (userInput >= 0 && userInput < options.length) {
			return options[userInput];
		}
		return Vendilo.SupporterOption.UNDEFINED;
	}

    public static void chooseStatementMenu(){
        System.out.println("1) New User");
        System.out.println("2) I Already Have An Account");
		System.out.println("3) Back");
        System.out.print("Please Enter Your Choise:  ");        
    }

    public static Vendilo.Statement getStatementOption() {
		Vendilo.Statement[] options = Vendilo.Statement.values();
		int userInput = ScannerWrapper.getInstance().nextInt();
		ScannerWrapper.getInstance().nextLine();
		userInput--;
		if (userInput >= 0 && userInput < options.length) {
			return options[userInput];
		}
		return Vendilo.Statement.UNDEFINED;
	}

    
    
}