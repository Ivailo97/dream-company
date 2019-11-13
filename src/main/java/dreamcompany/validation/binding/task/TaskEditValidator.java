package dreamcompany.validation.binding.task;

import dreamcompany.domain.model.binding.TaskEditBindingModel;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dreamcompany.validation.binding.ValidationConstants.*;

@dreamcompany.validation.binding.annotation.Validator
public class TaskEditValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return TaskEditBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        TaskEditBindingModel task = (TaskEditBindingModel) o;

        if (task.getProject() == null || task.getProject().isEmpty()) {
            errors.rejectValue(PROJECT_FIELD, PROJECT_IS_MANDATORY, PROJECT_IS_MANDATORY);
        }

        if (task.getName() == null || task.getName().isEmpty()) {
            errors.rejectValue(NAME_FIELD, NAME_IS_MANDATORY, NAME_IS_MANDATORY);
        }

        if (task.getCredits() == null){
            errors.rejectValue(CREDITS_FIELD,CREDITS_ARE_MANDATORY,CREDITS_ARE_MANDATORY);
        }

        if (creditsAreNotInRange(task)) {
            errors.rejectValue(CREDITS_FIELD, CREDITS_COUNT_INVALID, CREDITS_COUNT_INVALID);
        }

        Pattern pattern = Pattern.compile(DESCRIPTION_PATTERN_STRING);
        Matcher matcher = pattern.matcher(task.getDescription());

        if (!matcher.matches()) {
            errors.rejectValue(DESCRIPTION_FIELD, DESCRIPTION_IS_INVALID, DESCRIPTION_IS_INVALID);
        }

        if (task.getRequiredPosition() == null || task.getRequiredPosition().isEmpty()) {
            errors.rejectValue(REQUIRED_POSITION_FIELD, POSITION_IS_MANDATORY, POSITION_IS_MANDATORY);
        }

        if (task.getMinutesNeeded() < MIN_MINUTES || task.getMinutesNeeded() > MAX_MINUTES){
            errors.rejectValue(MINUTES_FIELD,MINUTES_COUNT_INVALID,MINUTES_COUNT_INVALID);
        }
    }

    private boolean creditsAreNotInRange(TaskEditBindingModel task){
        return task.getCredits() != null
                && task.getCredits() < MIN_CREDITS
                || task.getCredits() != null
                && task.getCredits() > MAX_CREDITS;
    }
}
