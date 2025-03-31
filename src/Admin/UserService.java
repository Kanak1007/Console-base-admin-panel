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

        return users.stream().filter(User -> User.getEmail().equalsIgnoreCase(email)).findFirst()
                .orElseThrow(() -> new UserNotFoundException("User not found with email " + email));


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

    // update a user
    public String updateUser(String email, String newName, List<String> newNumbers)
            throws UserNotFoundException {

        User user = getSingleuser(email);
        user.setName(newName);
        user.setMobile(newNumbers);

        return "User updated successfully.";
    }

    public boolean CheckList() {
        if (users.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

};
