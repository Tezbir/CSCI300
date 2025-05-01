import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to Some Company! Are you signing in as a customer, an employee, or an admin? (C/E/A)");
        String input = scan.nextLine().toUpperCase();
        switch (input) { // Rather than using if-else statements, we can use a switch statement for better readability.
            case "C":
                new customerLogin(); // Open the customer login window
                break;
            case "E":
                //new EmployeeLogin(); // Change to how you made the employee login window.
                break;
            case "A":
                //new AdminGUI(); // Change this to how you made the admin login window.
                break;
            default:
                System.out.println("Invalid input. Please enter C, E, or A.");
        }
    }
}
