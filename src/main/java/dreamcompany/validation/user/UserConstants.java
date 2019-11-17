package dreamcompany.validation.user;

public final class UserConstants {

    //user field names
    public static final String USERNAME_FIELD = "username";
    public static final String FIRST_NAME_FIELD = "firstName";
    public static final String LAST_NAME_FIELD = "lastName";
    public static final String PASSWORD_FIELD = "password";
    public static final String OLD_PASSWORD_FIELD = "oldPassword";
    public static final String CONFIRM_PASSWORD_FIELD = "confirmPassword";
    public static final String EMAIL_FIELD = "email";

    //user invalid input error messages
    public final static String USERNAME_ALREADY_EXISTS = "Username %s already exists!";
    public final static String PASSWORDS_DO_NOT_MATCH = "Passwords don't match!";
    public final static String EMAIL_ALREADY_EXISTS = "Email %s already exists";
    public final static String EMAIL_IS_NOT_VALID = "Email is not valid!";
    public final static String USERNAME_IS_INVALID = "Username must start with a letter and must be between 3 and 10 characters long!";
    public final static String OLD_PASSWORD_IS_MANDATORY = "Enter old password!";
    public final static String PASSWORD_IS_MANDATORY = "Enter a password!";
    public final static String CONFIRM_PASSWORD_IS_MANDATORY = "Confirm your password!";
    public final static String FIRST_NAME_IS_INVALID = "First name must start with a letter and must be between 3 and 10 characters long!";
    public final static String LAST_NAME_IS_INVALID = "Last name must start with a letter and must be between 5 and 11 characters long!";
    public final static String WRONG_OLD_PASSWORD = "Wrong old password!";

    //user fields constraints
    public final static String EMAIL_PATTERN_STRING = "^((\"[\\w-\\s]+\")|([\\w-]+(?:\\.[\\w-]+)*)|(\"[\\w-\\s]+\")([\\w-]+(?:\\.[\\w-]+)*))(@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$)|(@\\[?((25[0-5]\\.|2[0-4][0-9]\\.|1[0-9]{2}\\.|[0-9]{1,2}\\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\]?$)";
    public static final String USERNAME_PATTERN_STRING = "^[\\p{L}][\\p{L}0-9]{2,9}$";
    public static final String NAME_PATTERN_STRING = "^[\\p{Lu}]\\p{L}{3,10}$";
}
