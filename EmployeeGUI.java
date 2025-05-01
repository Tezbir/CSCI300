import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class EmployeeGUI extends JFrame {
    public EmployeeGUI() {
        setTitle("Employee Dashboard");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1, 10, 10));
        setLocationRelativeTo(null);

        JButton addItemButton = new JButton("Add New Item to Store");
        JButton viewCustomersButton = new JButton("View All Customers");
        JButton viewOrdersButton = new JButton("View All Orders");
        JButton createCouponButton = new JButton("Create Coupon Code");
        JButton logoutButton = new JButton("Logout");

        add(addItemButton);
        add(viewCustomersButton);
        add(viewOrdersButton);
        add(createCouponButton);
        add(logoutButton);

        addItemButton.addActionListener(e -> addItemToStore());
        viewCustomersButton.addActionListener(e -> viewCustomers());
        viewOrdersButton.addActionListener(e -> viewOrders());
        createCouponButton.addActionListener(e -> createCoupon());
        logoutButton.addActionListener(e -> {
            dispose();
            new EmployeeLogin();
        });

        setVisible(true);
    }

    private void addItemToStore() {
        JTextField itemName = new JTextField();
        JTextField price = new JTextField();
        JTextField quantity = new JTextField();
        JTextField creatorId = new JTextField();

        Object[] fields = {
            "Item Name:", itemName,
            "Price:", price,
            "Quantity:", quantity,
            "Created By (Employee ID):", creatorId
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add New Item", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try (Connection conn = database.connection()) {
                String sql = "INSERT INTO online_store.Items (item_name, item_price, quantity_in_stock, created_by) VALUES (?, ?, ?, ?)";
                PreparedStatement stm = conn.prepareStatement(sql);
                stm.setString(1, itemName.getText());
                stm.setDouble(2, Double.parseDouble(price.getText()));
                stm.setInt(3, Integer.parseInt(quantity.getText()));
                stm.setInt(4, Integer.parseInt(creatorId.getText()));
                stm.executeUpdate();
                JOptionPane.showMessageDialog(this, "Item added successfully!");
            } catch (SQLException | NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding item. Please check inputs.");
            }
        }
    }

    private void viewCustomers() {
        try (Connection conn = database.connection()) {
            String sql = "SELECT customer_id, customer_name, customer_username FROM online_store.Customers";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            StringBuilder sb = new StringBuilder("Customers:\n");
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("customer_id"))
                  .append(", Name: ").append(rs.getString("customer_name"))
                  .append(", Username: ").append(rs.getString("customer_username")).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load customer list.");
        }
    }

    private void viewOrders() {
        try (Connection conn = database.connection()) {
            String sql = "SELECT o.order_id, c.customer_name, o.order_date_time FROM online_store.Orders o JOIN online_store.Customers c ON o.customer_id = c.customer_id ORDER BY c.customer_name ASC";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            StringBuilder sb = new StringBuilder("Orders:\n");
            while (rs.next()) {
                sb.append("Order ID: ").append(rs.getInt("order_id"))
                  .append(", Customer: ").append(rs.getString("customer_name"))
                  .append(", Date: ").append(rs.getTimestamp("order_date_time")).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load orders.");
        }
    }

    private void createCoupon() {
        JTextField codeIdField = new JTextField();
        JTextField discountField = new JTextField();
        JTextField employeeIdField = new JTextField();

        Object[] fields = {
            "Coupon Code (ID):", codeIdField,
            "Discount Percent (0–100):", discountField,
            "Created By (Employee ID):", employeeIdField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Create Coupon Code", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try (Connection conn = database.connection()) {
                String sql = "INSERT INTO online_store.Coupons (code_id, discount_percent, created_by) VALUES (?, ?, ?)";
                PreparedStatement stm = conn.prepareStatement(sql);
                stm.setInt(1, Integer.parseInt(codeIdField.getText()));
                stm.setDouble(2, Double.parseDouble(discountField.getText()));
                stm.setInt(3, Integer.parseInt(employeeIdField.getText()));
                stm.executeUpdate();
                JOptionPane.showMessageDialog(this, "Coupon created!");
            } catch (SQLException | NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error creating coupon.");
            }
        }
    }

    public static void main(String[] args) {
        new EmployeeGUI();
    }
}
