package ir.ac.kntu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import ir.ac.kntu.helper.ConsoleColors;
import ir.ac.kntu.model.User;

public class VendiloPlusDAO {

    private static final String DB_URL = "jdbc:sqlite:data.db";

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS vendilo_plus ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "email TEXT NOT NULL,"
                + "date TEXT NOT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Table created or already exists.");

        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }

    public static void insertToClub(User user, int month) {
        String query1 = "INSERT INTO vendilo_plus(email, date) "
                + "VALUES (?, ?)";

        Instant simulatedNow = ir.ac.kntu.helper.Calendar.now();
        ZonedDateTime monthsLater = simulatedNow.atZone(ZoneId.systemDefault()).plusMonths(month);
        String updatedInstant = monthsLater.toString();

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement insertStmt = conn.prepareStatement(query1)) {

            insertStmt.setString(1, user.getEmail());
            insertStmt.setString(2, updatedInstant);

            insertStmt.executeUpdate();
            System.out.println(ConsoleColors.GREEN + "User inserted to club successfully." + ConsoleColors.RESET);
        } catch (SQLException e) {
            System.out.println(ConsoleColors.RED + "Insert failed: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

 
    public static boolean vendiloPlusUser(User user) {
    String query = "SELECT date FROM vendilo_plus WHERE email = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, user.getEmail());
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String dateString = rs.getString("date");

            Instant now = ir.ac.kntu.helper.Calendar.now();
            Instant dateInstant = Instant.parse(dateString.trim());

            return now.isBefore(dateInstant); 
        }

        return false;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}


    

}
