package ir.ac.kntu.helper.HandleModels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ir.ac.kntu.Menu;
import ir.ac.kntu.Vendilo;
import ir.ac.kntu.dao.UserDAO;
import ir.ac.kntu.helper.loginPage;
import ir.ac.kntu.helper.readData.PersonFactory;
import ir.ac.kntu.model.User;

public class HandleUser {
    private static final String DB_URL = "jdbc:sqlite:data.db";


    public static User handleNewUser() {
        User user = new User();
        while(true){ 
            user = PersonFactory.readUserData();
            Boolean inserted = UserDAO.insertUser(user);
            if(!inserted){
            continue;
            }
            break;
        }
            while(true){
            String username = loginPage.userLoginPage();
            if(username == null){
                continue;
            }
            return user;
        }
        
    }


    public static void handleUser(){ 
        
        while(true){
            Menu.chooseStatementMenu();
            Vendilo.Statement statement = Menu.getStatementOption();
            User user = new User();
            switch (statement) {
                case NEW_USER -> {
                    user = handleNewUser(); 
                    chooseOption(user);
                }

                case ALREADY_HAS_ACCOUNT -> {
                    String  username ="";
                    while(true){
                        username = loginPage.userLoginPage();
                        if(username == null){
                            continue;
                        }
                        break;
                    }
                    user = findUser(username);
                    chooseOption(user);
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

   

    public static void chooseOption(User user){
        while(true){
            Menu.userMenu();
            Vendilo.UserOption option = Menu.getUserOption();
            switch (option) {
                case SEARCH_FOR_PRODUCTS -> {
                    HandleUserOptions.searchProduct(user);
                }
                case SHOPPING_CART-> {
                    HandleUserOptions.handleShoppingCart(user);
                }
                case SETTING-> {
                    HandleUserOptions.handleSetting(user);
                }
                case RECENT_PURCHASES-> {
                    break;
                }
                case ADRESSES -> {
                    HandleUserOptions.handleAddress(user);    
                }
                case WALLET -> {
                    break;
                }
                case CUSTOMER_SUPPORT -> {
                    break;
                }
                case BACK -> {
                    return;
                }
                case UNDEFINED -> {
                    System.out.println("Undefined Choice; Try again...\\n");
                }
                
            }
        }
    }  

    public static User findUser(String username) {
    String sql = "SELECT first_name, last_name, email, phone_number, password FROM users WHERE email = ? OR phone_number = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, username);
        stmt.setString(2, username);

        ResultSet resultSet = stmt.executeQuery();

        if (resultSet.next()) {
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String email = resultSet.getString("email");
            String phone = resultSet.getString("phone_number");
            String password = resultSet.getString("password");

            return new User(firstName, lastName, email, phone, password);
        } else {
            System.out.println("No user found with email or phone: " + username);
            return null;
        }

    } catch (SQLException e) {
        System.err.println("Error finding user: " + e.getMessage());
        return null;
    }
}

public static void setAndUpdateUserData(User user, String field, String newValue) {
        
    String sql = "UPDATE users SET " + field + " = ? WHERE email = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL);
     PreparedStatement stmt = conn.prepareStatement(sql)) {

    stmt.setString(1, newValue);
    stmt.setString(2, user.getEmail()); 

    int rowsUpdated = stmt.executeUpdate();

    if (rowsUpdated > 0) {
        System.out.println("User data updated successfully.");
    } else {
        System.out.println("No update was made.");
    }

} catch (SQLException e) {
    e.printStackTrace();
}

switch (field) {
    case "email" -> {
        user.setEmail(newValue);;  
    }
    case "phone_number" -> {
        user.setPhoneNumber(newValue);;
    }
    case "first_name"-> {
        user.setFirstName(newValue);;
    }
    case "last_name" -> {
        user.setLastName(newValue);;
    }
    case "password" -> {
        user.setPassword(newValue);
    }
    default -> throw new AssertionError();
}

}

    
}
