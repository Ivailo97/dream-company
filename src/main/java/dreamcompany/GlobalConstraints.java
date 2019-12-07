package dreamcompany;

public final class GlobalConstraints {

    public static final Integer MAX_CREDITS = 100;
    public static final Integer STARTING_CREDITS = 0;
    public static final Integer EMPLOYEES_MIN_COUNT = 2;

    //roles
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_MODERATOR = "ROLE_MODERATOR";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_ROOT = "ROLE_ROOT";

    //error handler
    public static final String GLOBAL_EXCEPTION_VIEW_NAME = "/error/error";
    public static final String ERROR_PAGE_STATUS_CODE_ATTRIBUTE_NAME = "statusCode";
    public static final String ERROR_PAGE_MESSAGE_ATTRIBUTE_NAME = "message";

    //exception messages
    public static final String DUPLICATE_PROJECT_MESSAGE = "Project with that name already exist!";
    public static final String DUPLICATE_ADDRESS_MESSAGE = "Office with same address already exist!";
    public static final String DUPLICATE_USER_USERNAME_MESSAGE = "User with the same username already exist!";
    public static final String DUPLICATE_USER_EMAIL_MESSAGE = "User with the same email already exist!";
    public static final String WRONG_OLD_PASSWORD_MESSAGE = "Wrong old password!";
    public static final String DUPLICATE_TASK_MESSAGE = "Task with that name already exist in this project!";
    public static final String DUPLICATE_TEAM_MESSAGE = "Team with that name already exist!";

    public static final String USER_NOT_FOUND_MESSAGE = "Invalid user id!";
    public static final String TASK_NOT_FOUND_MESSAGE = "Invalid task id!";
    public static final String OFFICE_NOT_FOUND_MESSAGE = "Invalid office id!";
    public static final String TEAM_NOT_FOUND_MESSAGE = "Invalid team id!";
    public static final String PROJECT_NOT_FOUND_MESSAGE = "Invalid project id!";
    public static final String FRIEND_REQUEST_NOT_FOUND_MESSAGE = "Invalid request id!";

    public static final String INVALID_USER_SERVICE_MODEL_MESSAGE = "Invalid user data entered!";
    public static final String INVALID_FRIEND_REQUEST_MESSAGE = "Cant send a friend request to yourself!";
    public static final String INVALID_TEAM_SERVICE_MODEL_MESSAGE = "Invalid team data entered!";
    public static final String INVALID_PROJECT_SERVICE_MODEL_MESSAGE = "Invalid project data entered!";
    public static final String INVALID_TASK_SERVICE_MODEL_MESSAGE = "Invalid task data entered!";
    public static final String INVALID_OFFICE_SERVICE_MODEL_MESSAGE = "Invalid office data entered!";
    public static final String INVALID_REQUEST_MESSAGE = "Already friends!";
    public static final String INVALID_REQUEST_USER_HAS_ALREADY_SEND = "User has already send you a request!";
    public static final String USERNAME_NOT_FOUND = "Username not found!";

    //LocalDateTime format pattern
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    //Log messages
    //user
    public static final String REGISTERED_USER_SUCCESSFULLY = "Registered successfully %s with generated id:%s";
    public static final String UPDATED_USER_PASSWORD = "Updated password";
    public static final String UPDATED_USER_IMAGE = "Updated image";
    public static final String UPDATED_USER_EMAIL = "Updated email";
    public static final String UPDATED_USER_FIRST_NAME = "Updated first name";
    public static final String UPDATED_USER_LAST_NAME = "Updated last name";
    public static final String UPDATED_USER_SUCCESSFULLY = "Updated successfully profile:";
    public static final String ASSIGNED_TASK_SUCCESSFULLY = "Assigned task with name: %s to user with username: %s successfully";
    public static final String COMPLETED_TASK_SUCCESSFULLY = "User with username %s completed task with name %s successfully";
    public static final String CHANGED_ROLE_SUCCESSFULLY = "Role of user with username %s was changed to %s successfully";
    public static final String CHANGING_ROLE_FAILED = "Failed to change role";
    public static final String PROMOTED_SUCCESSFULLY = "Promoted user with username %s to %s successfully";
    public static final String DEMOTED_SUCCESSFULLY = "Demoted user with username %s to %s successfully";

    //team
    public static final String CREATED_TEAM_SUCCESSFULLY = "Created team with following name: %s and employees %s successfully";
    public static final String UPDATED_TEAM_SUCCESSFULLY = "Updated team: %s successfully:";
    public static final String UPDATED_TEAM_NAME = "Updated team name to: %s";
    public static final String UPDATED_TEAM_OFFICE = "Updated office";
    public static final String UPDATED_TEAM_LOGO = "Updated team logo";
    public static final String DELETED_TEAM_SUCCESSFULLY = "Deleted team with name: %s successfully";
    public static final String ASSIGNED_PROJECT_SUCCESSFULLY = "Assigned project with name: %s to team with name: %s successfully";
    public static final String ADDED_EMPLOYEE_TO_TEAM_SUCCESSFULLY = "Added employee with name: %s to team with name: %s successfully";
    public static final String REMOVED_EMPLOYEE_FROM_TEAM_SUCCESSFULLY = "Removed employee with name: %s from team with name: %s successfully";
}
