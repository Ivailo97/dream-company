package dreamcompany.validation.team.binding;

import dreamcompany.domain.model.binding.TeamEditBindingModel;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static dreamcompany.validation.team.TeamConstants.*;

@dreamcompany.validation.annotation.Validator
public class TeamEditValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return TeamEditBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        TeamEditBindingModel team = (TeamEditBindingModel) o;

        if (!team.getName().matches(NAME_PATTERN)) {
            errors.rejectValue(TEAM_NAME_FIELD, TEAM_NAME_IS_INVALID, TEAM_NAME_IS_INVALID);
        }

        if (team.getOffice() == null || team.getOffice().isEmpty()){
            errors.rejectValue(OFFICE_FIELD,OFFICE_IS_MANDATORY,OFFICE_IS_MANDATORY);
        }
    }
}
