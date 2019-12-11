package dreamcompany.service.implementation.office;

import dreamcompany.domain.entity.Office;
import dreamcompany.domain.model.service.OfficeServiceModel;
import dreamcompany.error.duplicates.OfficeAddressAlreadyExists;
import dreamcompany.error.invalidservicemodels.InvalidOfficeServiceModelException;
import dreamcompany.error.notexist.OfficeNotFoundException;
import dreamcompany.repository.OfficeRepository;
import dreamcompany.service.implementation.base.TestBase;
import dreamcompany.service.interfaces.OfficeService;
import dreamcompany.service.interfaces.validation.OfficeValidationService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class OfficeServiceCreateAndEditTests extends TestBase {

    @Autowired
    OfficeService officeService;

    @MockBean
    OfficeValidationService validationService;

    @MockBean
    OfficeRepository officeRepository;

    OfficeServiceModel office;

    @Override
    protected void before() {

        office = new OfficeServiceModel();
        when(validationService.isValid(any()))
                .thenReturn(true);
    }

    @Test
    public void create_shouldSaveOfficeInDb_whenValidModel() {

        office.setAddress("ulica Vasil Levski 63A");

        when(officeRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OfficeServiceModel savedAndReturnedOffice = officeService.create(office);

        verify(officeRepository).save(any());
        assertEquals(office.getAddress(), savedAndReturnedOffice.getAddress());
    }

    @Test(expected = InvalidOfficeServiceModelException.class)
    public void create_shouldThrowException_whenInvalidModel() {

        when(validationService.isValid(any()))
                .thenReturn(false);

        officeService.create(office);
    }

    @Test
    public void edit_shouldUpdateOfficeInDb_whenValidModelAndOfficeId() {

        Office officeInDb = new Office();
        officeInDb.setAddress("oldAddress");
        officeInDb.setCountry("oldCountry");
        officeInDb.setTown("oldTown");
        officeInDb.setPhoneNumber("oldPhoneNumber");

        when(officeRepository.findById(any()))
                .thenReturn(Optional.of(officeInDb));

        office.setAddress("newAddress");
        office.setCountry("newCountry");
        office.setTown("newTown");
        office.setPhoneNumber("newPhoneNumber");

        when(officeRepository.existsByAddress(any()))
                .thenReturn(false);

        when(officeRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        OfficeServiceModel updatedAndReturnedOffice = officeService.edit("officeId", office);

        //Assert
        verify(officeRepository).save(any());
        assertEquals(office.getAddress(), updatedAndReturnedOffice.getAddress());
        assertEquals(office.getCountry(), updatedAndReturnedOffice.getCountry());
        assertEquals(office.getTown(), updatedAndReturnedOffice.getTown());
        assertEquals(office.getPhoneNumber(), updatedAndReturnedOffice.getPhoneNumber());
    }

    @Test(expected = InvalidOfficeServiceModelException.class)
    public void edit_shouldThrowException_whenInvalidOfficeModel() {

      when(validationService.isValid(any()))
              .thenReturn(false);

      officeService.edit("officeId",office);
    }

    @Test(expected = OfficeNotFoundException.class)
    public void edit_shouldThrowException_whenInvalidOfficeId(){

        when(officeRepository.findById(any()))
                .thenReturn(Optional.empty());

        officeService.edit("officeId",office);
    }

    @Test(expected = OfficeAddressAlreadyExists.class)
    public void edit_shouldThrowException_whenEditedModelNameIsAlreadyTaken(){

        Office officeInDb = new Office();
        officeInDb.setAddress("oldAddress");

        office.setAddress("newAddress");

        when(officeRepository.findById(any()))
                .thenReturn(Optional.of(officeInDb));

        when(officeRepository.existsByAddress(any()))
                .thenReturn(true);

        officeService.edit("officeId",office);
    }


}
