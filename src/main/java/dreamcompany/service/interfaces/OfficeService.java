package dreamcompany.service.interfaces;

import dreamcompany.domain.model.service.OfficeServiceModel;

import java.util.List;

public interface OfficeService {

    OfficeServiceModel create(OfficeServiceModel officeServiceModel);

    OfficeServiceModel edit(String id, OfficeServiceModel officeServiceModel);

    OfficeServiceModel delete(String id);

    List<OfficeServiceModel> findAll();

    OfficeServiceModel findById(String id);
}
