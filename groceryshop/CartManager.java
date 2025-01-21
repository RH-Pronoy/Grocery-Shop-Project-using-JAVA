package groceryshop;

import java.sql.*;
//import java.util.Scanner;

public class CartManager {

  //  private static final Scanner input = new Scanner(System.in);

    public static void addToCart(String category, String phone, int productId, int quantity) {
        try {
            // Check stock availability
            String stockQuery = "SELECT stock FROM products WHERE id = ?;";
            PreparedStatement stockStmt = DatabaseManager.getConnection().prepareStatement(stockQuery);
            stockStmt.setInt(1, productId);

            ResultSet stockRs = stockStmt.executeQuery();

            if (stockRs.next() && stockRs.getInt("stock") >= quantity) {
                String cartQuery = "INSERT INTO cart (phone, product_id, category, quantity) VALUES (?, ?, ?, ?) " +
                                   "ON DUPLICATE KEY UPDATE quantity = quantity + ?;";
                PreparedStatement cartStmt = DatabaseManager.getConnection().prepareStatement(cartQuery);
                cartStmt.setString(1, phone);
                cartStmt.setInt(2, productId);
                cartStmt.setString(3, category);
                cartStmt.setInt(4, quantity);
                cartStmt.setInt(5, quantity);
                cartStmt.executeUpdate();

                System.out.println("Product added to cart.");
            } else {
                System.out.println("Insufficient stock.");
            }

        } catch (SQLException e) {
            System.out.println("Error adding to cart: " + e.getMessage());
        }
    }

    public static void viewCartByCategory(String category, String phone) {
        try {
            String query = "SELECT p.name, c.quantity, p.price, (c.quantity * p.price) AS total FROM cart c " +
                           "JOIN products p ON c.product_id = p.id WHERE c.phone = ? AND c.category = ?;";
            PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(query);
            stmt.setString(1, phone);
            stmt.setString(2, category);

            ResultSet rs = stmt.executeQuery();

            System.out.println("\nYour Cart:");
            double grandTotal = 0;

            while (rs.next()) {
                System.out.printf("Product: %s | Quantity: %d | Price: %.2f | Total: %.2f\n",
                        rs.getString("name"), rs.getInt("quantity"), rs.getDouble("price"), rs.getDouble("total"));
                grandTotal += rs.getDouble("total");
            }

            System.out.printf("\nGrand Total: %.2f\n", grandTotal);

        } catch (SQLException e) {
            System.out.println("Error fetching cart: " + e.getMessage());
        }
    }

    public static void checkoutByCategory(String category, String phone) {
        try {
            DatabaseManager.getConnection().setAutoCommit(false);

            // Fetch cart items for the category
            String cartQuery = "SELECT product_id, quantity FROM cart WHERE phone = ? AND category = ?;";
            PreparedStatement cartStmt = DatabaseManager.getConnection().prepareStatement(cartQuery);
            cartStmt.setString(1, phone);
            cartStmt.setString(2, category);

            ResultSet cartRs = cartStmt.executeQuery();

            while (cartRs.next()) {
                int productId = cartRs.getInt("product_id");
                int quantity = cartRs.getInt("quantity");

                // Deduct stock for each product
                String stockUpdate = "UPDATE products SET stock = stock - ? WHERE id = ?;";
                PreparedStatement stockStmt = DatabaseManager.getConnection().prepareStatement(stockUpdate);
                stockStmt.setInt(1, quantity);
                stockStmt.setInt(2, productId);
                stockStmt.executeUpdate();
            }

            // Clear cart for the category
            String clearCartQuery = "DELETE FROM cart WHERE phone = ? AND category = ?;";
            PreparedStatement clearCartStmt = DatabaseManager.getConnection().prepareStatement(clearCartQuery);
            clearCartStmt.setString(1, phone);
            clearCartStmt.setString(2, category);
            clearCartStmt.executeUpdate();

            DatabaseManager.getConnection().commit();
            System.out.println("Checkout successful. Thank you for shopping!");

        } catch (SQLException e) {
            try {
                DatabaseManager.getConnection().rollback();
                System.out.println("Checkout failed. Transaction rolled back: " + e.getMessage());
            } catch (SQLException rollbackEx) {
                System.out.println("Rollback failed: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                DatabaseManager.getConnection().setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Error resetting auto-commit: " + ex.getMessage());
            }
        }
    }
}
