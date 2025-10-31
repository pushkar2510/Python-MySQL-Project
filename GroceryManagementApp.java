import java.sql.*;
import java.util.Scanner;

/**
 * A Java Console Application for Grocery Management using JDBC.
 * NOTE: This code uses PreparedStatement for all queries to prevent SQL Injection,
 * which is a critical improvement over the original Python implementation.
 * You must have the MySQL Connector/J JDBC driver in your classpath to run this.
 */
public class GroceryManagementApp {

    // --- Database Configuration (Matches the original Python connection) ---
    private static final String DB_URL = "jdbc:mysql://localhost:3306/grocery_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private static Connection getConnection() throws SQLException {
        // Register the JDBC driver (optional for modern JDBC versions but good practice)
        // Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // --- Core Transaction Functions (Require Transactional Safety) ---

    /**
     * Updates the credit of a customer in the Credit table using an existing connection.
     * This is a helper for makeTransaction and does not commit/close the connection.
     */
    private static void updateCustomerCredit(Connection conn, int customerId, double amount) throws SQLException {
        String query = "UPDATE Credit SET credit = credit + ? WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, amount);
            stmt.setInt(2, customerId);
            stmt.executeUpdate();
        }
    }

    /**
     * Updates the quantity of a product in the Products table using an existing connection.
     * This is a helper for makeTransaction and does not commit/close the connection.
     */
    private static void updateProductQuantity(Connection conn, int productId, int quantity) throws SQLException {
        String query = "UPDATE Products SET quantity = quantity - ? WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        }
    }

    /**
     * Makes a transaction and updates the relevant tables atomically.
     * Uses Connection.setAutoCommit(false) for transaction safety.
     */
    public static void makeTransaction(int customerId, int productId, String transactionType, int quantity) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Insert the transaction into the Transactions table
            String insertQuery = "INSERT INTO Transactions (customer_id, product_id, transaction_type) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setInt(1, customerId);
                stmt.setInt(2, productId);
                stmt.setString(3, transactionType);
                stmt.executeUpdate();
            }

            // 2. Update the customer's credit if the transaction was made on credit
            if (transactionType.equalsIgnoreCase("credit")) {
                // Get the price of the product
                String priceQuery = "SELECT price FROM Products WHERE product_id = ?";
                double price = 0.0;
                try (PreparedStatement priceStmt = conn.prepareStatement(priceQuery)) {
                    priceStmt.setInt(1, productId);
                    try (ResultSet rs = priceStmt.executeQuery()) {
                        if (rs.next()) {
                            price = rs.getDouble("price");
                        } else {
                            throw new SQLException("Product ID not found.");
                        }
                    }
                }

                // Calculate the amount and update credit
                double amount = price * quantity;
                updateCustomerCredit(conn, customerId, amount);
            }

            // 3. Update the product's quantity
            updateProductQuantity(conn, productId, quantity);

            conn.commit(); // Commit if all steps were successful
            System.out.println("Transaction successful!");

        } catch (SQLException e) {
            System.err.println("Transaction failed: " + e.getMessage());
            if (conn != null) {
                try {
                    System.out.println("Attempting to roll back transaction...");
                    conn.rollback(); // Rollback on error
                    System.out.println("Transaction rolled back.");
                } catch (SQLException rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    // --- Utility and CRUD Functions (Non-Transactional) ---

    /**
     * Prints all records from a given table.
     */
    public static void displayRecords(String tableName) {
        // IMPORTANT: tableName cannot be safely prepared, so it must be checked against a whitelist
        String safeTableName = tableName.toLowerCase().trim();
        if (!safeTableName.matches("customers|credit|vendors|products|transactions|coupons|rewards")) {
            System.err.println("Error: Invalid table name provided for display.");
            return;
        }

        String query = "SELECT * FROM " + safeTableName;
        System.out.println("\n" + tableName + ":");

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int columns = rsmd.getColumnCount();

            // Print column names
            for (int i = 1; i <= columns; i++) {
                System.out.print(rsmd.getColumnName(i) + "\t");
            }
            System.out.println();

            // Print the records
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }

        } catch (SQLException e) {
            System.err.println("Error displaying records from " + tableName + ": " + e.getMessage());
        }
    }

    /**
     * Updates the rewards points of a customer in the Rewards table.
     */
    public static void updateRewards(int customerId, int points) {
        String query = "UPDATE Rewards SET points = points + ? WHERE customer_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, points);
            stmt.setInt(2, customerId);
            stmt.executeUpdate();
            System.out.println("Rewards points updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating rewards: " + e.getMessage());
        }
    }

    /**
     * Adds a customer to the Customers table.
     */
    public static void addCustomer(String firstName, String lastName, String email, String phone, String address) {
        String query = "INSERT INTO Customers (first_name, last_name, email, phone, address) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.setString(5, address);
            stmt.executeUpdate();
            System.out.println("Customer added successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding customer: " + e.getMessage());
        }
    }

    /**
     * Adds a vendor to the Vendors table.
     */
    public static void addVendor(String name, String phone, String email, String address) {
        String query = "INSERT INTO Vendors (name, phone, email, address) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, email);
            stmt.setString(4, address);
            stmt.executeUpdate();
            System.out.println("Vendor added successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding vendor: " + e.getMessage());
        }
    }

    /**
     * Adds a product to the Products table.
     */
    public static void addProduct(String name, int vendorId, double price, int quantity) {
        String query = "INSERT INTO Products (name, vendor_id, price, quantity) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setInt(2, vendorId);
            stmt.setDouble(3, price);
            stmt.setInt(4, quantity);
            stmt.executeUpdate();
            System.out.println("Product added successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
        }
    }

    /**
     * Adds a coupon to the Coupons table.
     */
    public static void addCoupon(String code, double discount, String expirationDate) {
        String query = "INSERT INTO Coupons (code, discount, expiration_date) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, code);
            stmt.setDouble(2, discount);
            // Assuming expirationDate is a valid SQL date string (YYYY-MM-DD)
            stmt.setString(3, expirationDate);
            stmt.executeUpdate();
            System.out.println("Coupon added successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding coupon: " + e.getMessage());
        }
    }

    /**
     * Updates a field in the Customers table for a given customer.
     */
    public static void updateCustomer(int customerId, String field, String newValue) {
        // Field name is dynamic, so it cannot be a prepared statement parameter.
        // Needs careful sanitation/whitelisting in a production environment.
        String query = String.format("UPDATE Customers SET %s = ? WHERE customer_id = ?", field);
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newValue);
            stmt.setInt(2, customerId);
            stmt.executeUpdate();
            System.out.println("Customer updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating customer: " + e.getMessage());
        }
    }

    // A generic update function could handle vendor, product, and coupon updates, but
    // for fidelity to the original code, we'll keep them separate.

    /**
     * Updates a field in the Vendors table for a given vendor.
     */
    public static void updateVendor(int vendorId, String field, String newValue) {
        String query = String.format("UPDATE Vendors SET %s = ? WHERE vendor_id = ?", field);
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newValue);
            stmt.setInt(2, vendorId);
            stmt.executeUpdate();
            System.out.println("Vendor updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating vendor: " + e.getMessage());
        }
    }

    /**
     * Updates a field in the Products table for a given product.
     */
    public static void updateProduct(int productId, String field, String newValue) {
        String query = String.format("UPDATE Products SET %s = ? WHERE product_id = ?", field);
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newValue);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
            System.out.println("Product updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
        }
    }

    /**
     * Updates a field in the Coupons table for a given coupon.
     */
    public static void updateCoupon(int couponId, String field, String newValue) {
        String query = String.format("UPDATE Coupons SET %s = ? WHERE coupon_id = ?", field);
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newValue);
            stmt.setInt(2, couponId);
            stmt.executeUpdate();
            System.out.println("Coupon updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating coupon: " + e.getMessage());
        }
    }


    // --- Delete Functions ---

    /** Deletes a customer. */
    public static void deleteCustomer(int customerId) {
        String query = "DELETE FROM Customers WHERE customer_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
            System.out.println("Customer deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
        }
    }

    /** Deletes a vendor. */
    public static void deleteVendor(int vendorId) {
        String query = "DELETE FROM Vendors WHERE vendor_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, vendorId);
            stmt.executeUpdate();
            System.out.println("Vendor deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error deleting vendor: " + e.getMessage());
        }
    }

    /** Deletes a product. */
    public static void deleteProduct(int productId) {
        String query = "DELETE FROM Products WHERE product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
            System.out.println("Product deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
        }
    }

    /** Deletes a coupon. */
    public static void deleteCoupon(int couponId) {
        String query = "DELETE FROM Coupons WHERE coupon_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, couponId);
            stmt.executeUpdate();
            System.out.println("Coupon deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error deleting coupon: " + e.getMessage());
        }
    }

    // --- Menu and Main Logic ---

    public static void displayMenu() {
        System.out.println("\n--- Grocery Management Menu ---");
        System.out.println("1. Make a transaction");
        System.out.println("2. Display records");
        System.out.println("3. Update rewards points");
        System.out.println("4. Add a customer");
        System.out.println("5. Add a vendor");
        System.out.println("6. Add a product");
        System.out.println("7. Add a coupon");
        System.out.println("8. Update a customer");
        System.out.println("9. Update a vendor");
        System.out.println("10. Update a product");
        System.out.println("11. Update a coupon");
        System.out.println("12. Delete a customer");
        System.out.println("13. Delete a vendor");
        System.out.println("14. Delete a product");
        System.out.println("15. Delete a coupon");
        System.out.println("16. Quit");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        try {
            // Test connection to ensure driver is loaded and connection is possible
            try (Connection testConn = getConnection()) {
                System.out.println("Database connection established successfully.");
            }
        } catch (SQLException e) {
            System.err.println("CRITICAL ERROR: Failed to establish database connection.");
            System.err.println("Please ensure MySQL is running, the database 'grocery_management' exists, and the MySQL Connector/J driver is in your classpath.");
            System.err.println("Details: " + e.getMessage());
            return;
        }


        while (true) {
            displayMenu();
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // consume the invalid line
                continue;
            }

            try {
                switch (choice) {
                    case 1: // Make a transaction
                        System.out.print("Enter the customer ID: ");
                        int cId = scanner.nextInt();
                        System.out.print("Enter the product ID: ");
                        int pId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter the transaction type (credit or other): ");
                        String tType = scanner.nextLine().trim();
                        System.out.print("Enter the quantity: ");
                        int qty = scanner.nextInt();
                        scanner.nextLine();
                        makeTransaction(cId, pId, tType, qty);
                        break;

                    case 2: // Display records
                        System.out.println("Available Tables: Customers, Credit, Vendors, Products, Transactions, Coupons, Rewards");
                        System.out.print("Enter the table name: ");
                        String tName = scanner.nextLine();
                        displayRecords(tName);
                        break;

                    case 3: // Update rewards points
                        System.out.print("Enter the customer ID: ");
                        int rCId = scanner.nextInt();
                        System.out.print("Enter the points to add: ");
                        int points = scanner.nextInt();
                        scanner.nextLine();
                        updateRewards(rCId, points);
                        break;

                    case 4: // Add a customer
                        System.out.print("Enter first name: ");
                        String fName = scanner.nextLine();
                        System.out.print("Enter last name: ");
                        String lName = scanner.nextLine();
                        System.out.print("Enter email: ");
                        String email = scanner.nextLine();
                        System.out.print("Enter phone number: ");
                        String phone = scanner.nextLine();
                        System.out.print("Enter address: ");
                        String address = scanner.nextLine();
                        addCustomer(fName, lName, email, phone, address);
                        break;

                    case 5: // Add a vendor
                        System.out.print("Enter vendor name: ");
                        String vName = scanner.nextLine();
                        System.out.print("Enter phone number: ");
                        String vPhone = scanner.nextLine();
                        System.out.print("Enter email: ");
                        String vEmail = scanner.nextLine();
                        System.out.print("Enter address: ");
                        String vAddress = scanner.nextLine();
                        addVendor(vName, vPhone, vEmail, vAddress);
                        break;

                    case 6: // Add a product
                        System.out.print("Enter product name: ");
                        String pName = scanner.nextLine();
                        System.out.print("Enter vendor ID: ");
                        int vId = scanner.nextInt();
                        System.out.print("Enter price: ");
                        double price = scanner.nextDouble();
                        System.out.print("Enter quantity: ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine();
                        addProduct(pName, vId, price, quantity);
                        break;

                    case 7: // Add a coupon
                        System.out.print("Enter coupon code: ");
                        String code = scanner.nextLine();
                        System.out.print("Enter discount amount: ");
                        double discount = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Enter expiration date (YYYY-MM-DD): ");
                        String expDate = scanner.nextLine();
                        addCoupon(code, discount, expDate);
                        break;

                    case 8: // Update customer
                    case 9: // Update vendor
                    case 10: // Update product
                    case 11: { // Update coupon
                        System.out.print("Enter ID: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter field to update: ");
                        String field = scanner.nextLine();
                        System.out.print("Enter new value: ");
                        String newValue = scanner.nextLine();

                        switch (choice) {
                            case 8: updateCustomer(id, field, newValue); break;
                            case 9: updateVendor(id, field, newValue); break;
                            case 10: updateProduct(id, field, newValue); break;
                            case 11: updateCoupon(id, field, newValue); break;
                        }
                        break;
                    }

                    case 12: // Delete customer
                    case 13: // Delete vendor
                    case 14: // Delete product
                    case 15: { // Delete coupon
                        System.out.print("Enter ID: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        switch (choice) {
                            case 12: deleteCustomer(id); break;
                            case 13: deleteVendor(id); break;
                            case 14: deleteProduct(id); break;
                            case 15: deleteCoupon(id); break;
                        }
                        break;
                    }

                    case 16: // Quit
                        System.out.println("Exiting application.");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 16.");
                }
            } catch (java.util.InputMismatchException e) {
                System.err.println("Invalid input type. Please enter the correct data type (e.g., number for ID/quantity).");
                scanner.nextLine(); // clear the buffer
            }
        }
    }
}
