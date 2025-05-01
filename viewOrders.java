import java.sql.*;

public class viewOrders {
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
    public viewOrders(int customerId) {
        try (Connection conn = connection()) {
            String sql = "SELECT * FROM orders WHERE customer_id = ?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, customerId);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String customerAddress = rs.getString("customer_address");
                System.out.println("Order ID: " + orderId + ", Customer Address: " + customerAddress);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
}
}
