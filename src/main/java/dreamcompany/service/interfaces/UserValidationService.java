package dreamcompany.service.interfaces;

import dreamcompany.domain.model.service.UserServiceModel;

public interface UserValidationService {

    boolean isValid(UserServiceModel userServiceModel);
}
