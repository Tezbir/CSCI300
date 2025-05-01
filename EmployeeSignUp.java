import java.sql.*; 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EmployeeSignUp extends JFrame {
    private JTextField nameField, usernameField;
    private JPasswordField passwordField;
    private JButton submitButton, cancelButton;

    public EmployeeSignUp() {
        setTitle("Employee Sign Up");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));
        setLocationRelativeTo(null);

        nameField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        submitButton = new JButton("Sign Up");
        cancelButton = new JButton("Cancel");

        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(submitButton);
        add(cancelButton);

        submitButton.addActionListener(e -> signUp());
        cancelButton.addActionListener(e -> cancelSignUp());

        setVisible(true);
    }

    public void signUp() {
        String name = nameField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = database.connection()) {
            String sql = "INSERT INTO online_store.Employees (employee_name, employee_username, employee_password) VALUES (?, ?, ?)";
            PreparedStatement stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, name);
            stm.setString(2, username);
            stm.setString(3, password);
            stm.executeUpdate();

            ResultSet rs = stm.getGeneratedKeys();
            if (rs.next()) {
                int employeeId = rs.getInt(1);
                JOptionPane.showMessageDialog(this, "Account created successfully!\nYour Employee ID is: " + employeeId);
            } else {
                JOptionPane.showMessageDialog(this, "Account created, but Employee ID could not be retrieved.");
            }

            this.dispose(); // Close after successful signup
            new EmployeeSignUp(); // Open a fresh form for next signup

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating account. Try again.");
        }
    }

    public void cancelSignUp() {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel?", "Cancel Sign Up", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }

    public static void main(String[] args) {
        new EmployeeSignUp();
    }
}
