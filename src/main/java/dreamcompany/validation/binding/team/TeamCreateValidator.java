package dreamcompany.validation.binding.team;

import dreamcompany.domain.model.binding.TeamCreateBindingModel;
import dreamcompany.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import static dreamcompany.validation.binding.ValidationConstants.*;

@dreamcompany.validation.binding.annotation.Validator
public class TeamCreateValidator implements Validator {

    private final TeamRepository teamRepository;

    @Autowired
    public TeamCreateValidator(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return TeamCreateBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        TeamCreateBindingModel team = (TeamCreateBindingModel) o;

        if (team.getName() == null || team.getName().isEmpty()) {
            errors.rejectValue(NAME_FIELD, NAME_IS_MANDATORY, NAME_IS_MANDATORY);
        }

        if (team.getOffice() == null || team.getOffice().isEmpty()){
            errors.rejectValue(OFFICE_FIELD,OFFICE_IS_MANDATORY,OFFICE_IS_MANDATORY);
        }

        if ( team.getEmployees() == null || team.getEmployees().size() < EMPLOYEES_MIN_COUNT){
            errors.rejectValue(EMPLOYEES_FIELD,EMPLOYEES_COUNT_INVALID,EMPLOYEES_COUNT_INVALID);
        }

        if (team.getLogo() == null || team.getLogo().isEmpty()){
            errors.rejectValue(LOGO_FIELD,LOGO_IS_MANDATORY,LOGO_IS_MANDATORY);
        }

        if (teamRepository.findByName(team.getName()).isPresent()){
            errors.rejectValue(NAME_FIELD,NAME_ALREADY_EXIST,NAME_ALREADY_EXIST);
        }
    }
}
