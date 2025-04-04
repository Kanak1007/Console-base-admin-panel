package Admin;

import Exceptions.UserNotFoundException;
import User.*;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final List<User> users = new ArrayList<>();

    // creating a user
    public String createUser(String name, String email, List<String> mobileNumbers) {

        if (users.stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email))) {
            return "User with this email already exists!";
        }

        users.add(new User(name, email, mobileNumbers));
        return "User Created Successfully!";
    }

    // fetching all the users
    public List<User> getAllUsers() {
        if (users.isEmpty()) {
            System.out.println("List is empty. Put some data");
        }
        return users;
    }

    // fetching a single user
    public User getSingleuser(String email) throws UserNotFoundException {

        return users.stream().filter(User -> User.getEmail().equalsIgnoreCase(email)).findFirst().orElseThrow(() -> new UserNotFoundException("User not found with email " + email));


    }

    //delete a single user
    public String Delete_singleuser(String email) throws UserNotFoundException {
        User user = getSingleuser(email);
        users.remove(user);
        return "User deleted successfully.";
    }

    // delete all the users
    public String deleteAll() {
        if (users.isEmpty()) {
            return "List is already empty.";
        }
        users.clear();
        return "All users deleted successfully.";
    }

    // Update User
    public boolean updateUser(String currentEmail, String newName, List<String> newNumbers, String newEmail) {
        User user = users.stream().filter(u -> u.getEmail().equalsIgnoreCase(currentEmail)).findFirst().orElse(null);

        if (user == null) {
            return false; // User not found
        }

        if (newName != null) user.setName(newName);
        if (newNumbers != null) user.setMobile(newNumbers);
        if (newEmail != null) user.setEmail(newEmail);

        return true;
    }

    // check if the list is empty
    public boolean CheckList() {
        return users.isEmpty();
    }

}
