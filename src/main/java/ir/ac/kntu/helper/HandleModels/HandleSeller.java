package ir.ac.kntu.helper.HandleModels;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.BookDAO;
import ir.ac.kntu.dao.LaptopDAO;
import ir.ac.kntu.dao.MobileDAO;
import ir.ac.kntu.dao.SellerDAO;
import ir.ac.kntu.helper.DealerCodeGenerator;
import ir.ac.kntu.helper.loginPage;
import ir.ac.kntu.helper.readData.PersonFactory;
import ir.ac.kntu.helper.readData.ProductFactory;
import ir.ac.kntu.model.Book;
import ir.ac.kntu.model.Laptop;
import ir.ac.kntu.model.Mobile;
import ir.ac.kntu.model.Seller;

public class HandleSeller {

    
    public static void handleNewSeller() {
        while(true){ 
            Seller seller = PersonFactory.readSellerData();
            String agencyCode = DealerCodeGenerator.generateUniqueCode();
            seller.setAgencyCode(agencyCode);
            Boolean inserted = SellerDAO.insertSeller(seller);
            if(inserted){
                System.out.println("Here is your agency code : " + agencyCode);
                break;
            }
        }
        while(true){
            boolean canEnter = loginPage.sellerLoginPage();
            if(!canEnter){
                continue;
            }
            break;
        }
    }



    public static void handleSeller(){
        while(true){
            Menu.chooseStatementMenu();
            Vendilo.Statement statement = Menu.getStatementOption(); 
            switch (statement) {
                case NEW_USER -> {
                    handleNewSeller();
                    chooseOption();
                }
                case ALREADY_HAS_ACCOUNT -> {
                    while(true){
                        boolean canEnter = loginPage.sellerLoginPage();
                        if(!canEnter){
                            return;
                        }
                        break;
                    }
                    chooseOption();
                } 
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println("Undefined Choice");
                    break;
                }        
            }
        }
    }

 

    public static void chooseOption(){
        while(true){
            Menu.sellerMenu();
            Vendilo.SellerOption option = Menu.getSellerOption();
            switch (option) {
                case PRODUCTS -> {
                    handleProduct();
                }
                case WALLET-> {
                    break;
                }
                case RECENT_PURCHASES-> {
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


    private static void handleProduct() {
        while(true) {
            Menu.productMenu();
            Vendilo.ProductOption productOption = Menu.getProductOption();
            switch (productOption) {
                case INSERT_PRODUCT-> {
                    handleInsertProduct();
                }
                case SET_INVENTORY-> {
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED-> {
                    System.out.println("Undefined Choice; Try again...\n");

                }
            }
        }
    }

    public static void handleInsertProduct() {
        while(true) {
            Menu.productCategoryMenu();
            Vendilo.Product insertProduct = Menu.getProductCategory();
            switch (insertProduct) {
                case MOBILE -> {
                    Mobile mobile = ProductFactory.readMobileData();
                    MobileDAO.insertMobile(mobile);
                }
                case LAPTOP -> {
                    Laptop laptop = ProductFactory.readLaptopData();
                    LaptopDAO.insertLaptop(laptop);
                }
                case BOOK -> {
                    Book book = ProductFactory.readBookData();
                    BookDAO.insertBook(book);
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
