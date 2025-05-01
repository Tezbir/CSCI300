import java.sql.*;
public class database {
    public static Connection connection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Force the JDBC driver to register
    
            String url = "jdbc:mysql://localhost:3306/online_store?serverTimezone=EST";
            String user = "root";
            String password = "database28";
    
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}