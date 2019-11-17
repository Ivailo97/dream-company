package dreamcompany.validation.task.binding;

import dreamcompany.domain.model.binding.TaskCreateBindingModel;
import dreamcompany.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static dreamcompany.validation.task.TaskConstants.*;

@dreamcompany.validation.annotation.Validator
public class TaskCreateValidator implements Validator {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskCreateValidator(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return TaskCreateBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        TaskCreateBindingModel task = (TaskCreateBindingModel) o;

        if (task.getProject() == null || task.getProject().isEmpty()) {
            errors.rejectValue(PROJECT_FIELD, PROJECT_IS_MANDATORY, PROJECT_IS_MANDATORY);
        }

        if (!task.getName().matches(TASK_NAME_PATTERN_STRING)) {
            errors.rejectValue(TASK_NAME_FIELD, NAME_IS_INVALID, NAME_IS_INVALID);
        }

        if (taskWithSameNameExistInTheProject(task)) {
            errors.rejectValue(TASK_NAME_FIELD, TASK_ALREADY_EXIST_IN_THIS_PROJECT, TASK_ALREADY_EXIST_IN_THIS_PROJECT);
        }

        if (task.getCredits() == null){
            errors.rejectValue(CREDITS_FIELD,CREDITS_ARE_MANDATORY,CREDITS_ARE_MANDATORY);
        }

        if (creditsAreNotInRange(task)) {
            errors.rejectValue(CREDITS_FIELD, CREDITS_COUNT_INVALID, CREDITS_COUNT_INVALID);
        }

        if (!task.getDescription().matches(DESCRIPTION_PATTERN_STRING)) {
            errors.rejectValue(DESCRIPTION_FIELD, DESCRIPTION_IS_INVALID, DESCRIPTION_IS_INVALID);
        }

        if (task.getRequiredPosition() == null || task.getRequiredPosition().isEmpty()) {
            errors.rejectValue(REQUIRED_POSITION_FIELD, POSITION_IS_MANDATORY, POSITION_IS_MANDATORY);
        }

        if (task.getMinutesNeeded() < MIN_MINUTES || task.getMinutesNeeded() > MAX_MINUTES){
            errors.rejectValue(MINUTES_FIELD,MINUTES_COUNT_INVALID,MINUTES_COUNT_INVALID);
        }
    }

    private boolean creditsAreNotInRange(TaskCreateBindingModel task){
        return task.getCredits() != null
                && task.getCredits() < MIN_CREDITS
                || task.getCredits() != null
                && task.getCredits() > MAX_CREDITS;
    }

    private boolean taskWithSameNameExistInTheProject(TaskCreateBindingModel task) {
        return task.getProject() != null
                && !task.getProject().isEmpty()
                && task.getName() != null
                && !task.getName().isEmpty()
                && taskRepository.existsByNameAndProjectId(task.getName(), task.getProject());
    }
}
