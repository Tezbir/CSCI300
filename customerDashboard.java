import javax.swing.*;
import java.awt.*;
public class customerDashboard extends JFrame{
    private int customer_id;
    public customerDashboard(int customerId) {
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
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JButton updateInfoButton = new JButton("Update Info");
        JButton logoutButton = new JButton("Logout?");
        JButton browseItemsButton = new JButton("Browse Items");
        JButton viewOrdersButton = new JButton("View Orders");
        updateInfoButton.addActionListener(e -> customerUpdateInfo(customer_id));
        logoutButton.addActionListener(e -> logout());
        browseItemsButton.addActionListener(e -> browseItems());
        viewOrdersButton.addActionListener(e -> viewOrders());
        buttonPanel.add(updateInfoButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(browseItemsButton);
        buttonPanel.add(viewOrdersButton);
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
}
