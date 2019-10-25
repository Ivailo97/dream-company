package dreamcompany;

public final class GlobalConstraints {

    public static final Integer MAX_CREDITS = 100;
    public static final Integer STARTING_CREDITS = 0;

    //roles
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_MODERATOR = "ROLE_MODERATOR";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_ROOT = "ROLE_ROOT";


    // error handler
    public static final String GLOBAL_EXCEPTION_VIEW_NAME = "/error/error";
    public static final String ERROR_PAGE_STATUS_CODE_ATTRIBUTE_NAME = "statusCode";
    public static final String ERROR_PAGE_MESSAGE_ATTRIBUTE_NAME = "message";


    //exception messages
    public static final String DUPLICATE_PROJECT_MESSAGE = "Project with that name already exist!";
    public static final String DUPLICATE_USER_USERNAME_MESSAGE = "User with the same username already exist!";
    public static final String DUPLICATE_USER_EMAIL_MESSAGE = "User with the same email already exist!";
    public static final String WRONG_OLD_PASSWORD_MESSAGE = "Wrong old password!";
    public static final String DUPLICATE_TASK_MESSAGE = "Task with that name already exist!";
    public static final String DUPLICATE_TEAM_MESSAGE = "Team with that name already exist!";
    public static final String USER_NOT_FOUND_MESSAGE = "Invalid user id!";
    public static final String TASK_NOT_FOUND_MESSAGE = "Invalid task id!";
    public static final String INVALID_USER_SERVICE_MODEL_MESSAGE = "Invalid data!";
    public static final String USERNAME_NOT_FOUND = "Username not found!";

    //LocalDateTime format pattern
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    //Log messages
    // user
    public static final String REGISTERED_USER_SUCCESSFULLY = "Registered successfully %s with generated id:%s";
    public static final String UPDATED_USER_PASSWORD = "Updated password";
    public static final String UPDATED_USER_IMAGE = "Updated image";
    public static final String UPDATED_USER_EMAIL = "Updated email";
    public static final String UPDATED_USER_FIRST_NAME = "Updated first name";
    public static final String UPDATED_USER_LAST_NAME = "Updated last name";
    public static final String UPDATED_USER_SUCCESSFULLY = "Updated successfully profile:";
    public static final String ASSIGNED_TASK_SUCCESSFULLY = "Assigned task with name: %s  to user with username: %s successfully";
    public static final String COMPLETED_TASK_SUCCESSFULLY = "User with username %s completed task with name %s successfully";
    public static final String CHANGED_ROLE_SUCCESSFULLY = "Role of user with username %s was changed to %s successfully";
    public static final String PROMOTED_SUCCESSFULLY = "Promoted user with username %s to %s successfully";
    public static final String DEMOTED_SUCCESSFULLY = "Demoted user with username %s to %s successfully";


}
