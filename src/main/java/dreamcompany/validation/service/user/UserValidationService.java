package dreamcompany.validation.service.user;

import dreamcompany.domain.model.service.UserServiceModel;

public interface UserValidationService {

    boolean isValid(UserServiceModel userServiceModel);
}
