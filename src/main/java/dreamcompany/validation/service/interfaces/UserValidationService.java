package dreamcompany.validation.service.interfaces;

import dreamcompany.domain.model.service.UserServiceModel;

public interface UserValidationService {

    boolean isValid(UserServiceModel userServiceModel);
}
