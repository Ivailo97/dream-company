package dreamcompany.validation.office.binding;

import dreamcompany.domain.model.binding.OfficeCreateBindingModel;
import dreamcompany.repository.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static dreamcompany.validation.office.OfficeConstants.*;

@dreamcompany.validation.annotation.Validator
public class OfficeCreateValidator implements Validator {

    private final OfficeRepository officeRepository;

    @Autowired
    public OfficeCreateValidator(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return OfficeCreateBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        OfficeCreateBindingModel office = (OfficeCreateBindingModel) o;

        if (!office.getAddress().matches(ADDRESS_PATTERN_STRING)) {
            errors.rejectValue(ADDRESS_FIELD, ADDRESS_IS_INVALID, ADDRESS_IS_INVALID);
        }

        if (officeRepository.existsByAddress(office.getAddress())) {
            errors.rejectValue(ADDRESS_FIELD, ADDRESS_ALREADY_EXIST, ADDRESS_ALREADY_EXIST);
        }

        if (!office.getPhoneNumber().matches(PHONE_NUMBER_PATTERN_STRING)) {
            errors.rejectValue(PHONE_NUMBER_FIELD, PHONE_NUMBER_INVALID, PHONE_NUMBER_INVALID);
        }

        if (!office.getTown().matches(TOWN_COUNTRY_PATTERN_STRING)) {
            errors.rejectValue(TOWN_FIELD, TOWN_IS_INVALID, TOWN_IS_INVALID);
        }

        if (!office.getCountry().matches(TOWN_COUNTRY_PATTERN_STRING)) {
            errors.rejectValue(COUNTRY_FIELD, COUNTRY_IS_INVALID, COUNTRY_IS_INVALID);
        }
    }
}
