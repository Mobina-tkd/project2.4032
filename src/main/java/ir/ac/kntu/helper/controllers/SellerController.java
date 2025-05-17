package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.SellerDAO;
import ir.ac.kntu.helper.DealerCodeGenerator;
import ir.ac.kntu.helper.readData.PersonFactory;
import ir.ac.kntu.model.Seller;

public class SellerController {

    public static void chooseSellerOption(String agencyCode){
        while(true){
            Menu.sellerMenu();
            Vendilo.SellerOption option = Menu.getSellerOption();
            switch (option) {
                case PRODUCTS -> {
                    ProductController.handleProduct(agencyCode);
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

    public static String handleNewSeller() {
        String agencyCode;
        while(true){ 
            Seller seller = PersonFactory.readSellerData();
            agencyCode = DealerCodeGenerator.generateUniqueCode();
            seller.setAgencyCode(agencyCode);
            Boolean inserted = SellerDAO.insertSeller(seller);
            if(inserted){
                System.out.println("Here is your agency code : " + agencyCode);
                break;
            }
        }
        while(true){
            agencyCode = loginPageController.sellerLoginPage();
            if(agencyCode == null){
                continue;
            }
            break;
        }
        return agencyCode;
    }



    public static void handleSellerChoise(){
        while(true){
            Menu.chooseStatementMenu();
            Vendilo.Statement statement = Menu.getStatementOption(); 
            switch (statement) {
                case NEW_USER -> {
                    String agencyCode = handleNewSeller();
                    chooseSellerOption(agencyCode);
                }
                case ALREADY_HAS_ACCOUNT -> {
                    String agencyCode;
                    while(true){
                        agencyCode = loginPageController.sellerLoginPage();
                        if(agencyCode == null){
                            continue;
                        }
                        break;
                    }
                    chooseSellerOption(agencyCode);
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
