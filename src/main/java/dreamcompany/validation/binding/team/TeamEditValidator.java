package dreamcompany.validation.binding.team;

import dreamcompany.domain.model.binding.TeamEditBindingModel;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static dreamcompany.validation.binding.ValidationConstants.*;
import static dreamcompany.validation.binding.ValidationConstants.OFFICE_IS_MANDATORY;

@dreamcompany.validation.binding.annotation.Validator
public class TeamEditValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return TeamEditBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        TeamEditBindingModel team = (TeamEditBindingModel) o;

        if (team.getName() == null || team.getName().isEmpty()) {
            errors.rejectValue(NAME_FIELD, NAME_IS_MANDATORY, NAME_IS_MANDATORY);
        }

        if (team.getOffice() == null || team.getOffice().isEmpty()){
            errors.rejectValue(OFFICE_FIELD,OFFICE_IS_MANDATORY,OFFICE_IS_MANDATORY);
        }
    }
}
