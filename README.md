# Grocery-Shop-Project-using-JAVA

The Grocery Shop project is a Java-based console application designed to manage a digital grocery store. It supports two categories of products: **Daily Grocery** and **Medicine**. The application integrates with a MySQL database for user management, product inventory, cart operations, and transactions.

---

## Features
### User Management
- **Signup**: Register with a phone number, name, address, and PIN.
- **Login**: Authenticate using phone number and PIN.

### Product Management
- **View Products**: Browse products by category (Daily Grocery or Medicine).

### Cart Operations
- **Add to Cart**: Add products to the cart with stock validation.
- **View Cart**: View items in the cart along with the total price.
- **Checkout**: Deduct product stock and clear the cart after a successful transaction.

---

## Project Structure
```
GroceryShop/
├── src/
│   ├── GroceryShop.java         # Main application class
│   ├── DatabaseManager.java     # Handles database connection
│   ├── UserManager.java         # Handles user signup and login
│   ├── ProductManager.java      # Manages both daily grocery and medicine operations
│   ├── CartManager.java         # Handles cart operations
├── resources/
│   ├── schema.sql               # SQL script to create and populate the database
├── lib/
│   ├── mysql-connector-java.jar # MySQL JDBC driver
└── README.md                    # Project documentation
```

---

## Prerequisites
1. **Java JDK 8 or Higher**
2. **MySQL Database**
3. **MySQL JDBC Driver**

---

## Database Setup
1. Create the database:
   ```sql
   CREATE DATABASE grocery_shop;
   USE grocery_shop;
   ```

2. Create the tables:
   
   CREATE TABLE users (
       phone VARCHAR(15) PRIMARY KEY,
       name VARCHAR(50) NOT NULL,
       address VARCHAR(100) NOT NULL,
       pin VARCHAR(10) NOT NULL
   );

   CREATE TABLE products (
       id INT AUTO_INCREMENT PRIMARY KEY,
       name VARCHAR(50) NOT NULL,
       category ENUM('Daily Grocery', 'Medicine') NOT NULL,
       price DECIMAL(10, 2) NOT NULL,
       stock INT NOT NULL
   );

   CREATE TABLE cart (
       phone VARCHAR(15),
       product_id INT,
       category ENUM('Daily Grocery', 'Medicine') NOT NULL,
       quantity INT NOT NULL,
       PRIMARY KEY (phone, product_id, category),
       FOREIGN KEY (phone) REFERENCES users(phone),
       FOREIGN KEY (product_id) REFERENCES products(id)
   );
   

3. Populate the products table:
   
   INSERT INTO products (name, category, price, stock) VALUES
   ('Rice', 'Daily Grocery', 65.00, 100),
   ('Potato', 'Daily Grocery', 25.00, 200),
   ('Onion', 'Daily Grocery', 35.00, 150),
   ('Sugar', 'Daily Grocery', 90.00, 50),
   ('Paracetamol', 'Medicine', 1.00, 500),
   ('Antibiotic', 'Medicine', 5.00, 300),
   ('Vitamin C', 'Medicine', 2.00, 250);
   

---

## How to Run
1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd GroceryShop
   ```

2. **Add the MySQL Connector JAR**:
   - Place the `mysql-connector-java.jar` file in the `lib/` directory.
   - Ensure the classpath includes the JAR file.

3. **Compile the Code**:
   ```bash
   javac -cp .:lib/mysql-connector-java.jar src/*.java
   ```

4. **Run the Application**:
   ```bash
   java -cp .:lib/mysql-connector-java.jar src.GroceryShop
   ```

---

## Usage
1. Start the application.
2. Register as a new user or log in.
3. Browse products by category and add items to the cart.
4. View the cart and proceed to checkout.

---

## Validation Rules
- **Phone Number Validation**:
  - Must start with `01` and follow Bangladeshi SIM prefixes (`3-9`).
  - Example: `01712345678`, `01987654321`.

- **Stock Validation**:
  - Ensures sufficient stock is available before adding items to the cart or checking out.

---

## Future Enhancements
1. Add a graphical user interface (GUI) using JavaFX or Swing.
2. Implement role-based access (e.g., admin for inventory management).
3. Add support for online payment gateways.

---

## Contributors
- **Rafsan Hasan Pronoy** - Developer

---

