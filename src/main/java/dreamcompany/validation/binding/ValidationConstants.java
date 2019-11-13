package dreamcompany.validation.binding;

import java.math.BigDecimal;

public final class ValidationConstants {

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
    public final static String USERNAME_LENGTH = "Username must be between 3 and 10 characters long!";
    public final static String FIRST_NAME_LENGTH = "First name must be between 3 and 10 characters long!";
    public final static String LAST_NAME_LENGTH = "Last name must be between 5 and 11 characters long!";
    public final static String PASSWORDS_DO_NOT_MATCH = "Passwords don't match!";
    public final static String EMAIL_ALREADY_EXISTS = "Email %s already exists";
    public final static String EMAIL_IS_NOT_VALID = "Email is not valid";
    public final static String USERNAME_IS_MANDATORY = "Enter a username!";
    public final static String OLD_PASSWORD_IS_MANDATORY = "Enter old password!";
    public final static String PASSWORD_IS_MANDATORY = "Enter a password!";
    public final static String CONFIRM_PASSWORD_IS_MANDATORY = "Confirm your password!";
    public final static String FIRST_NAME_IS_MANDATORY = "Enter first name!";
    public final static String LAST_NAME_IS_MANDATORY = "Enter last name!";
    public final static String WRONG_OLD_PASSWORD = "Wrong old password!";

    //user fields constraints
    public final static String EMAIL_PATTERN_STRING = "^((\"[\\w-\\s]+\")|([\\w-]+(?:\\.[\\w-]+)*)|(\"[\\w-\\s]+\")([\\w-]+(?:\\.[\\w-]+)*))(@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$)|(@\\[?((25[0-5]\\.|2[0-4][0-9]\\.|1[0-9]{2}\\.|[0-9]{1,2}\\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\]?$)";
    public static final int USERNAME_MIN_LENGTH = 3;
    public static final int USERNAME_MAX_LENGTH = 10;
    public static final int FIRST_NAME_MIN_LENGTH = 3;
    public static final int FIRST_NAME_MAX_LENGTH = 10;
    public static final int LAST_NAME_MIN_LENGTH = 5;
    public static final int LAST_NAME_MAX_LENGTH = 11;

    //team field names
    public static final String NAME_FIELD = "name";
    public static final String OFFICE_FIELD = "office";
    public static final String EMPLOYEES_FIELD = "employees";
    public static final String LOGO_FIELD = "logo";

    //team invalid input error messages
    public final static String NAME_IS_MANDATORY = "Enter a name!";
    public final static String OFFICE_IS_MANDATORY = "Select office!";
    public final static String EMPLOYEES_COUNT_INVALID = "Select at least two employees!";
    public final static String LOGO_IS_MANDATORY = "Upload a logo!";
    public final static String NAME_ALREADY_EXIST = "Team with that name already exist!";

    //team fields constraints
    public static final int EMPLOYEES_MIN_COUNT = 2;

    //office field names
    public static final String ADDRESS_FIELD = "address";
    public static final String PHONE_NUMBER_FIELD = "phoneNumber";
    public static final String TOWN_FIELD = "town";
    public static final String COUNTRY_FIELD = "country";

    //office invalid input error messages
    public final static String ADDRESS_IS_MANDATORY = "Enter address!";
    public final static String ADDRESS_LENGTH_INVALID = "Length must be between 5 and 50 symbols!";
    public final static String ADDRESS_ALREADY_EXIST = "Office with same address already exist!";
    public final static String PHONE_NUMBER_INVALID = "Length must be between 7 and 10 symbols and digits only!";
    public final static String TOWN_IS_INVALID = "Town must start with capital letter and contain letters only!!";
    public final static String COUNTRY_IS_INVALID = "Country must start with capital letter and contain letters only!";

    //office fields constraints
    public static final int ADDRESS_MIN_LENGTH = 5;
    public static final int ADDRESS_MAX_LENGTH = 50;
    public final static String PHONE_NUMBER_PATTERN_STRING = "^[0-9]{7,10}$";
    public final static String TOWN_COUNTRY_PATTERN_STRING = "^[A-Z][a-z]+$";

    //project field names
    public static final String DESCRIPTION_FIELD = "description";
    public static final String REWARD_FIELD = "reward";

    //project invalid input error messages
    public static final String NAME_IS_INVALID = "Name must start with capital letter and must be between 4 and 20 symbols long!";
    public static final String PROJECT_ALREADY_EXIST = "Project with same name already exists!";
    public static final String DESCRIPTION_IS_INVALID = "Description must be between 10 and 300 symbols long!";
    public static final String REWARD_IS_NEGATIVE = "Reward must be a positive number!";
    public static final String REWARD_IS_MANDATORY = "Enter reward!";

    //project fields constraints
    public static final BigDecimal REWARD_MIN_VALUE = new BigDecimal("0.01");
    public final static String NAME_PATTERN_STRING = "^[A-Z][a-z]{3,19}$";
    public final static String DESCRIPTION_PATTERN_STRING = "^[A-Z][\\w\\s]{9,299}$";

    //task field names
    public static final String REQUIRED_POSITION_FIELD = "requiredPosition";
    public static final String CREDITS_FIELD = "credits";
    public static final String PROJECT_FIELD = "project";
    public static final String MINUTES_FIELD = "minutesNeeded";

    //task invalid input error messages
    public static final String POSITION_IS_MANDATORY = "Select required position!";
    public static final String TASK_ALREADY_EXIST_IN_THIS_PROJECT = "Task with same name already exist in the project!";
    public static final String PROJECT_IS_MANDATORY = "Select project!";
    public static final String CREDITS_ARE_MANDATORY = "Enter credits!";
    public static final String CREDITS_COUNT_INVALID = "Credits must be between 2 and 20!";
    public static final String MINUTES_COUNT_INVALID = "Minutes must be between 1 and 60!";

    //task fields constraints
    public static final int MIN_CREDITS = 2;
    public static final int MAX_CREDITS = 20;
    public static final int MIN_MINUTES = 1;
    public static final int MAX_MINUTES = 60;
}
