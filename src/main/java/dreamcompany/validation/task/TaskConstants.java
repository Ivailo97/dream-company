package dreamcompany.validation.task;

public final class TaskConstants {

    //task field names
    public static final String TASK_NAME_FIELD = "name";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String REQUIRED_POSITION_FIELD = "requiredPosition";
    public static final String CREDITS_FIELD = "credits";
    public static final String PROJECT_FIELD = "project";
    public static final String MINUTES_FIELD = "minutesNeeded";

    //task invalid input error messages
    public static final String NAME_IS_INVALID = "Name must start with capital letter and must be between 4 and 11 symbols long!";
    public static final String POSITION_IS_MANDATORY = "Select required position!";
    public static final String DESCRIPTION_IS_INVALID = "Description must be between 10 and 300 symbols long!";
    public static final String TASK_ALREADY_EXIST_IN_THIS_PROJECT = "Task with same name already exist in the project!";
    public static final String PROJECT_IS_MANDATORY = "Select project!";
    public static final String CREDITS_ARE_MANDATORY = "Enter credits!";
    public static final String CREDITS_COUNT_INVALID = "Credits must be between 2 and 20!";
    public static final String MINUTES_COUNT_INVALID = "Minutes must be between 1 and 60!";

    //task fields constraints
    public static final int MIN_CREDITS = 2;
    public static final int MAX_CREDITS = 20;
    public final static String TASK_NAME_PATTERN_STRING = "^[\\p{Lu}]\\p{L}{3,10}$";
    public final static String DESCRIPTION_PATTERN_STRING = "^[\\p{L}][\\p{L}\\s]{9,299}$";
    public static final int MIN_MINUTES = 1;
    public static final int MAX_MINUTES = 60;
}
