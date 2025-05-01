import javax.swing.*;
import java.sql.*;
import java.awt.*;
import javax.swing.table.*;
public class browseItems {
    private int customer_id;
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton addToCartButton, viewCartButton, backButton;
    private JTextField searchField;
    private JButton searchButton;
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
    public browseItems(int customerId) {
        this.customer_id = customerId;
        frame = new JFrame("Browse Items");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"Item ID", "Item Name", "Price"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addToCartButton = new JButton("Add to Cart");
        viewCartButton = new JButton("View Cart");
        backButton = new JButton("Back to Dashboard");
        buttonPanel.add(addToCartButton);
        buttonPanel.add(viewCartButton);
        buttonPanel.add(backButton);

        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        JPanel searchPanel = new JPanel();
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(searchPanel, BorderLayout.NORTH);

        loadItems();

        addToCartButton.addActionListener(e -> addToCart());
        viewCartButton.addActionListener(e -> viewCart());
        backButton.addActionListener(e -> backToDashboard());
        searchButton.addActionListener(e -> searchItems());

        frame.setVisible(true);
    }
    public void loadItems() {
        try (Connection conn = connection()) {
            String sql = "SELECT * FROM items";
            PreparedStatement stm = conn.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            tableModel.setRowCount(0); // Clear existing rows
            while (rs.next()) {
                String itemName = rs.getString("item_name");
                double itemPrice = rs.getDouble("item_price");
                int itemQuantity = rs.getInt("item_quantity");
                tableModel.addRow(new Object[]{itemName, itemPrice, itemQuantity});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addToCart() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String itemName = (String) tableModel.getValueAt(selectedRow, 0);
            double itemPrice = (double) tableModel.getValueAt(selectedRow, 1);
            int itemQuantity = (int) tableModel.getValueAt(selectedRow, 2);
            try (Connection conn = connection()) {
                String sql = "INSERT INTO cart (customer_id, item_name, item_price, item_quantity) VALUES (?, ?, ?, ?)";
                PreparedStatement stm = conn.prepareStatement(sql);
                stm.setInt(1, customer_id);
                stm.setString(2, itemName);
                stm.setDouble(3, itemPrice);
                stm.setInt(4, itemQuantity);
                stm.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Item added to cart!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select an item to add to cart.");
        }
    }
}
