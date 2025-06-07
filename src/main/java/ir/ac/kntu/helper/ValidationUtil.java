package ir.ac.kntu.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {

    public static boolean matchEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.find();

    }

    public static boolean matchPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d])[A-Za-z\\d\\S]{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();

    }

    public static boolean matchNumber(String number) {
        Pattern pattern = Pattern.compile("^(09\\d{9}|\\+98\\d{10})$");
        Matcher matcher = pattern.matcher(number);
        return matcher.find();

    }

    public static boolean matchIdNumber(String idNumber) {
        Pattern pattern = Pattern.compile("^\\d{10}$");
        Matcher matcher = pattern.matcher(idNumber);
        return matcher.find();
    }

    public static boolean matchIsbn(String isbn) {
        Pattern pattern = Pattern.compile("^\\d{13}$");
        Matcher matcher = pattern.matcher(isbn);
        return matcher.find();
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    
    



}
