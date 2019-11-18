package dreamcompany.validation.office;

public final class OfficeConstants {

    //office field names
    public static final String ADDRESS_FIELD = "address";
    public static final String PHONE_NUMBER_FIELD = "phoneNumber";
    public static final String TOWN_FIELD = "town";
    public static final String COUNTRY_FIELD = "country";

    //office invalid input error messages
    public final static String ADDRESS_IS_INVALID = "Length must be between 5 and 50 symbols!";
    public final static String ADDRESS_ALREADY_EXIST = "Office with same address already exist!";
    public final static String PHONE_NUMBER_INVALID = "Length must be between 7 and 10 symbols and digits only!";
    public final static String TOWN_IS_INVALID = "Town must start with capital letter and contain letters only!";
    public final static String COUNTRY_IS_INVALID = "Country must start with capital letter and contain letters only!";

    //office fields constraints
    public final static String PHONE_NUMBER_PATTERN_STRING = "^[0-9]{7,10}$";
    public final static String ADDRESS_PATTERN_STRING = "^[\\p{L}\\s\\d]{5,50}$";
    public final static String TOWN_COUNTRY_PATTERN_STRING = "^[A-Z][a-z]+$";
}
