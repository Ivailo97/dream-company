package dreamcompany.service.implementation;

import dreamcompany.GlobalConstraints;
import dreamcompany.domain.entity.Office;
import dreamcompany.domain.model.service.OfficeServiceModel;
import dreamcompany.error.duplicates.OfficeAddressAlreadyExists;
import dreamcompany.repository.OfficeRepository;
import dreamcompany.service.interfaces.OfficeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository officeRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public OfficeServiceImpl(OfficeRepository officeRepository, ModelMapper modelMapper) {
        this.officeRepository = officeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OfficeServiceModel create(OfficeServiceModel officeServiceModel) {

        Office office = modelMapper.map(officeServiceModel, Office.class);
        office = officeRepository.save(office);

        return modelMapper.map(office, OfficeServiceModel.class);
    }

    @Override
    public OfficeServiceModel edit(String id, OfficeServiceModel edited) {

        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid office id"));

        if (!office.getAddress().equals(edited.getAddress())){
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
        if (officeRepository.existsByAddress(address)){
            throw new OfficeAddressAlreadyExists(GlobalConstraints.DUPLICATE_ADDRESS_MESSAGE);
        }
    }

    @Override
    public OfficeServiceModel delete(String id) {

        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid office id"));

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
                .orElseThrow(() -> new IllegalArgumentException("Invalid office id!"));
    }
}
