package dreamcompany.service.interfaces.validation;

import dreamcompany.domain.model.service.UserServiceModel;

public interface UserValidationService {

    boolean isValid(UserServiceModel userServiceModel);
}
