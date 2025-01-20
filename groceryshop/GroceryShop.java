package groceryshop;

import java.util.Scanner;

public class GroceryShop {

    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        DatabaseManager.connectDatabase();

        while (true) {
            System.out.println("\nWelcome to Grocery Shop");
            System.out.println("1. Signup");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");

            int choice = input.nextInt();
            input.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> UserManager.signup();
                case 2 -> UserManager.login();
                case 3 -> {
                    System.out.println("Exiting the application. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
