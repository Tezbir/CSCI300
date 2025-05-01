import java.sql.*;
import java.util.Scanner;

public class Admin {

    public static Connection connection() {
        try {
            String url = "jdbc:mysql://localhost:3306/online_store";
            String user = "root";
            String password = "database28";

            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addEmployee() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter new employee's ID:");
        int id = scanner.nextInt();
        scanner.nextLine(); 

        System.out.println("Enter new employee's name:");
        String name = scanner.nextLine();

        System.out.println("Enter new employee's username:");
        String username = scanner.nextLine();

        System.out.println("Enter new employee's password:");
        String password = scanner.nextLine();

        Connection conn = connection();
        if (conn != null) {
            try {
                String sql = "INSERT INTO online_store.employees (employee_id, employee_name, employee_username, employee_password, first_login) VALUES (?, ?, ?, ?, true)";
                PreparedStatement stm = conn.prepareStatement(sql);
                stm.setInt(1, id);
                stm.setString(2, name);
                stm.setString(3, username);
                stm.setString(4, password);

                stm.executeUpdate();
                System.out.println("Employee created successfully.");
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateEmployee() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter employee ID to update:");
        int id = scanner.nextInt();
        scanner.nextLine(); 

        System.out.println("Enter full name:");
        String name = scanner.nextLine();

        System.out.println("Enter new username:");
        String username = scanner.nextLine();

        System.out.println("Enter new password:");
        String password = scanner.nextLine();

        Connection conn = connection();
        if (conn != null) {
            try {
                String sql = "UPDATE online_store.employees SET employee_name = ?, employee_username = ?, employee_password = ? WHERE employee_id = ?";
                PreparedStatement stm = conn.prepareStatement(sql);
                stm.setString(1, name);
                stm.setString(2, username);
                stm.setString(3, password);
                stm.setInt(4, id);

                int rows = stm.executeUpdate();
                if (rows > 0) {
                    System.out.println("Employee updated successfully.");
                } else {
                    System.out.println("Employee ID not found.");
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteEmployee() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter employee ID to delete:");
        int id = scanner.nextInt();

        Connection conn = connection();
        if (conn != null) {
            try {
                String sql = "DELETE FROM online_store.employees WHERE employee_id = ?";
                PreparedStatement stm = conn.prepareStatement(sql);
                stm.setInt(1, id);

                int rows = stm.executeUpdate();
                if (rows > 0) {
                    System.out.println("Employee deleted.");
                } else {
                    System.out.println("Employee ID not found.");
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void changeOrder() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter order ID to update:");
        int id = scanner.nextInt();

        System.out.println("Enter new customer ID:");
        int customerId = scanner.nextInt();

        Connection conn = connection();
        if (conn != null) {
            try {
                String sql = "UPDATE online_store.Orders SET customer_id = ? WHERE order_id = ?";
                PreparedStatement stm = conn.prepareStatement(sql);
                stm.setInt(1, customerId);
                stm.setInt(2, id);

                int rows = stm.executeUpdate();
                if (rows > 0) {
                    System.out.println("Order updated successfully.");
                } else {
                    System.out.println("Order ID not found.");
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteCustomer() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter customer ID to delete:");
        int id = scanner.nextInt();

        Connection conn = connection();
        if (conn != null) {
            try {
                String sql = "DELETE FROM online_store.Customers WHERE customer_id = ?";
                PreparedStatement stm = conn.prepareStatement(sql);
                stm.setInt(1, id);

                int rows = stm.executeUpdate();
                if (rows > 0) {
                    System.out.println("Customer deleted successfully.");
                } else {
                    System.out.println("Customer ID not found.");
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 0) {
            System.out.println("1. Add New Employee");
            System.out.println("2. Update Employee Info");
            System.out.println("3. Delete Employee");
            System.out.println("4. Edit Order");
            System.out.println("5. Delete Customer");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    updateEmployee();
                    break;
                case 3:
                    deleteEmployee();
                    break;
                case 4:
                    changeOrder();
                    break;
                case 5:
                    deleteCustomer(); 
                    break;
                case 0:
                    System.out.println("Exiting Admin Panel. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}

        
    
    

