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
            try {
                String name = itemName.getText().trim();
                double itemPrice = Double.parseDouble(price.getText().trim());
                int itemQuantity = Integer.parseInt(quantity.getText().trim());
                int empId = Integer.parseInt(creatorId.getText().trim());

                try (Connection conn = database.connection()) {
                    String sql = "INSERT INTO online_store.Items (item_name, item_price, quantity_in_stock, created_by) VALUES (?, ?, ?, ?)";
                    PreparedStatement stm = conn.prepareStatement(sql);
                    stm.setString(1, name);
                    stm.setDouble(2, itemPrice);
                    stm.setInt(3, itemQuantity);
                    stm.setInt(4, empId);
                    stm.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Item added successfully!");
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values for price, quantity, and employee ID.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error while adding item.");
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
            if (sb.length() == "Customers:\n".length()) {
                sb.append("No customers found.");
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
            if (sb.length() == "Orders:\n".length()) {
                sb.append("No orders found.");
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
            try {
                int codeId = Integer.parseInt(codeIdField.getText().trim());
                double discount = Double.parseDouble(discountField.getText().trim());
                int empId = Integer.parseInt(employeeIdField.getText().trim());

                if (discount < 0 || discount > 100) {
                    JOptionPane.showMessageDialog(this, "Discount must be between 0 and 100.");
                    return;
                }

                try (Connection conn = database.connection()) {
                    String sql = "INSERT INTO online_store.Coupons (code_id, discount_percent, created_by) VALUES (?, ?, ?)";
                    PreparedStatement stm = conn.prepareStatement(sql);
                    stm.setInt(1, codeId);
                    stm.setDouble(2, discount);
                    stm.setInt(3, empId);
                    stm.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Coupon created!");
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error creating coupon.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeGUI::new);
    }
}
