import javax.swing.*; 
import java.awt.*;
import java.sql.*;

public class customerDashboard extends JFrame {
    private int customer_id;

    public customerDashboard(int customer_id) {
        this.customer_id = customer_id;
        setTitle("Customer Dashboard");
        setSize(800, 700);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome to the Customer Dashboard!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 10, 10));
       import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class customerDashboard extends JFrame {
    private int customer_id;

    public customerDashboard(int customer_id) {
        this.customer_id = customer_id;
        setTitle("Customer Dashboard");
        setSize(800, 700);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome to the Customer Dashboard!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton updateInfoButton = new JButton("Update Info");
        JButton logoutButton = new JButton("Logout?");
        JButton browseItemsButton = new JButton("Browse Items");
        JButton viewOrdersButton = new JButton("View Orders");
        JButton placeOrderButton = new JButton("Place Order");

        updateInfoButton.addActionListener(e -> customerUpdateInfo(customer_id));
        logoutButton.addActionListener(e -> logout());
        browseItemsButton.addActionListener(e -> browseItems());
        viewOrdersButton.addActionListener(e -> viewOrders());
        placeOrderButton.addActionListener(e -> placeOrder());

        buttonPanel.add(updateInfoButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(browseItemsButton);
        buttonPanel.add(viewOrdersButton);
        buttonPanel.add(placeOrderButton);

        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void customerUpdateInfo(int customerId) {
        new customerUpdateInfo(customerId);
    }

    private void logout() {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            dispose();
            new customerLogin();
        }
    }

    private void browseItems() {
        new browseItems(customer_id);
    }

    private void viewOrders() {
        new viewOrders(customer_id);
    }

    private void placeOrder() {
        String couponCodeInput = JOptionPane.showInputDialog(this, "Enter coupon code (optional):");
        double discount = 0.0;
        int couponId = 0;

        try (Connection conn = database.connection()) {
            // Check for valid coupon
            if (couponCodeInput != null && !couponCodeInput.trim().isEmpty()) {
                PreparedStatement couponCheck = conn.prepareStatement(
                        "SELECT code_id, discount_percent FROM coupons WHERE code = ?");
                couponCheck.setString(1, couponCodeInput);
                ResultSet rs = couponCheck.executeQuery();
                if (rs.next()) {
                    couponId = rs.getInt("code_id");
                    discount = rs.getDouble("discount_percent") / 100.0;
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid coupon code.");
                }
            }

            // Get cart total
            PreparedStatement getCart = conn.prepareStatement(
                    "SELECT item_price, item_quantity FROM cart WHERE customer_id = ?");
            getCart.setInt(1, customer_id);
            ResultSet rs = getCart.executeQuery();

            double total = 0.0;
            while (rs.next()) {
                double price = rs.getDouble("item_price");
                int quantity = rs.getInt("item_quantity");
                total += price * quantity;
            }

            if (total == 0.0) {
                JOptionPane.showMessageDialog(this, "Your cart is empty.");
                return;
            }

            // Apply discount
            total -= (total * discount);

            // Insert order
            PreparedStatement insertOrder = conn.prepareStatement(
                    "INSERT INTO Orders (customer_id, order_date_time, delivery_date, coupon_code) VALUES (?, NOW(), ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            insertOrder.setInt(1, customer_id);
            insertOrder.setDate(2, java.sql.Date.valueOf(LocalDate.now().plusDays(7))); // delivery in 7 days

            if (couponId > 0) {
                insertOrder.setInt(3, couponId);
            } else {
                insertOrder.setNull(3, Types.INTEGER);
            }

            insertOrder.executeUpdate();

            // Get order ID
            ResultSet generatedKeys = insertOrder.getGeneratedKeys();
            int orderId = 0;
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            }

            // Transfer cart items to OrderOfItems
            PreparedStatement getItems = conn.prepareStatement(
                    "SELECT item_id, item_quantity FROM cart WHERE customer_id = ?");
            getItems.setInt(1, customer_id);
            ResultSet cartItems = getItems.executeQuery();

            PreparedStatement insertOrderItem = conn.prepareStatement(
                    "INSERT INTO OrderOfItems (order_id, item_id, quantity, item_status) VALUES (?, ?, ?, ?)");

            while (cartItems.next()) {
                int itemId = cartItems.getInt("item_id");
                int qty = cartItems.getInt("item_quantity");

                insertOrderItem.setInt(1, orderId);
                insertOrderItem.setInt(2, itemId);
                insertOrderItem.setInt(3, qty);
                insertOrderItem.setString(4, "pending");
                insertOrderItem.executeUpdate();
            }

            // Clear the cart
            PreparedStatement clearCart = conn.prepareStatement("DELETE FROM cart WHERE customer_id = ?");
            clearCart.setInt(1, customer_id);
            clearCart.executeUpdate();

            JOptionPane.showMessageDialog(this, "Order placed successfully! Total: $" + total);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error placing order.");
        }
    }
}

