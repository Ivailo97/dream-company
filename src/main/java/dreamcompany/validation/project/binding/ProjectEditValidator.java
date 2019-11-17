package dreamcompany.validation.project.binding;

import dreamcompany.domain.model.binding.ProjectEditBindingModel;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dreamcompany.validation.project.ProjectConstants.*;

@dreamcompany.validation.annotation.Validator
public class ProjectEditValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return ProjectEditBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        ProjectEditBindingModel project = (ProjectEditBindingModel) o;

        Pattern pattern = Pattern.compile(PROJECT_NAME_PATTERN_STRING);
        Matcher matcher = pattern.matcher(project.getName());

        if (!matcher.matches()){
            errors.rejectValue(PROJECT_NAME_FIELD,NAME_IS_INVALID,NAME_IS_INVALID);
        }

        pattern = Pattern.compile(DESCRIPTION_PATTERN_STRING);
        matcher = pattern.matcher(project.getDescription());

        if (!matcher.matches()){
            errors.rejectValue(DESCRIPTION_FIELD,DESCRIPTION_IS_INVALID,DESCRIPTION_IS_INVALID);
        }

        if (project.getReward() == null){
            errors.rejectValue(REWARD_FIELD,REWARD_IS_MANDATORY,REWARD_IS_MANDATORY);
        }

        if ( project.getReward() != null && project.getReward().compareTo(REWARD_MIN_VALUE) < 0){
            errors.rejectValue(REWARD_FIELD,REWARD_IS_NEGATIVE,REWARD_IS_NEGATIVE);
        }
    }
}
