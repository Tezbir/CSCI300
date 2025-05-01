
import java.sql.*;
public class database {
    public static Connection connection() {
        try {
            String url = "jdbc:mysql://localhost:3306/online_store?serverTimezone=EST";
            String user = "root";
            String password = "database28";

            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}