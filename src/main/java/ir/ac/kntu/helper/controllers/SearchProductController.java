package ir.ac.kntu.helper.controllers;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.BookDAO;
import ir.ac.kntu.dao.ProductDAO;
import ir.ac.kntu.helper.ScannerWrapper;
import ir.ac.kntu.model.User;

public class SearchProductController {

    public static void HandleSearchProduct(User user) {
        while(true) {
            Menu.productCategoryMenu();
            Vendilo.Product insertProduct = Menu.getProductCategory();
            switch (insertProduct) {
                case MOBILE -> {
                    searchMobile(user);
                    
                }
                case LAPTOP -> {
                    searchLaptop(user);
                   
                }
                case BOOK -> {
                    searchBook(user);
                    
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
    
    public static void searchBook(User user) {
        while (true) { 
            Menu.searchBookMenu();
            Vendilo.SearchBookOption option = Menu.getSearchBookOption();
            switch (option) {
                case SHOW_ALL-> {
                    ProductDAO.showAllProducts("Books");
                    ProductController.handleAddProductToList("Books", user);
                }


                case SEARCH_BY_PRICE-> {
                    System.out.println("Enter the min price: ");
                    double min = ScannerWrapper.getInstance().nextDouble();
                    System.out.println("Enter the max price: ");
                    double max = ScannerWrapper.getInstance().nextDouble();
                    ProductDAO.searchByPrice("Books", min, max);
                    ProductController.handleAddProductToList("Books", user);                
                }

                case SEARCH_BY_TITLE -> {
                    System.out.println("Enter book title: ");
                    String title = ScannerWrapper.getInstance().nextLine();
                    BookDAO.searchBookByTitle("Books", title);
                    ProductController.handleAddProductToList("Books", user);              
                }

                case BACK-> {
                    return;
                }

                case UNDEFINED-> {
                    System.out.println("Undefined Choice; Try again...\n");
                    break;
                }
            }
        }
    }

    
    public static void searchLaptop(User user) {
        Menu.searchMenu();
        Vendilo.SearchMenu option = Menu.getSearchMenuOption();

        switch (option) {
            case SHOW_ALL-> {
                ProductDAO.showAllProducts("Laptop");
                ProductController.handleAddProductToList("Laptop", user);              
            }
            case SEARCH_BY_PRICE-> {
                System.out.println("Enter the min price: ");
                double min = ScannerWrapper.getInstance().nextDouble();
                System.out.println("Enter the max price: ");
                double max = ScannerWrapper.getInstance().nextDouble();
                ProductDAO.searchByPrice("Laptop", min, max);
                ProductController.handleAddProductToList("Laptop", user);              
            }
            case BACK-> {
                return;
            }
            case UNDEFINED-> {
                System.out.println("Undefined Choice; Try again...\n");
                break;
            }
        }
        
    }


    public static void searchMobile(User user) {
        Menu.searchMenu();
        Vendilo.SearchMenu option = Menu.getSearchMenuOption();

        switch (option) {
            case SHOW_ALL-> {
                ProductDAO.showAllProducts("Mobile");
                ProductController.handleAddProductToList("Mobile",user);              
            }
            case SEARCH_BY_PRICE-> {
                System.out.println("Enter the min price: ");
                double min = ScannerWrapper.getInstance().nextDouble();
                System.out.println("Enter the max price: ");
                double max = ScannerWrapper.getInstance().nextDouble();
                ProductDAO.searchByPrice( "Mobile", min, max);
                ProductController.handleAddProductToList("Mobile", user);              
            }
            case BACK -> {
                return ;
            }
            case UNDEFINED-> {
                System.out.println("Undefined Choice; Try again...\n");
                break;
            }
        }
    }


}
