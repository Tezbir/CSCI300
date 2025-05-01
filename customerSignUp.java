// Make sign-up for customers VIA JFrame and implement the information collected into the database.
import java.sql.*; 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class customerSignUp extends JFrame {
    private JTextField nameField, usernameField, addressField;
    private JPasswordField passwordField; // Found online, allows for password input without showing the password characters in the database for security reasons.
    // private JTextField passwordField; // This is the original line, but it shows the password characters in the database.
    private JButton submitButton, cancelButton;
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
    public customerSignUp() {
        setTitle("Sign Up");
        setSize(400,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));
        setLocationRelativeTo(null); // Center the frame on the screen
        nameField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        addressField = new JTextField();
        submitButton = new JButton("Sign Up");
        cancelButton = new JButton("Cancel");
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel("Address:"));
        add(addressField);
        add(submitButton);
        add(cancelButton);
        submitButton.addActionListener (e -> signUp());
        cancelButton.addActionListener (e -> cancelSignUp());
        setVisible(true);
    }
    public void signUp() {
        String name = nameField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String address = addressField.getText();
        try (Connection conn = connection()) {
            String sql = "INSERT INTO customers (customer_name, customer_username, customer_password, customer_address) VALUES (?, ?, ?, ?)"; // SQL query to insert customer data into the database. customer_id should be auto-incremented in the database.
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, name);
            stm.setString(2, username);
            stm.setString(3, password);
            stm.setString(4, address);
            stm.executeUpdate();
            JOptionPane.showMessageDialog(this, "Account created successfully!");
            this.dispose(); // Close current JFrame after successful sign-up.
            new customerSignUp(); // Creates a new instance of the customerSignUp class to allow for another sign-up.
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void cancelSignUp() {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel?", "Cancel Sign Up", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            this.dispose(); // Close the JFrame
        }
    }
}