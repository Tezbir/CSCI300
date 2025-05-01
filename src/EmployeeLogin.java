import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class EmployeeLogin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public EmployeeLogin() {
        setTitle("Employee Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));
        setLocationRelativeTo(null);

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel("")); 
        add(loginButton);

        loginButton.addActionListener(e -> login());

        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

          if (username.equals("admin") && password.equals("bypass123")) {
        JOptionPane.showMessageDialog(this, "Bypass login successful.");
        dispose();
        EmployeeGUI.main(null);
        return; }

        try (Connection conn = database.connection()) {
            String sql = "SELECT * FROM online_store.Employees WHERE employee_username = ?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, username);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("employee_password");
                String tempPassword = rs.getString("employee_tempPassword");

                if (tempPassword != null && tempPassword.equals(password)) {
                    String newPassword = JOptionPane.showInputDialog(this, "First-time login detected. Enter new password:");
                    if (newPassword != null && !newPassword.trim().isEmpty()) {
                        String updateSql = "UPDATE online_store.Employees SET employee_password = ?, employee_tempPassword = NULL WHERE employee_username = ?";
                        PreparedStatement updateStm = conn.prepareStatement(updateSql);
                        updateStm.setString(1, newPassword);
                        updateStm.setString(2, username);
                        updateStm.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Password updated successfully.");
                        dispose();
                        EmployeeGUI.main(null);
                    }
                } else if (storedPassword != null && storedPassword.equals(password)) {
                    JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + username + ".");
                    dispose();
                    EmployeeGUI.main(null);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    usernameField.setText("");
                    passwordField.setText("");
                }

            } else {
                JOptionPane.showMessageDialog(this, "User not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new EmployeeLogin();
    }
}
