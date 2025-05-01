import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class AdminGUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Admin Panel");
        frame.setSize(400, 300); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        JButton addBtn = new JButton("Add Employee");
        addBtn.setBounds(100, 30, 200, 30);
        frame.add(addBtn);

        JButton updateBtn = new JButton("Update Employee");
        updateBtn.setBounds(100, 70, 200, 30);
        frame.add(updateBtn);

        JButton removeBtn = new JButton("Remove Employee");
        removeBtn.setBounds(100, 110, 200, 30);
        frame.add(removeBtn);

        JButton orderBtn = new JButton("Edit Order");
        orderBtn.setBounds(100, 150, 200, 30);
        frame.add(orderBtn);

        JButton deleteCustomerBtn = new JButton("Delete Customer");
        deleteCustomerBtn.setBounds(100, 190, 200, 30);
        frame.add(deleteCustomerBtn);

        JButton exitBtn = new JButton("Exit");
        exitBtn.setBounds(100, 230, 200, 30);
        frame.add(exitBtn);

        addBtn.addActionListener(e -> addEmployee());
        updateBtn.addActionListener(e -> updateEmployee());
        removeBtn.addActionListener(e -> deleteEmployee());
        orderBtn.addActionListener(e -> changeOrder());
        deleteCustomerBtn.addActionListener(e -> deleteCustomer());
        exitBtn.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    public static Connection connection() {
        try {
            String url = "jdbc:mysql://localhost:3306/online_store";
            String user = "root";
            String password = "database28";
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection error: " + e.getMessage());
            return null;
        }
    }

    public static void addEmployee() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter new employee's ID:"));
            String name = JOptionPane.showInputDialog("Enter new employee's name:");
            String username = JOptionPane.showInputDialog("Enter new employee's username:");
            String password = JOptionPane.showInputDialog("Enter new employee's password:");

            Connection conn = connection();
            if (conn != null) {
                String sql = "INSERT INTO employees (employee_id, employee_name, employee_username, employee_password, first_login) VALUES (?, ?, ?, ?, true)";
                PreparedStatement stm = conn.prepareStatement(sql);
                stm.setInt(1, id);
                stm.setString(2, name);
                stm.setString(3, username);
                stm.setString(4, password);
                stm.executeUpdate();
                JOptionPane.showMessageDialog(null, "Employee created successfully.");
                conn.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public static void updateEmployee() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter employee ID to update:"));
            String name = JOptionPane.showInputDialog("Enter full name:");
            String username = JOptionPane.showInputDialog("Enter new username:");
            String password = JOptionPane.showInputDialog("Enter new password:");

            Connection conn = connection();
            if (conn != null) {
                String sql = "UPDATE employees SET employee_name = ?, employee_username = ?, employee_password = ? WHERE employee_id = ?";
                PreparedStatement stm = conn.prepareStatement(sql);
                stm.setString(1, name);
                stm.setString(2, username);
                stm.setString(3, password);
                stm.setInt(4, id);

                int rows = stm.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "Employee updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Employee ID not found.");
                }
                conn.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public static void deleteEmployee() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter employee ID to delete:"));
            Connection conn = connection();
            if (conn != null) {
                String sql = "DELETE FROM employees WHERE employee_id = ?";
                PreparedStatement stm = conn.prepareStatement(sql);
                stm.setInt(1, id);
                int rows = stm.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "Employee deleted.");
                } else {
                    JOptionPane.showMessageDialog(null, "Employee ID not found.");
                }
                conn.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public static void changeOrder() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter order ID to update:"));
            int customerId = Integer.parseInt(JOptionPane.showInputDialog("Enter new customer ID:"));
            Connection conn = connection();
            if (conn != null) {
                String sql = "UPDATE Orders SET customer_id = ? WHERE order_id = ?";
                PreparedStatement stm = conn.prepareStatement(sql);
                stm.setInt(1, customerId);
                stm.setInt(2, id);
                int rows = stm.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "Order updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Order ID not found.");
                }
                conn.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public static void deleteCustomer() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter customer ID to delete:"));
            Connection conn = connection();
            if (conn != null) {
                String sql = "DELETE FROM Customers WHERE customer_id = ?";
                PreparedStatement stm = conn.prepareStatement(sql);
                stm.setInt(1, id);
                int rows = stm.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "Customer deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Customer ID not found.");
                }
                conn.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}

