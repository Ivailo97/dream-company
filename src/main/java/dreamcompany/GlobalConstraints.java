package dreamcompany;

import java.math.BigDecimal;

public final class GlobalConstraints {

    public static final BigDecimal INTERN_SALARY = new BigDecimal("800");
    public static final BigDecimal SENIOR_SALARY = new BigDecimal("3000");
    public static final BigDecimal JUNIOR_SALARY = new BigDecimal("1500");
    public static final BigDecimal TEAM_LEADER_SALARY = new BigDecimal("4000");
    public static final BigDecimal PROJECT_MANAGER_SALARY = new BigDecimal("6000");

    public static final Integer MAX_CREDITS = 100;
    public static final Integer STARTING_CREDITS = 0;


    // error handler
    public static final String GLOBAL_EXCEPTION_VIEW_NAME = "/error/error";
    public static final String ERROR_PAGE_STATUS_CODE_ATTRIBUTE_NAME = "statusCode";
    public static final String ERROR_PAGE_MESSAGE_ATTRIBUTE_NAME = "message";


    //exception messages
    public static final String DUPLICATE_PROJECT_MESSAGE = "Project with that name already exist";
    public static final String DUPLICATE_USER_USERNAME_MESSAGE = "User with the same username already exist";
    public static final String DUPLICATE_USER_EMAIL_MESSAGE = "User with the same email already exist";
    public static final String WRONG_OLD_PASSWORD_MESSAGE = "Wrong old password";
    public static final String DUPLICATE_TASK_MESSAGE = "Task with that name already exist";
    public static final String DUPLICATE_TEAM_MESSAGE = "Team with that name already exist";

    //LocalDateTime format pattern
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    //Log messages
    public static final String REGISTERED_SUCCESSFULLY = "Registered successfully %s with generated id:%s";
    public static final String UPDATED_PASSWORD = "Updated password";
    public static final String UPDATED_IMAGE = "Updated image";
    public static final String UPDATED_EMAIL = "Updated email";
    public static final String UPDATED_FIRST_NAME = "Updated first name";
    public static final String UPDATED_LAST_NAME = "Updated last name";
    public static final String UPDATED_SUCCESSFULLY = "Updated successfully profile:";

}
