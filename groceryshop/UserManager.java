package groceryshop;

import java.sql.*;
import java.util.Scanner;

public class UserManager {

    private static final Scanner input = new Scanner(System.in);

    private static boolean isValidBangladeshiPhone(String phone) {
        return phone.matches("01[3-9]\\d{8}");
    }

    public static void signup() {
        try {
            System.out.println("Enter your phone number: ");
            String phone = input.nextLine();

            if (!isValidBangladeshiPhone(phone)) {
                System.out.println("Invalid phone number. Please enter a valid Bangladeshi phone number.");
                return;
            }

            System.out.println("Enter your name: ");
            String name = input.nextLine();

            System.out.println("Enter your address: ");
            String address = input.nextLine();

            System.out.println("Enter your PIN: ");
            String pin = input.nextLine();

            String query = "INSERT INTO users (phone, name, address, pin) VALUES (?, ?, ?, ?);";
            PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(query);
            stmt.setString(1, phone);
            stmt.setString(2, name);
            stmt.setString(3, address);
            stmt.setString(4, pin);

            stmt.executeUpdate();
            System.out.println("Signup successful!");

        } catch (SQLException e) {
            System.out.println("Signup failed: " + e.getMessage());
        }
    }

    public static void login() {
        try {
            System.out.println("Enter your phone number: ");
            String phone = input.nextLine();

            if (!isValidBangladeshiPhone(phone)) {
                System.out.println("Invalid phone number. Please enter a valid Bangladeshi phone number.");
                return;
            }

            System.out.println("Enter your PIN: ");
            String pin = input.nextLine();

            String query = "SELECT * FROM users WHERE phone = ? AND pin = ?;";
            PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(query);
            stmt.setString(1, phone);
            stmt.setString(2, pin);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful! Welcome, " + rs.getString("name") + ".");
                ProductManager.mainMenu(phone);
            } else {
                System.out.println("Invalid credentials.");
            }

        } catch (SQLException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }
}
