import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class customerDashboard extends JFrame {
    private final int customer_id;

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
        JButton logoutButton = new JButton("Logout");
        JButton browseItemsButton = new JButton("Browse Items");
        JButton viewOrdersButton = new JButton("View Orders");
        JButton placeOrderButton = new JButton("Place Order");

        // Actions
        updateInfoButton.addActionListener(e -> customerUpdateInfo());
        logoutButton.addActionListener(e -> logout());
        browseItemsButton.addActionListener(e -> browseItems());
        viewOrdersButton.addActionListener(e -> viewOrders());
        placeOrderButton.addActionListener(e -> placeOrder());

        // Add buttons
        buttonPanel.add(updateInfoButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(browseItemsButton);
        buttonPanel.add(viewOrdersButton);
        buttonPanel.add(placeOrderButton);

        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void customerUpdateInfo() {
        new customerUpdateInfo(customer_id);
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
        try (Connection conn = database.connection()) {
            // Check cart
            PreparedStatement getCart = conn.prepareStatement("SELECT * FROM cart WHERE customer_id = ?");
            getCart.setInt(1, customer_id);
            ResultSet rs = getCart.executeQuery();

            double total = 0.0;
            boolean hasItems = false;
            while (rs.next()) {
                double price = rs.getDouble("item_price");
                int quantity = rs.getInt("item_quantity");
                total += price * quantity;
                hasItems = true;
            }

            if (!hasItems) {
                JOptionPane.showMessageDialog(this, "Your cart is empty.");
                return;
            }

            // Insert order
            PreparedStatement insertOrder = conn.prepareStatement(
                    "INSERT INTO Orders (customer_id, order_date_time, delivery_date, coupon_code) VALUES (?, NOW(), ?, NULL)",
                    Statement.RETURN_GENERATED_KEYS
            );
            insertOrder.setInt(1, customer_id);
            insertOrder.setDate(2, Date.valueOf(LocalDate.now().plusDays(5))); // 5 days delivery
            insertOrder.executeUpdate();

            ResultSet generatedKeys = insertOrder.getGeneratedKeys();
            int orderId = 0;
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            }

            // Add to OrderOfItems
            PreparedStatement cartItems = conn.prepareStatement("SELECT item_id, item_quantity FROM cart WHERE customer_id = ?");
            cartItems.setInt(1, customer_id);
            ResultSet items = cartItems.executeQuery();

            PreparedStatement insertOrderItems = conn.prepareStatement(
                    "INSERT INTO OrderOfItems (order_id, item_id, quantity, item_status) VALUES (?, ?, ?, ?)"
            );

            while (items.next()) {
                insertOrderItems.setInt(1, orderId);
                insertOrderItems.setInt(2, items.getInt("item_id"));
                insertOrderItems.setInt(3, items.getInt("item_quantity"));
                insertOrderItems.setString(4, "pending");
                insertOrderItems.executeUpdate();
            }

            // Clear cart
            PreparedStatement clearCart = conn.prepareStatement("DELETE FROM cart WHERE customer_id = ?");
            clearCart.setInt(1, customer_id);
            clearCart.executeUpdate();

            JOptionPane.showMessageDialog(this, "Order placed successfully! Total: $" + total);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while placing the order.");
        }
    }
}

