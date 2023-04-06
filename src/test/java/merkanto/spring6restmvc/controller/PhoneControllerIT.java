package merkanto.spring6restmvc.controller;

import merkanto.spring6restmvc.entities.Phone;
import merkanto.spring6restmvc.entities.mappers.PhoneMapper;
import merkanto.spring6restmvc.model.PhoneDTO;
import merkanto.spring6restmvc.repositories.PhoneRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PhoneControllerIT {
    @Autowired
    PhoneController phoneController;

    @Autowired
    PhoneRepository phoneRepository;

    @Autowired
    PhoneMapper phoneMapper;

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            phoneController.deleteById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void deleteByIdFound() {
        Phone phone = phoneRepository.findAll().get(0);
        ResponseEntity responseEntity = phoneController.deleteById(phone.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(phoneRepository.findById(phone.getId()).isEmpty());
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> {
            phoneController.updateById(UUID.randomUUID(), PhoneDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void updateExistingPhone() {
        Phone phone = phoneRepository.findAll().get(0);
        PhoneDTO phoneDTO = phoneMapper.phoneToPhoneDto(phone);

        phoneDTO.setId(null);
        phoneDTO.setVersion(null);

        final String phoneName = "UPDATED";
        phoneDTO.setPhoneName(phoneName);

        ResponseEntity responseEntity = phoneController.updateById(phone.getId(), phoneDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Phone updatedPhone = phoneRepository.findById(phone.getId()).get();
        assertThat(updatedPhone.getPhoneName()).isEqualTo(phoneName);
    }

    @Rollback
    @Transactional
    @Test
    void saveNewPhoneTest() {
        PhoneDTO phoneDTO = PhoneDTO.builder()
                .phoneName("New Phone")
                .build();

        ResponseEntity responseEntity = phoneController.handlePost(phoneDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Phone phone = phoneRepository.findById(savedUUID).get();
        assertThat(phone).isNotNull();
    }

    @Test
    void testPhoneIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            phoneController.getPhoneById(UUID.randomUUID());
        });

    }

    @Test
    void testGetById() {
        Phone phone = phoneRepository.findAll().get(0);
        PhoneDTO dto = phoneController.getPhoneById(phone.getId());
        assertThat(dto).isNotNull();
    }

    @Test
    void testListPhones() {
        List<PhoneDTO> dtos = phoneController.listPhones();
        assertThat(dtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        phoneRepository.deleteAll();
        List<PhoneDTO> dtos = phoneController.listPhones();
        assertThat(dtos.size()).isZero();
    }
}