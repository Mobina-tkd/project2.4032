package ir.ac.kntu.helper.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.helper.ScannerWrapper;

public class loginPageController {
    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static String sellerLoginPage() {
        while (true) {
            System.out.println("--------Login Page-------");
            System.out.print("Do you want to login? Y/N: ");
            String login = ScannerWrapper.getInstance().nextLine();
            if ("n".equalsIgnoreCase(login)) {
                return "Back";
            }
            System.out.print("Enter your agency code : ");
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
                        System.out.println("Welcome dear seller");
                        return username;
                    } else if (identityVarified == 2) {
                        System.out.println(message);
                        return username;

                    } else if (identityVarified == 0) {
                        System.out.println("your identity is not varified yet");
                        return null;

                    } else {
                        System.out.println("Incorrect password. Try again.");
                    }
                } else {
                    System.out.println("We could not find this agency code.");
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
            System.out.print("Enter your username(Phone Number or Email Address) :  ");
            String username = ScannerWrapper.getInstance().nextLine();
            System.out.print("Enter you password : ");
            String password = ScannerWrapper.getInstance().nextLine();

            String sql = "SELECT password FROM users WHERE email = ? OR phone_number = ?";

            try (Connection conn = DriverManager.getConnection(url);
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, username);
                pstmt.setString(2, username);

                ResultSet resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    if (storedPassword.equals(password)) {
                        System.out.println(ConsoleColors.GREEN +"Login successful! Welcome."+ ConsoleColors.RESET);
                        return username;
                    } else {
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

    public static boolean supporterLoginPage() {
        System.out.print("Enter your username: ");
        String username = ScannerWrapper.getInstance().nextLine();
        System.out.print("Enter your password: ");
        String password = ScannerWrapper.getInstance().nextLine();

        if ("Sara_H82".equals(username) && "S1382ara_".equals(password)) {
            System.out.println("Welcome dear sara");
            return true;
        } else if ("mmd_L80".equals(username) && "M1380md_".equals(password)) {
            System.out.println("Welcome dear mohammad");
            return true;
        } else {
            System.out.println("Wrong username or password :( please try again...");
            return false;
        }

    }

}
