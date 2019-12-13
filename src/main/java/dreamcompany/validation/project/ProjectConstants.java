package dreamcompany.validation.project;

import java.math.BigDecimal;

public final class ProjectConstants {

    //project field names
    public static final String PROJECT_NAME_FIELD = "name";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String REWARD_FIELD = "reward";

    //project invalid input error messages
    public static final String NAME_IS_INVALID = "Name must start with capital letter and must be between 4 and 12 symbols long!";
    public static final String PROJECT_ALREADY_EXIST = "Project with same name already exists!";
    public static final String DESCRIPTION_IS_INVALID = "Description must be between 10 and 300 symbols long!";
    public static final String REWARD_IS_NEGATIVE = "Reward must be a positive number!";
    public static final String REWARD_IS_MANDATORY = "Enter reward!";

    //project fields constraints
    public static final BigDecimal REWARD_MIN_VALUE = new BigDecimal("0.01");
    public final static String PROJECT_NAME_PATTERN_STRING = "^[\\p{Lu}]\\p{L}{3,11}$";
    public final static String DESCRIPTION_PATTERN_STRING = "^[\\p{L}][\\p{L}\\s]{9,299}$";
}
