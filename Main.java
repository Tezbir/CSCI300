import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to Some Company! Are you signing in as a customer, an employee, or an admin? (C/E/A or type Q to quit)");
        String input = scan.nextLine().toUpperCase();
        switch (input) { // Rather than using if-else statements, we can use a switch statement for better readability.
            case "C":
                new customerLogin(); // Open the customer login window
                break;
            case "E":
                //new EmployeeLogin(); // Change to how you made the employee login window.
                break;
            case "A":
                new AdminSignInPage(); // Change this to how you made the admin login window.
                break;
            case "Q":
                System.out.println("Exiting the program. Goodbye!");
                break;
            default:
                System.out.println("Invalid input. Please enter C, E, A or Q.");
        }
        scan.close(); // Close the scanner to prevent resource leaks.
    }
}
