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
        String couponCode = JOptionPane.showInputDialog(this, "Enter coupon code:");
        double discount = 0.0;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee", "root", "database28")) {
            if (couponCode != null && !couponCode.trim().isEmpty()) {
                PreparedStatement couponCheck = conn.prepareStatement("SELECT discount_percent FROM coupons WHERE code = ?");
                couponCheck.setString(1, couponCode);
                ResultSet rs = couponCheck.executeQuery();
                if (rs.next()) {
                    discount = rs.getDouble("discount_percent") / 100.0;
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid coupon code.");
                }
            }

            PreparedStatement getCart = conn.prepareStatement("SELECT * FROM cart WHERE customer_id = ?");
            getCart.setInt(1, customer_id);
            ResultSet rs = getCart.executeQuery();

            double total = 0.0;
            while (rs.next()) {
                double price = Double.parseDouble(rs.getString("item_price"));
                int quantity = rs.getInt("item_quantity");
                total += price * quantity;
            }

            if (total == 0.0) {
                JOptionPane.showMessageDialog(this, "Your cart is empty.");
                return;
            }

            total = total - (total * discount);

            PreparedStatement insertOrder = conn.prepareStatement("INSERT INTO orders (customer_id, total_price, coupon_code) VALUES (?, ?, ?)");
            insertOrder.setInt(1, customer_id);
            insertOrder.setDouble(2, total);
            insertOrder.setString(3, couponCode);
            insertOrder.executeUpdate();

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
