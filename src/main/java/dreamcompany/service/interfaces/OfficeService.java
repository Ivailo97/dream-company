package dreamcompany.service.interfaces;

import dreamcompany.domain.model.service.OfficeServiceModel;

import java.io.IOException;
import java.util.List;

public interface OfficeService {

    OfficeServiceModel create(OfficeServiceModel officeServiceModel);

    OfficeServiceModel edit(String id, OfficeServiceModel officeServiceModel);

    OfficeServiceModel delete(String id) throws IOException;

    List<OfficeServiceModel> findAll();

    OfficeServiceModel findById(String id);
}
