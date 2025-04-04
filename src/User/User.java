package User;


import java.util.List;

import Validate.*;


public class User {
    private String name;
    private String email;
    private List<String> mobile;


    // Constructor
    public User(String name, String email, List<String> mobile) {
        setMobile(mobile);
        setEmail(email);
        setName(name);
    }


    // Getter methods
    public String getEmail() {
        return email;
    }


    // Setter methods
    public void setMobile(List<String> mobile) {
        if (Validate.isValidMobileList(mobile)) {
            throw new IllegalArgumentException("Invalid mobile number(s): " + mobile);
        }
        this.mobile = mobile;
    }

    public void setName(String name) {
        if (!Validate.isValidName(name)) {
            throw new IllegalArgumentException("Invalid name: " + name);
        }
        this.name = name;
    }

    public void setEmail(String email) {
        if (!Validate.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid name: " + email);
        }
        this.email = email;
    }

    // ToString method for displaying user details
    @Override
    public String toString() {
        return "User deatils:- {" + "name='" + name + '\'' + ", email='" + email + '\'' + ", mobile=" + mobile + '}' + '\n';
    }
}
