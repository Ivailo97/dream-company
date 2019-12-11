package dreamcompany.service.implementation;

import dreamcompany.domain.entity.Log;
import dreamcompany.domain.entity.User;
import dreamcompany.domain.model.service.LogServiceModel;
import dreamcompany.error.notexist.UserNotFoundException;
import dreamcompany.repository.LogRepository;
import dreamcompany.repository.UserRepository;
import dreamcompany.service.interfaces.LogService;
import dreamcompany.service.interfaces.validation.LogValidationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    private final UserRepository userRepository;
    private final LogValidationService logValidationService;
    private final ModelMapper modelMapper;

    @Override
    public String create(LogServiceModel logServiceModel) {

        throwIfInvalidServiceModel(logServiceModel);

        Log log = modelMapper.map(logServiceModel, Log.class);

        User user = userRepository.findByUsername(logServiceModel.getUsername()).orElse(null);

        throwIfUserNotFound(user);

        log = logRepository.save(log);

        return log.getDescription();
    }


    private void throwIfUserNotFound(User user) {

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
    }

    private void throwIfInvalidServiceModel(LogServiceModel logServiceModel) {
        if (!logValidationService.isValid(logServiceModel)) {
            throw new IllegalArgumentException("Invalid log service model");
        }
    }

}
