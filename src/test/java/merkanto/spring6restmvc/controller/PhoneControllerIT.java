package merkanto.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import merkanto.spring6restmvc.entities.Phone;
import merkanto.spring6restmvc.entities.mappers.PhoneMapper;
import merkanto.spring6restmvc.model.PhoneDTO;
import merkanto.spring6restmvc.model.PhoneStyle;
import merkanto.spring6restmvc.repositories.PhoneRepository;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PhoneControllerIT {
    @Autowired
    PhoneController phoneController;

    @Autowired
    PhoneRepository phoneRepository;

    @Autowired
    PhoneMapper phoneMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Disabled // just for demo purposes
    @Test
    void testUpdateBeerBadVersion() throws Exception {
        Phone phone = phoneRepository.findAll().get(0);

        PhoneDTO phoneDTO = phoneMapper.phoneToPhoneDto(phone);

        phoneDTO.setPhoneName("Updated Name");

        MvcResult result = mockMvc.perform(put(PhoneController.PHONE_PATH_ID, phone.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phoneDTO)))
                .andExpect(status().isNoContent())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

        phoneDTO.setPhoneName("Updated Name 2");

        MvcResult result2 = mockMvc.perform(put(PhoneController.PHONE_PATH_ID, phone.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phoneDTO)))
                .andExpect(status().isNoContent())
                .andReturn();

        System.out.println(result2.getResponse().getStatus());
    }

    @Test
    void tesListPhonesByStyleAndNameShowInventoryTruePage2() throws Exception {
        mockMvc.perform(get(PhoneController.PHONE_PATH)
                        .with(httpBasic(PhoneControllerTest.USERNAME, PhoneControllerTest.PASSWORD))
                        .queryParam("phoneName", "APPLE")
                        .queryParam("phoneStyle", PhoneStyle.APPLE.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageNumber", "2")
                        .queryParam("pageSize", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(0)));
//                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void tesListPhonesByStyleAndNameShowInventoryTrue() throws Exception {
        mockMvc.perform(get(PhoneController.PHONE_PATH)
                        .with(httpBasic(PhoneControllerTest.USERNAME, PhoneControllerTest.PASSWORD))
                        .queryParam("phoneName", "APPLE")
                        .queryParam("phoneStyle", PhoneStyle.APPLE.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(1)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void tesListPhonesByStyleAndNameShowInventoryFalse() throws Exception {
        mockMvc.perform(get(PhoneController.PHONE_PATH)
                        .with(httpBasic(PhoneControllerTest.USERNAME, PhoneControllerTest.PASSWORD))
                        .queryParam("phoneName", "APPLE")
                        .queryParam("phoneStyle", PhoneStyle.APPLE.name())
                        .queryParam("showInventory", "false")
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(1)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.nullValue()));
    }

    @Test
    void tesListPhonesByStyleAndName() throws Exception {
        mockMvc.perform(get(PhoneController.PHONE_PATH)
                        .with(httpBasic(PhoneControllerTest.USERNAME, PhoneControllerTest.PASSWORD))
                        .queryParam("phoneName", "APPLE")
                        .queryParam("phoneStyle", PhoneStyle.APPLE.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(11)));
    }

    @Test
    void testNoAuth() throws Exception {
        // Test No Auth
        mockMvc.perform(get(PhoneController.PHONE_PATH)
                        .queryParam("phoneStyle", PhoneStyle.APPLE.name()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testListPhonesByStyle() throws Exception {
        mockMvc.perform(get(PhoneController.PHONE_PATH)
                        .with(httpBasic(PhoneControllerTest.USERNAME, PhoneControllerTest.PASSWORD))
                        .queryParam("phoneStyle", PhoneStyle.APPLE.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(11)));
    }

    @Test
    void testListPhonesByName() throws Exception {
        mockMvc.perform(get(PhoneController.PHONE_PATH)
                        .with(httpBasic(PhoneControllerTest.USERNAME, PhoneControllerTest.PASSWORD))
                        .queryParam("phoneName", "APPLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(11)));
    }

    @Test
    void testPatchPhoneBadName() throws Exception {
        Phone phone = phoneRepository.findAll().get(0);

        Map<String, Object> phoneMap = new HashMap<>();
        phoneMap.put("phoneName", "New Name New Name New Name New Name New Name New Name New Name New Name New Name New Name New Name New Name New Name New Name New Name New Name New Name ");

        mockMvc.perform(patch(PhoneController.PHONE_PATH_ID, phone.getId())
                        .with(httpBasic(PhoneControllerTest.USERNAME, PhoneControllerTest.PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phoneMap)))
                .andExpect(status().isBadRequest());
    }

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
        Page<PhoneDTO> dtos = phoneController.listPhones(null, null, false, 1, 25);
        assertThat(dtos.getContent().size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        phoneRepository.deleteAll();
        Page<PhoneDTO> dtos = phoneController.listPhones(null, null, false, 1, 25);
        assertThat(dtos.getContent().size()).isZero();
    }
}