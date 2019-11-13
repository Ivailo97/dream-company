package dreamcompany.validation.binding.office;

import dreamcompany.domain.model.binding.OfficeEditBindingModel;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dreamcompany.validation.binding.ValidationConstants.*;

@dreamcompany.validation.binding.annotation.Validator
public class OfficeEditValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return OfficeEditBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        OfficeEditBindingModel office = (OfficeEditBindingModel) o;

        if (office.getAddress() == null || office.getAddress().isEmpty()) {
            errors.rejectValue(ADDRESS_FIELD, ADDRESS_IS_MANDATORY, ADDRESS_IS_MANDATORY);
        }

        if (office.getAddress().length() < ADDRESS_MIN_LENGTH || office.getAddress().length() > ADDRESS_MAX_LENGTH) {
            errors.rejectValue(ADDRESS_FIELD, ADDRESS_LENGTH_INVALID, ADDRESS_LENGTH_INVALID);
        }

        Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN_STRING);
        Matcher matcher = pattern.matcher(office.getPhoneNumber());

        if (!matcher.matches()) {
            errors.rejectValue(PHONE_NUMBER_FIELD, PHONE_NUMBER_INVALID,PHONE_NUMBER_INVALID);
        }

        pattern = Pattern.compile(TOWN_COUNTRY_PATTERN_STRING);
        matcher = pattern.matcher(office.getTown());

        if (!matcher.matches()) {
            errors.rejectValue(TOWN_FIELD, TOWN_IS_INVALID, TOWN_IS_INVALID);
        }

        matcher = pattern.matcher(office.getCountry());

        if (!matcher.matches()) {
            errors.rejectValue(COUNTRY_FIELD, COUNTRY_IS_INVALID, COUNTRY_IS_INVALID);
        }
    }
}
