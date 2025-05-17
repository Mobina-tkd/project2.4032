package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.SellerDAO;
import ir.ac.kntu.helper.DealerCodeGenerator;
import ir.ac.kntu.helper.readData.PersonFactory;
import ir.ac.kntu.model.Seller;

public class SellerController {

    public static void chooseSellerOption(){
        while(true){
            Menu.sellerMenu();
            Vendilo.SellerOption option = Menu.getSellerOption();
            switch (option) {
                case PRODUCTS -> {
                    ProductController.handleProduct();
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
            boolean canEnter = loginPageController.sellerLoginPage();
            if(!canEnter){
                continue;
            }
            break;
        }
    }



    public static void handleSellerChoise(){
        while(true){
            Menu.chooseStatementMenu();
            Vendilo.Statement statement = Menu.getStatementOption(); 
            switch (statement) {
                case NEW_USER -> {
                    handleNewSeller();
                    chooseSellerOption();
                }
                case ALREADY_HAS_ACCOUNT -> {
                    while(true){
                        boolean canEnter = loginPageController.sellerLoginPage();
                        if(!canEnter){
                            return;
                        }
                        break;
                    }
                    chooseSellerOption();
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

    
}
