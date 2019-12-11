package dreamcompany.service.interfaces;

import dreamcompany.domain.model.service.LogServiceModel;

public interface LogService {

    // returns log message
    String create(LogServiceModel log);
}
