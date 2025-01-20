package groceryshop;

import java.sql.*;
import java.util.Scanner;

public class ProductManager {

    private static final Scanner input = new Scanner(System.in);

    public static void mainMenu(String phone) {
        while (true) {
            System.out.println("\nMain Menu");
            System.out.println("1. Daily Grocery");
            System.out.println("2. Medicine");
            System.out.println("3. Logout");
            System.out.print("Select an option: ");

            int choice = input.nextInt();
            input.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> categoryMenu("Daily Grocery", phone);
                case 2 -> categoryMenu("Medicine", phone);
                case 3 -> {
                    System.out.println("Logged out successfully.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    public static void categoryMenu(String category, String phone) {
        while (true) {
            System.out.printf("\n%s Menu\n", category);
            System.out.println("1. View Products");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Checkout");
            System.out.println("5. Back to Main Menu");
            System.out.print("Select an option: ");

            int choice = input.nextInt();
            input.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> viewProductsByCategory(category);
                case 2 -> {
                    System.out.println("Enter the product ID to add to your cart: ");
                    int productId = input.nextInt();
                    input.nextLine(); // Consume newline

                    System.out.println("Enter the quantity: ");
                    int quantity = input.nextInt();
                    input.nextLine(); // Consume newline

                    CartManager.addToCart(category, phone, productId, quantity);
                }
                case 3 -> CartManager.viewCartByCategory(category, phone);
                case 4 -> CartManager.checkoutByCategory(category, phone);
                case 5 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }


    public static void viewProductsByCategory(String category) {
        try {
            String query = "SELECT * FROM products WHERE category = ?;";
            PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(query);
            stmt.setString(1, category);

            ResultSet rs = stmt.executeQuery();

            System.out.println("\nAvailable Products:");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Price: %.2f | Stock: %d\n",
                        rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("stock"));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }
    }
}