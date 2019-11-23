package dreamcompany.service.implementation;

import dreamcompany.domain.model.service.TaskServiceModel;
import dreamcompany.service.interfaces.TaskValidationService;
import org.springframework.stereotype.Service;

import static dreamcompany.validation.task.TaskConstants.*;

@Service
public class TaskValidationServiceImpl implements TaskValidationService {

    @Override
    public boolean isValid(TaskServiceModel task) {
        return task != null && fieldsAreValid(task);
    }

    private boolean fieldsAreValid(TaskServiceModel task) {
        return fieldsAreNotNull(task) && fieldsMeetTheRequirements(task);
    }

    private boolean fieldsMeetTheRequirements(TaskServiceModel task) {
        return task.getName().matches(TASK_NAME_PATTERN_STRING)
                && task.getDescription().matches(DESCRIPTION_PATTERN_STRING)
                && creditsAreInRange(task.getCredits())
                && minutesAreInRange(task.getMinutesNeeded());
    }

    private boolean minutesAreInRange(long minutesNeeded) {
        return minutesNeeded >= MIN_MINUTES && minutesNeeded <= MAX_MINUTES;
    }

    private boolean fieldsAreNotNull(TaskServiceModel task) {
        return task.getProject() != null
                && task.getName() != null
                && task.getDescription() != null
                && task.getCredits() != null
                && task.getRequiredPosition() != null;
    }

    private boolean creditsAreInRange(Integer credits) {
        return credits.compareTo(MIN_CREDITS) >= 0
                && credits.compareTo(MAX_CREDITS) <= 0;
    }
}
