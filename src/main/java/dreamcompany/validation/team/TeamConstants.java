package dreamcompany.validation.team;

public final class TeamConstants {

    //team field names
    public static final String TEAM_NAME_FIELD = "name";
    public static final String OFFICE_FIELD = "office";
    public static final String EMPLOYEES_FIELD = "employees";
    public static final String LOGO_FIELD = "logo";

    //team invalid input error messages
    public final static String TEAM_NAME_IS_MANDATORY = "Enter name!";
    public final static String OFFICE_IS_MANDATORY = "Select office!";
    public final static String EMPLOYEES_COUNT_INVALID = "Select at least two employees!";
    public final static String LOGO_IS_MANDATORY = "Upload logo!";
    public final static String NAME_ALREADY_EXIST = "Team with that name already exist!";

    //team fields constraints
    public static final int EMPLOYEES_MIN_COUNT = 2;
}
