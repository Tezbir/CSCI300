import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
public class customerLogin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField; // Found online, allows for password input without showing the password characters in the database for security reasons.
    private JButton loginButton, signUpButton;
    public customerLogin() {
        setTitle("Customer Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));
        setLocationRelativeTo(null); // Center the frame on the screen
        usernameField = new JTextField();
        passwordField = new JPasswordField(); // This is the original line, but it shows the password characters in the database.
        loginButton = new JButton("Login");
        signUpButton = new JButton("Sign Up");

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(loginButton);
        add(signUpButton);

        loginButton.addActionListener(e -> login());
        signUpButton.addActionListener(e -> {
            this.dispose(); // Close the login window
            new customerSignUp(); // Open the sign-up window
        });
        setVisible(true);   
    }
    public void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        try (Connection conn = database.connection()) {
            String sql = "SELECT * FROM customers WHERE customer_username = ? AND customer_password = ?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, password);
            ResultSet rs = stm.executeQuery();
           if (rs.next()) {
            int customerId = rs.getInt("customer_id");
            JOptionPane.showMessageDialog(this, "Login successful! Welcome back " + username + "!");
            this.dispose(); // Close the login window
           } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.");
            usernameField.setText(""); // Clear the username field
            passwordField.setText(""); // Clear the password field
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }
}
}
