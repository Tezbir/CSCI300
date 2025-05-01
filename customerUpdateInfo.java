import javax.swing.*;
import java.awt.*;
import java.sql.*;
public class customerUpdateInfo extends JFrame {
    private int customer_id;
    private JTextField nameField, addressField;
    private JButton updateButton, cancelButton;
    public static Connection connection() {
        try {
            String url = "jdbc:mysql://localhost:3306/employee";
            String user = "root";
            String password = "database28";

            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public customerUpdateInfo(int customID) {
        this.customer_id = customID;
        setTitle("Update Info:");
        setSize(400, 300);
        setLayout(new GridLayout(4, 2));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        nameField = new JTextField();
        addressField = new JTextField();
        updateButton = new JButton("Update");
        cancelButton = new JButton("Cancel");
        
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Address:"));
        add(addressField);
        add(updateButton);
        add(cancelButton);
        updateButton.addActionListener(e -> updateInfo());
        cancelButton.addActionListener(e -> cancelUpdate());
        loadCurrentInfo();
        setVisible(true);
    }
    private void loadCurrentInfo() { // Load current customer info from the database to update
        try (Connection loadInfo = connection()) {
            String sql = "SELECT customer_name, customer_address FROM customers WHERE customer_id = ?";
            PreparedStatement stm = loadInfo.prepareStatement(sql);
            stm.setInt(1, customer_id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                nameField.setText(rs.getString("customer_name"));
                addressField.setText(rs.getString("customer_address"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void updateInfo() { // Update customer info in the database
        String name = nameField.getText();
        String address = addressField.getText();
        try (Connection conn = connection()) {
            String sql = "UPDATE customers SET customer_name = ?, customer_address = ? WHERE customer_id = ?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, name);
            stm.setString(2, address);
            stm.setInt(3, customer_id);
            stm.executeUpdate();
            JOptionPane.showMessageDialog(this, "Info updated successfully.");
            dispose(); // Close the window after updating
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void cancelUpdate() { // Close the update window without saving changes
         int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel?", "Cancel Updating Information", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            this.dispose(); // Close the JFrame
        }
    }
}