package dreamcompany.service.implementation;

import dreamcompany.domain.entity.BaseEntity;
import dreamcompany.domain.entity.Office;
import dreamcompany.domain.model.service.OfficeServiceModel;
import dreamcompany.error.duplicates.OfficeAddressAlreadyExists;
import dreamcompany.error.invalidservicemodels.InvalidOfficeServiceModelException;
import dreamcompany.error.notexist.OfficeNotFoundException;
import dreamcompany.repository.OfficeRepository;
import dreamcompany.service.interfaces.OfficeService;
import dreamcompany.service.interfaces.validation.OfficeValidationService;
import dreamcompany.service.interfaces.TeamService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static dreamcompany.common.GlobalConstants.*;

@Service
@AllArgsConstructor
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository officeRepository;

    private final OfficeValidationService officeValidationService;

    private final TeamService teamService;

    private final ModelMapper modelMapper;

    @Override
    public OfficeServiceModel create(OfficeServiceModel officeServiceModel) {

        throwIfInvalidServiceModel(officeServiceModel);

        Office office = modelMapper.map(officeServiceModel, Office.class);
        office = officeRepository.save(office);

        return modelMapper.map(office, OfficeServiceModel.class);
    }

    private void throwIfInvalidServiceModel(OfficeServiceModel officeServiceModel) {

        if (!officeValidationService.isValid(officeServiceModel)) {
            throw new InvalidOfficeServiceModelException(INVALID_OFFICE_SERVICE_MODEL_MESSAGE);
        }
    }

    @Override
    public OfficeServiceModel edit(String id, OfficeServiceModel edited) {

        throwIfInvalidServiceModel(edited);

        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new OfficeNotFoundException(OFFICE_NOT_FOUND_MESSAGE));

        if (!office.getAddress().equals(edited.getAddress())) {
            throwIfDuplicate(edited.getAddress());
        }

        office.setAddress(edited.getAddress());
        office.setTown(edited.getTown());
        office.setCountry(edited.getCountry());
        office.setPhoneNumber(edited.getPhoneNumber());

        office = officeRepository.save(office);

        return modelMapper.map(office, OfficeServiceModel.class);
    }

    private void throwIfDuplicate(String address) {
        if (officeRepository.existsByAddress(address)) {
            throw new OfficeAddressAlreadyExists(DUPLICATE_ADDRESS_MESSAGE);
        }
    }

    @Override
    public OfficeServiceModel delete(String id, String moderatorUsername) {

        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new OfficeNotFoundException(OFFICE_NOT_FOUND_MESSAGE));

        office.getTeams().stream()
                .map(BaseEntity::getId).forEach(t -> teamService.delete(t, moderatorUsername));

        officeRepository.delete(office);

        return modelMapper.map(office, OfficeServiceModel.class);
    }

    @Override
    public List<OfficeServiceModel> findAll() {
        return officeRepository.findAll()
                .stream()
                .map(x -> modelMapper.map(x, OfficeServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public OfficeServiceModel findById(String id) {

        return officeRepository.findById(id)
                .map(x -> modelMapper.map(x, OfficeServiceModel.class))
                .orElseThrow(() -> new OfficeNotFoundException(OFFICE_NOT_FOUND_MESSAGE));
    }
}
