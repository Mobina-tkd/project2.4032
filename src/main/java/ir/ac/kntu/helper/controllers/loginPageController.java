package ir.ac.kntu.helper.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.ScannerWrapper;

public class LoginPageController {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static String sellerLoginPage() {
        while (true) {
            System.out.println(ConsoleColors.RED +"---------"+ ConsoleColors.RESET+"Login Page"+ConsoleColors.RED +"--------"+ ConsoleColors.RESET);
            System.out.print("Do you want to login? Y/N: ");
            String login = ScannerWrapper.getInstance().nextLine();
            if ("n".equalsIgnoreCase(login)) {
                return "Back";
            }
            System.out.print("\nEnter your agency code : ");
            String username = ScannerWrapper.getInstance().nextLine();
            System.out.print("Enter your password : ");
            String userPassword = ScannerWrapper.getInstance().nextLine();

            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "SELECT password, identity_verified, message FROM sellers WHERE agency_code = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);

                ResultSet resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    String password = resultSet.getString("password");
                    int identityVarified = resultSet.getInt("identity_verified");
                    String message = resultSet.getString("message");
                    if (userPassword.equals(password) && identityVarified == 1) {
                        System.out.println(ConsoleColors.BLUE + "Welcome dear seller" + ConsoleColors.RESET);
                        return username;
                    } else if (identityVarified == 2) {
                        System.out.println(message);
                        return null;

                    } else if (identityVarified == 0) {
                        System.out.println(ConsoleColors.RED +"your identity is not varified yet"+ ConsoleColors.RESET);
                        return null;

                    } else {
                        System.out.println(ConsoleColors.RED +"Incorrect password." + ConsoleColors.RESET);
                    }
                } else {
                    System.out.println(ConsoleColors.RED +"We could not find this agency code."+ ConsoleColors.RESET);
                    return null;
                }

            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
                return null;
            }
        }
    }

    public static String userLoginPage() {
        String url = "jdbc:sqlite:data.db";
        while (true) {
            System.out.println(ConsoleColors.RED +"---------"+ ConsoleColors.RESET+"Login Page"+ConsoleColors.RED +"--------"+ ConsoleColors.RESET);
            System.out.print("Do you want to login? Y/N: ");
            String login = ScannerWrapper.getInstance().nextLine();
            if ("n".equalsIgnoreCase(login)) {
                return "Back";
            }
            System.out.print("\nEnter your username(Phone Number or Email Address) :  ");
            String username = ScannerWrapper.getInstance().nextLine();
            System.out.print("Enter you password : ");
            String password = ScannerWrapper.getInstance().nextLine();

            String sql = "SELECT password, isBlock FROM users WHERE email = ? OR phone_number = ?";

            try (Connection conn = DriverManager.getConnection(url);
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, username);
                pstmt.setString(2, username);

                ResultSet resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    int isBlock = resultSet.getInt("isBlock");
                    if (isBlock == 1) {
                        System.out.println("You have been blocked by manager");
                        return null;
                    }
                    if (storedPassword.equals(password)) {
                        System.out.println(ConsoleColors.BLUE +"Login successful! Welcome."+ ConsoleColors.RESET);
                        return username;
                    } 
                    else {
                        System.out.println(ConsoleColors.RED +"Incorrect password." + ConsoleColors.RESET);
                        return null;
                    }
                } else {
                    System.out.println(ConsoleColors.RED +"No account found with that email or phone number." + ConsoleColors.RESET);
                    return null;
                }

            } catch (SQLException e) {
                System.out.println(ConsoleColors.RED + "Database error: " + e.getMessage() + ConsoleColors.RESET);
            }

        }
    }


    public static String supporterAndManagerLoginPage(String usertype) {
        while (true) {
            System.out.println(ConsoleColors.RED +"---------"+ ConsoleColors.RESET+"Login Page"+ConsoleColors.RED +"--------"+ ConsoleColors.RESET);
            System.out.print("Do you want to login? Y/N: ");
            String login = ScannerWrapper.getInstance().nextLine();
            if ("n".equalsIgnoreCase(login)) {
                return "Back";
            }
            System.out.print("\nEnter your username:  ");
            String username = ScannerWrapper.getInstance().nextLine();
            System.out.print("Enter you password: ");
            String password = ScannerWrapper.getInstance().nextLine();

            String sql = "SELECT password, isBlock FROM "+ usertype +" WHERE username = ?";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, username);

                ResultSet resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    int isBlock = resultSet.getInt("isBlock");
                    if (isBlock == 1) {
                        System.out.println("You have been blocked by manager");
                        return null;
                    }
                    if (storedPassword.equals(password)) {
                        System.out.println(ConsoleColors.BLUE +"Login successful! Welcome."+ ConsoleColors.RESET);
                        return username;
                    } else {
                        System.out.println(ConsoleColors.RED +"Incorrect password." + ConsoleColors.RESET);
                        return null;
                    }
                } else {
                    System.out.println(ConsoleColors.RED +"No account found." + ConsoleColors.RESET);
                    return null;
                }

            } catch (SQLException e) {
                System.out.println(ConsoleColors.RED + "Database error: " + e.getMessage() + ConsoleColors.RESET);
            }

        }
    }

    

}
