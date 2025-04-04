package Admin;

import Exceptions.UserNotFoundException;
import User.User;
import Validate.Validate;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class UserController {

    private static final UserService userService = new UserService();
    private static final Scanner scanner = new Scanner(System.in);

    public void run() {
        while (true) {
            System.out.println(getMenu());
            int choice = getUserChoice();
            processChoice(choice);
        }
    }

    private static String getMenu() {
        Supplier<String> menuSupplier = () -> """
                
                ===== User Management Console App =====
                1. Create User
                2. View All Users
                3. View Single User
                4. Update User
                5. Delete User
                6. Delete All Users
                7. Exit
                Choose an option:\s""";
        return menuSupplier.get();
    }

    private static final Supplier<String> updateMenuSupplier = () -> """
            
            Select what you want to update:
            1. Update Name
            2. Update Mobile Numbers
            3. Update Email
            4. Update Everything
            0. Cancel Update
            """;


    private static int getUserChoice() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

    private static int getUserChoice2(int min, int max) {
        while (true) {
            try {
                System.out.print("Enter your choice: ");
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.println("Invalid choice! Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }

    private static void processChoice(int choice) {
        boolean isEmpty = userService.CheckList();

        try {
            switch (choice) {
                case 1 -> createUser(isEmpty);
                case 2 -> viewAllUsers(isEmpty);
                case 3 -> viewSingleUser(isEmpty);
                case 4 -> updateUser(isEmpty);
                case 5 -> deleteUser(isEmpty);
                case 6 -> deleteAllUsers();
                case 7 -> exitApp();
                default -> System.out.println("Invalid choice! Try again.");
            }
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void createUser(boolean isEmpty) {
        if (isEmpty) {
            System.out.println("Yahoo! You are the first user to create an account. Please enter details:");
        }

        String name = getValidInput("Enter Name: ", Validate::isValidName, "Invalid Name. Please try again.");
        if (name == null) return;

        String email = getValidInput("Enter Email: ", Validate::isValidEmail, "Invalid email format. Try again.");
        if (email == null) return;

        List<String> numbers = getValidMobileNumbers();
        if (numbers == null) return;

        System.out.println(userService.createUser(name, email, numbers));
    }

    private static void viewAllUsers(boolean isEmpty) {
        if (isEmpty) {
            System.out.println("The list is empty. Add some users.");
        } else {
            userService.getAllUsers().forEach(System.out::println);
        }
    }

    private static void viewSingleUser(boolean isEmpty) throws UserNotFoundException {
        if (isEmpty) {
            System.out.println("The list is empty. Add some users.");
        } else {
            String email = getValidInput("Enter Email: ", Validate::isValidEmail, "Invalid email format. Try again.");
            if (email == null) return;
            System.out.println(userService.getSingleuser(email));
        }
    }

    private static void updateUser(boolean isEmpty) {
        if (isEmpty) {
            System.out.println("The list is empty. There is nothing to update.");
            return;
        }

        String currentEmail = getValidInput("Enter Email: ", Validate::isValidEmail, "Invalid email format. Try again.");
        if (currentEmail == null) return;

        User user;
        try {
            user = userService.getSingleuser(currentEmail);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println(updateMenuSupplier.get());

        int choice = getUserChoice2(0, 4);

        if (choice == 0) {
            System.out.println("Update canceled.");
            return;
        }

        String newName = null;
        List<String> newNumbers = null;
        String newEmail = null;

        switch (choice) {
            case 1 -> newName = getValidInput("Enter New Name: ", Validate::isValidName, "Invalid name. Try again.");
            case 2 -> newNumbers = getValidMobileNumbers();
            case 3 ->
                    newEmail = getValidInput("Enter New Email: ", Validate::isValidEmail, "Invalid email format. Try again.");
            case 4 -> {
                newName = getValidInput("Enter New Name: ", Validate::isValidName, "Invalid name. Try again.");
                newNumbers = getValidMobileNumbers();
                newEmail = getValidInput("Enter New Email: ", Validate::isValidEmail, "Invalid email format. Try again.");
            }
        }

        if ((choice == 1 && newName == null) || (choice == 2 && newNumbers == null) || (choice == 3 && newEmail == null) || (choice == 4 && (newName == null || newNumbers == null || newEmail == null))) {
            System.out.println("Update canceled due to invalid input.");
            return;
        }

        boolean updated = userService.updateUser(user.getEmail(), newName, newNumbers, newEmail);

        if (updated) {
            System.out.println("User details updated successfully!");
        } else {
            System.out.println("Update failed. Please try again.");
        }
    }

    private static void deleteUser(boolean isEmpty) throws UserNotFoundException {
        if (isEmpty) {
            System.out.println("The list is already empty. No users to delete.");
        } else {
            String email = getValidInput("Enter Email: ", Validate::isValidEmail, "Invalid email format. Try again.");
            if (email == null) return;
            System.out.println(userService.Delete_singleuser(email));
        }
    }

    private static void deleteAllUsers() {
        System.out.println(userService.deleteAll());
    }

    private static void exitApp() {
        System.out.println("Exiting...");
        scanner.close();
        System.exit(0);
    }

    private static String getValidInput(String prompt, Predicate<String> validator, String errorMsg) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine();
            if (!validator.test(input)) {
                System.out.println(errorMsg);
                if (getBackToMainMenu()) return null;
            }
        } while (!validator.test(input));
        return input;
    }

    private static List<String> getValidMobileNumbers() {
        List<String> numbers;
        do {
            System.out.print("Enter Mobile Numbers (comma separated): ");
            numbers = Arrays.asList(scanner.nextLine().split(","));
            if (Validate.isValidMobileList(numbers)) {
                System.out.println("Invalid mobile number format. Try again.");
                if (getBackToMainMenu()) return null;
            }
        } while (Validate.isValidMobileList(numbers));
        return numbers;
    }

    private static boolean getBackToMainMenu() {
        while (true) {
            System.out.println("Do you want to continue or go back to the main menu?");
            System.out.println("1. Press 1 to continue");
            System.out.println("2. Press 2 to return to Main Menu");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    return false;
                case "2":
                    return true;
                default:
                    System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }
    }
}
