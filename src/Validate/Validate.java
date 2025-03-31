package Validate;

import java.util.List;
import java.util.regex.Pattern;

public class Validate {
    // Email validation regex
    private static final Pattern patternEmail =
            Pattern.compile("^[A-Za-z0-9]+([._-][A-Za-z0-9]+)*@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    // Mobile number validation regex (Allows country code +10 to 15 digits)
    private static final Pattern patternMobile = Pattern.compile("^(\\+\\d{1,3})?[0-9]{10,15}$");

    // Name validation regex (Only alphabets and spaces, 2-50 characters)
    private static final Pattern patternName = Pattern.compile("^[A-Za-z ]{2,50}$");

    // Validate email
    public static boolean isValidEmail(String email) {
        return email != null && patternEmail.matcher(email).matches();
    }

    // Validate name
    public static boolean isValidName(String name) {
        return name != null && patternName.matcher(name).matches();
    }

    // Validate a single mobile number
    public static boolean isValidMobile(String mobile) {
        return mobile != null && patternMobile.matcher(mobile).matches();
    }

    // Validate list of mobile numbers
    public static boolean isValidMobileList(List<String> mobileList) {
        return mobileList != null && mobileList.stream().allMatch(Validate::isValidMobile);
    }
}
