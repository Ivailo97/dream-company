package dreamcompany.validation.binding.project;

import dreamcompany.domain.model.binding.ProjectCreateBindingModel;
import dreamcompany.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import static dreamcompany.validation.binding.ValidationConstants.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@dreamcompany.validation.binding.annotation.Validator
public class ProjectCreateValidator implements Validator {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectCreateValidator(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ProjectCreateBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        ProjectCreateBindingModel project = (ProjectCreateBindingModel) o;

        Pattern pattern = Pattern.compile(NAME_PATTERN_STRING);
        Matcher matcher = pattern.matcher(project.getName());

        if (!matcher.matches()){
            errors.rejectValue(NAME_FIELD,NAME_IS_INVALID,NAME_IS_INVALID);
        }

        if (projectRepository.existsByName(project.getName())){
            errors.rejectValue(NAME_FIELD,PROJECT_ALREADY_EXIST,PROJECT_ALREADY_EXIST);
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
