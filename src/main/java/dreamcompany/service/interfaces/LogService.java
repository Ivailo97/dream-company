package dreamcompany.service.interfaces;


import dreamcompany.domain.model.service.LogServiceModel;

import java.util.List;

public interface LogService {

    // returns log message
    String create(LogServiceModel log);

    List<LogServiceModel> getLogsOrderedByDate();
}
