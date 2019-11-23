package dreamcompany.service.implementation;

import dreamcompany.domain.model.service.OfficeServiceModel;
import dreamcompany.service.interfaces.OfficeValidationService;
import org.springframework.stereotype.Service;

import static dreamcompany.validation.office.OfficeConstants.*;

@Service
public class OfficeValidationServiceImpl implements OfficeValidationService {

    @Override
    public boolean isValid(OfficeServiceModel office) {
        return office != null && fieldsAreValid(office);
    }

    private boolean fieldsAreValid(OfficeServiceModel office) {
        return fieldsAreNotNull(office) && fieldsMeetTheRequirements(office);
    }

    private boolean fieldsMeetTheRequirements(OfficeServiceModel office) {
        return office.getAddress().matches(ADDRESS_PATTERN_STRING)
                && office.getTown().matches(TOWN_COUNTRY_PATTERN_STRING)
                && office.getCountry().matches(TOWN_COUNTRY_PATTERN_STRING)
                && office.getPhoneNumber().matches(PHONE_NUMBER_PATTERN_STRING);
    }

    private boolean fieldsAreNotNull(OfficeServiceModel office) {
        return office.getAddress() != null
                && office.getCountry() != null
                && office.getPhoneNumber() != null
                && office.getTown() != null;
    }
}
