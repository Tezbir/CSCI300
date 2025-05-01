import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class AdminSignInPage {

    public AdminSignInPage() {
        JFrame frame = new JFrame("Admin Login");
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(30, 30, 80, 25);
        frame.add(userLabel);

        JTextField userText = new JTextField();
        userText.setBounds(120, 30, 160, 25);
        frame.add(userText);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(30, 70, 80, 25);
        frame.add(passLabel);

        JPasswordField passText = new JPasswordField();
        passText.setBounds(120, 70, 160, 25);
        frame.add(passText);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(120, 110, 80, 25);
        frame.add(loginBtn);

        loginBtn.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passText.getPassword());

            if (validateLogin(username, password)) {
                frame.dispose(); 
                AdminGUI.main(null); 
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    // Validate credentials against the database
    private static boolean validateLogin(String username, String password) {
        String sql = "SELECT * FROM employees WHERE employee_username = ? AND employee_password = ?";
        if (username.equals("superadmin") && password.equals("superpassword")) {
            return true;
        }
        try (Connection conn = Admin.connection(); 
             PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setString(1, username);
            stm.setString(2, password);

            ResultSet rs = stm.executeQuery();
            return rs.next(); // true if user found
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
