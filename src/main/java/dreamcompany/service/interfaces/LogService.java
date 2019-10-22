package dreamcompany.service.interfaces;


import dreamcompany.domain.model.service.LogServiceModel;

import java.util.List;

public interface LogService {

    LogServiceModel create(LogServiceModel log);

    List<LogServiceModel> getLogsOrderedByDate();
}
