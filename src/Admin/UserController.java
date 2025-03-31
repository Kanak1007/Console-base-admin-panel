package Admin;

import Exceptions.UserNotFoundException;
import Validate.Validate;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class UserController {


        private static final UserService userService = new UserService();
        private static final Scanner scanner = new Scanner(System.in);

        public void run(){
            while (true) {
                System.out.println(getMenu());
                int choice = getUserChoice();
                processChoice(choice);
            }
        }
        private static String getMenu() {
            Supplier<String> menuSupplier = () -> "\n===== User Management Console App =====\n"
                    + "1. Create User\n"
                    + "2. View All Users\n"
                    + "3. View Single User\n"
                    + "4. Update User\n"
                    + "5. Delete User\n"
                    + "6. Delete All Users\n"
                    + "7. Exit\n"
                    + "Choose an option: ";
            return menuSupplier.get();
        }

        private static int getUserChoice() {
            while (true) {
                try {
                    int choice = Integer.parseInt(scanner.nextLine().trim());
                    return choice;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter a number.");
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
            String email = getValidInput("Enter Email: ", Validate::isValidEmail, "Invalid email format. Try again.");
            List<String> numbers = getValidMobileNumbers();

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
                System.out.println(userService.getSingleuser(email));
            }
        }

        private static void updateUser(boolean isEmpty) throws UserNotFoundException {
            if (isEmpty) {
                System.out.println("The list is empty. There is nothing to update.");
            } else {
                String email = getValidInput("Enter Email: ", Validate::isValidEmail, "Invalid email format. Try again.");
                String newName = getValidInput("Enter New Name: ", Validate::isValidName, "Invalid name. Try again.");
                List<String> newNumbers = getValidMobileNumbers();

                System.out.println(userService.updateUser(email, newName, newNumbers));
            }
        }

        private static void deleteUser(boolean isEmpty) throws UserNotFoundException {
            if (isEmpty) {
                System.out.println("The list is already empty. No users to delete.");
            } else {
                String email = getValidInput("Enter Email: ", Validate::isValidEmail, "Invalid email format. Try again.");
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
                }
            } while (!validator.test(input));
            return input;
        }

        private static List<String> getValidMobileNumbers() {
            List<String> numbers;
            do {
                System.out.print("Enter Mobile Numbers (comma separated): ");
                numbers = Arrays.asList(scanner.nextLine().split(","));
                if (!Validate.isValidMobileList(numbers)) {
                    System.out.println("Invalid mobile number format. Try again.");
                }
            } while (!Validate.isValidMobileList(numbers));
            return numbers;
        }


}
