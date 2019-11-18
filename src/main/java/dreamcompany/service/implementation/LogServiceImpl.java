package dreamcompany.service.implementation;

import dreamcompany.GlobalConstraints;
import dreamcompany.domain.entity.Log;
import dreamcompany.domain.entity.User;
import dreamcompany.domain.model.service.LogServiceModel;
import dreamcompany.error.notexist.UserNotFoundException;
import dreamcompany.repository.LogRepository;
import dreamcompany.repository.UserRepository;
import dreamcompany.service.interfaces.LogService;
import dreamcompany.validation.log.service.LogValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    private final UserRepository userRepository;
    private final LogValidationService logValidationService;
    private final ModelMapper modelMapper;

    @Autowired
    public LogServiceImpl(LogRepository logRepository, UserRepository userRepository, LogValidationService logValidationService, ModelMapper modelMapper) {
        this.logRepository = logRepository;
        this.userRepository = userRepository;
        this.logValidationService = logValidationService;
        this.modelMapper = modelMapper;
    }

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

    //TODO delete or find a use
    private LocalDateTime formatDate(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(GlobalConstraints.DATE_PATTERN);

        return LocalDateTime.parse(dateTimeString.replace("T", " ")
                .substring(0, dateTimeString.lastIndexOf(".")), formatter);
    }

    @Override
    public List<LogServiceModel> getLogsOrderedByDate() {
        return logRepository.findAllOrderedByDateDesc()
                .stream()
                .map(l -> modelMapper.map(l, LogServiceModel.class))
                .collect(Collectors.toList());
    }
}
