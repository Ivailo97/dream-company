package dreamcompany.validation.user.service;

import dreamcompany.domain.model.service.UserServiceModel;

public interface UserValidationService {

    boolean isValid(UserServiceModel userServiceModel);
}
